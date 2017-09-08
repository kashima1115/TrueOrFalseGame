package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import messageQueue.ServerActiveMQMessaging;
import net.sf.json.JSONObject;

/**
 * 試合全体の管理を行うクラス
 * @author kanayama
 *
 */
public class BattleAdmin {
	//エラー通知オブジェクト
	private InformError ie;

	//受信するイベント情報
	private final String READY;
	private final String TURN_END;
	private Map<String,LogicInfoBean> logicMap;
	private ClientLogicBean clb;

	//ゲーム勝敗関係
	private final String WIN;
	private final String LOSE;
	private final String CONTINUE;
	private final String DRAW;

	//手番関係
	private final String FIRST;
	private final String SECOND;

	//MQとDBにアクセスするクラスのオブジェクトをを格納する変数
	private ServerActiveMQMessaging samqm;
	private DbInsert dbi;


	/**
	 * コンストラクタ
	 */
	BattleAdmin(){
		ie=new InformError();
		READY="ready";
		TURN_END="TurnEnd";
		WIN="win";
		LOSE="lose";
		CONTINUE="continue";
		DRAW="draw";
		FIRST="first";
		SECOND="second";
	}
	/**
	 * MQとDBの接続処理を行う
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws UnknownHostException
	 */
	private void mqDbAccess() throws SQLException, ClassNotFoundException, UnknownHostException{
		//MQクラスをインスタンス化
		this.samqm=new ServerActiveMQMessaging();

		//DBアクセスクラスをインスタンス化
		this.dbi=new DbInsert();

		try{
			//DBコネクションを生成
			this.dbi.connect();

			//メッセージングクラスをインスタンス化
			 samqm=new ServerActiveMQMessaging();

			//起動しているマシンのIPアドレスを取得
			InetAddress ia=InetAddress.getLocalHost();

			//Queue作成メソッドの呼び出し
			samqm.createQueue(ia.getHostAddress());


		}catch(SQLException e){
			e.printStackTrace();
			throw e;

		}catch(ClassNotFoundException e){
			e.printStackTrace();
			throw e;

		}catch(UnknownHostException e){
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * ロジック情報を受信する
	 */
	private void logicReceive(LogicAdmin la){
		//メッセージ受信メソッドの呼び出し
		JSONObject gameInfo=this.samqm.receiveMessage();

		//イベント情報取得
		String event=gameInfo.getString("event");

		//受信メッセージ振り分け
		if(event.equals(this.READY)){

			//受信したロジック情報をMapに格納
			la.logicSet(gameInfo);

			//期待していたメッセージ受信できなかった場合
		}else if(!event.equals(this.READY)){

			System.out.println("受信メッセージを受け付けられません");
			System.out.println("再度メッセージを受信します");
		}
	}

	/**
	 * ロジック情報を登録
	 */
	private void enrollLogic(LogicAdmin la){
		try{
			//ロジック情報を格納したMapを取得
			this.logicMap=la.getLogicMap();

			//クライアントアドレスを格納したBeanを取得
			this.clb=la.getClientAddressBean();

			//最初に受信したロジック情報のBeanを取得
			LogicInfoBean lib=this.logicMap.get(this.clb.getFirstLogic());

			//DBにロジック情報を登録
			dbi.logicInsert(lib);

			//2番目に受信したロジック情報のBeanを取得
			lib=this.logicMap.get(this.clb.getSecondLogic());

			//DBにロジック情報を登録
			dbi.logicInsert(lib);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * 指し手情報、試合開始・終了時間、指し手の考慮時間の取得とイベントエラーチェック
	 * @param ta
	 * @param location
	 */
	private DuringBattleInfoTrade locationAddTimeTurnAndErrJudge(DuringBattleInfoTrade dbit){

		boolean errJudge=true;
		TurnAdmin ta=dbit.getTa();

		if(ta.getTurn()==1){
			dbit.setLocation(dbit.getLca().createLocation());
		}

		//手番通知オブジェクト作成
		JSONObject gameInfo=ta.informTurn(dbit.getLocation());

		//オブジェクト送信処理
		this.samqm.sendMessage(gameInfo, dbit.getTurnLogic());

		//処理開始時間取得
		Date startDate=new Date();
		SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
		String playStartTime=sdfTime.format(startDate);
		dbit.setPlayStartTime(playStartTime);

		//受信メッセージ取得
		JSONObject ReceiveGameInfo=samqm.receiveMessage();
		dbit.setReceiveGameInfo(ReceiveGameInfo);

		//処理終了時刻取得
		Date endDate=new Date();
		String playEndTime=sdfTime.format(endDate);
		dbit.setPlayEndTime(playEndTime);


		//JSONObject内のイベント情報を取得
		String event=ReceiveGameInfo.getString("event");

		//一番最初のターンのみフィールドの変数に開始日付を代入
		if(ta.getTurn()==1){
			SimpleDateFormat sdfDate= new SimpleDateFormat("yyyy/MM/dd");
			dbit.setStartDate(sdfDate.format(startDate));
			dbit.setStartTime(playStartTime);
		}

		if(!event.equals(this.TURN_END)){
			errJudge=false;
		}

		dbit.setErrJudge(errJudge);

		return dbit;

	}

/**
 * クライアントのメッセージが期待したイベント情報であった場合の処理
 *
 */
	private DuringBattleInfoTrade expectEventDuringBattle(DuringBattleInfoTrade dbit){

		//ループを終了させるか否かを格納する変数
		boolean stopRoop=false;

		JudgeMatch jm=dbit.getJm();
		TurnAdmin ta=dbit.getTa();
		LocationAdmin lca=dbit.getLca();

		try{
			//指し手情報Beanを取得
			LocationInfoBean lifb=jm.getLocationInfo();

			//盤面情報を更新・取得
			String[][] location=lca.updateLocation(lifb);
			dbit.setLocation(location);

			//メソッド間、値引渡し用Bean内のjmオブジェクトを更新
			dbit.setLca(lca);

			//DBに指し手情報を登録
			dbi.locationInsert(lifb);

			//勝敗判定結果を取得
			String result=jm.battleJudge(location);
			dbit.setResult(result);

			//試合終了と継戦で条件分岐
			if(result.equals(this.WIN)||result.equals(this.DRAW)){

				//試合終了時刻を取得
				dbit.setEndTime(lifb.getPlayEnd());

				stopRoop=true;


			}else if(result.equals(this.CONTINUE)){
				//次手番のクライアントのIPアドレスを取得
				dbit.setTurnLogic(ta.judgeTurn());
			}

			dbit.setStopRoop(stopRoop);

		}catch(SQLException e){
			e.printStackTrace();
		}

		return dbit;
	}

	private void notExpectEventDuringBattle(DuringBattleInfoTrade dbit){

		JSONObject gameInfoErr=null;

		//アクセス過多の場合
		if(dbit.getReceiveGameInfo().get("event").equals(READY)){

			System.out.println("受信メッセージを受け付けられません");

			//エラー通知オブジェクトの取得
			gameInfoErr=ie.oversubscribedError();

			//メッセージを送信してきたクライアントのIPアドレス取得
			String ErrIPAddress=(dbit.getReceiveGameInfo().getString("address"));

			//通知オブジェクト送信
			samqm.sendMessage(gameInfoErr,ErrIPAddress);

			System.out.println("再度メッセージを受信します");

			//その他イベント情報を取得した場合
		}else{

			System.out.println("受信メッセージを受け付けられません");

			//エラー通知オブジェクトの取得
			gameInfoErr=ie.notExpectEventError();

			//通知オブジェクト送信
			samqm.sendMessage(gameInfoErr,this.logicMap.get(dbit.getTurnLogic()).getAddress());

			System.out.println("再度メッセージを受信します");
		}
	}

	/**
	 * 先攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（通常）
	 * @param turnAdd
	 * @param battleId
	 * @param result
	 * @param addRefIdMap
	 * @return
	 */
	private JSONObject normalFirstResult(int battleId,DuringBattleInfoTrade dbit,
			Map<String,Integer> addRefIdMap){

		JSONObject firstPlayerGameInfo=null;
		JudgeMatch jm=dbit.getJm();

		try{
			//先攻が勝利した場合
			if(dbit.getTurnLogic().equals(this.clb.getFirstLogic())){
				//先攻の試合結果を登録
				dbi.resultInsert(battleId, dbit.getStartTime(),dbit.getEndTime(),
						dbit.getResult(),addRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=jm.informResult(dbit.getResult());

				//引き分けの場合
			}else if(dbit.getResult().equals(this.DRAW)){

				//先攻の試合結果を登録
				dbi.resultInsert(battleId,dbit.getStartTime(),dbit.getEndTime(),
						dbit.getResult(),addRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=jm.informResult(dbit.getResult());

				//敗北の場合
			}else if(!dbit.getTurnLogic().equals(this.clb.getFirstLogic())){

				//先攻の試合結果を登録
				dbi.resultInsert(battleId,dbit.getStartTime(),dbit.getEndTime(),
						this.LOSE,addRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=jm.informResult(this.LOSE);
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		return firstPlayerGameInfo;
	}

	/**
	 *後攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（通常）
	 * @return
	 */
	private JSONObject normalSecondResult(int battleId,DuringBattleInfoTrade dbit,
			Map<String,Integer> addRefIdMap){

		JSONObject secondPlayerGameInfo=null;
		JudgeMatch jm=dbit.getJm();

		try{
			//後攻が勝利した場合
			if(dbit.getTurnLogic().equals(this.clb.getSecondLogic())){
				//後攻の試合結果を登録
				dbi.resultInsert(battleId,dbit.getStartTime(), dbit.getEndTime(),
						dbit.getResult(),addRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=jm.informResult(dbit.getResult());

				//引き分けの場合
			}else if(dbit.getResult().equals(this.DRAW)){

				//後攻の試合結果を登録
				dbi.resultInsert(battleId, dbit.getStartTime(), dbit.getEndTime(),
						dbit.getResult(),addRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=jm.informResult(dbit.getResult());

				//敗北の場合
			}else if(!dbit.getTurnLogic().equals(this.clb.getSecondLogic())){

				//後攻の試合結果を登録
				dbi.resultInsert(battleId, dbit.getStartTime(), dbit.getEndTime(),
						this.LOSE,addRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=jm.informResult(this.LOSE);
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		return secondPlayerGameInfo;
	}

	/**
	 * 先攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（反則）
	 * @return
	 */
	private JSONObject abnormalFirstResult(int battleId,Map<String,Integer> addRefIdMap,
			DuringBattleInfoTrade dbit){

		JSONObject firstPlayerGameInfo=null;
		JudgeMatch jm=dbit.getJm();

		try{
			//先攻反則負けした場合
			if(dbit.getTurnLogic().equals(this.clb.getFirstLogic())){
				//先攻の試合結果を登録
					dbi.resultInsert(battleId, dbit.getStartTime(),dbit.getEndTime(),
							this.LOSE,addRefIdMap.get(this.clb.getFirstLogic()),
							dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=this.ie.ruleError();

				//相手による反則勝ちの場合
			}else{
				//先攻の試合結果を登録
				dbi.resultInsert(battleId, dbit.getStartTime(),dbit.getEndTime(),
						this.WIN,addRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=jm.informResult(this.WIN);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return firstPlayerGameInfo;
	}

/**
 * 後攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（反則）
 * @param battleId
 * @param addRefIdMap
 * @param dbit
 * @return
 */
	private JSONObject abnormalSecondResult(int battleId,Map<String,Integer> addRefIdMap,
			DuringBattleInfoTrade dbit){

		JSONObject secondPlayerGameInfo=null;
		JudgeMatch jm=dbit.getJm();

		try{
			//後攻反則負けした場合
			if(dbit.getTurnLogic().equals(this.clb.getSecondLogic())){
				//後攻の試合結果を登録
				dbi.resultInsert(battleId,dbit.getStartTime(),dbit.getEndTime(),
						this.LOSE,addRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=this.ie.ruleError();

				//相手による反則勝ちの場合
			}else{
				//後攻の試合結果を登録
				dbi.resultInsert(battleId,dbit.getStartTime(),dbit.getEndTime(),
						this.WIN,addRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=jm.informResult(this.WIN);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		return secondPlayerGameInfo;
	}





	/**
	 * 試合中・終了を管理するメソッド
	 * @throws Exception
	 */
	private void gameLater(Map<String,Integer> logicRefIdMap,DuringBattleInfoTrade dbit,
			JudgeMatch jm,int battleId) throws Exception{
		try{
			//試合終了までループを繰り返す
			while(true){

				//指し手情報、試合開始・終了時間、指し手の考慮時間の取得と指し手情報のルール判定
				dbit=locationAddTimeTurnAndErrJudge(dbit);

				//取得するメッセージで問題がない場合
				if(dbit.isErrJudge()){

					//指し手情報のルール判定を行う
					boolean ruleJudge=jm.ruleJudge(dbit.getTa().getTurn(), dbit.getLocation(),
							dbit.getReceiveGameInfo(), battleId,dbit.getPlayStartTime(),
							dbit.getPlayEndTime(),logicRefIdMap.get(dbit.getTurnLogic()));

					//メソッド間、値引渡し用Bean内のjmオブジェクトを更新
					dbit.setJm(jm);

					if(ruleJudge){

						//指し手情報登録と継戦の判定をする
						dbit=expectEventDuringBattle(dbit);

						//試合が終了条件に当てはまった場合、ループから抜け出す
						if(dbit.isStopRoop()){
							break;
						}
					}
				}else{
					//期待するイベント情報を得られなかったときの処理
					notExpectEventDuringBattle(dbit);

					continue;
				}
			}

			//試合結果DB登録作業・勝敗オブジェクト作成
			JSONObject firstPlayerGameInfo=null;
			JSONObject secondPlayerGameInfo=null;


			//正常な試合終了か、反則による試合終了によるものか分岐
			//正常な試合終了
			if(!dbit.getResult().equals(this.CONTINUE)){

				//先攻クライアントの処理
				firstPlayerGameInfo=normalFirstResult(battleId, dbit, logicRefIdMap);
				//後攻クライアントの処理
				secondPlayerGameInfo=normalSecondResult(battleId, dbit, logicRefIdMap);

				//反則による試合終了
			}else{
				//先攻クライアントの処理
				firstPlayerGameInfo=abnormalFirstResult(battleId, logicRefIdMap, dbit);
				//後攻クライアントの処理
				secondPlayerGameInfo=abnormalSecondResult(battleId, logicRefIdMap, dbit);
			}

			//勝敗通知オブジェクト送信
			samqm.sendMessage(firstPlayerGameInfo, this.logicMap.get(clb.getFirstLogic()).getAddress());
			samqm.sendMessage(secondPlayerGameInfo, this.logicMap.get(clb.getSecondLogic()).getAddress());

			//同名ロジックでのアクセス時の例外処理
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 試合開始前を管理するメソッド
	 * @throws Exception
	 */
	public void gameStart() throws Exception{
		try{
			//MQとDBの接続処理
			mqDbAccess();

			//ロジック管理クラスのインスタンス化
			LogicAdmin la=new LogicAdmin();

			//ロジック情報を2つ受け取るまで、ループ
			while(true){

				//ロジック情報をMapに格納
				logicReceive(la);

				//2つ受け取ったらbreak
				if(la.fillClient()){
					break;
				}
			}

			//ロジック情報をDBに登録
			enrollLogic(la);

			//ロジック情報（IPアドレス）とロジックIDを紐付け・取得
			Map<String,Integer> logicRefIdMap=la.attachId(dbi);

			//同名ロジック判定
			boolean sameJudge=la.sameJudge();

			if(sameJudge==false){
				throw new SameLogicException();
			}

			//メソッド間、値引渡し用Bean
			DuringBattleInfoTrade dbit=new DuringBattleInfoTrade();

			//試合ID取得
			int battleId=BattleIdAdmin.getBattleID(dbi);

			//手番管理クラスのインスタンス化
			dbit.setTa(new TurnAdmin(this.clb));

			//先攻・後攻決定処理
			dbit.setTurnLogic(dbit.getTa().decideFirst());

			//盤面保持・更新クラスのインスタンス化
			dbit.setLca(new LocationAdmin());

			//試合判定クラスをインスタンス化
			JudgeMatch jm=new JudgeMatch();

			//試合判定結果格納用変数の用意
			dbit.setResult(this.CONTINUE);

			//試合中以降の処理に移行
			gameLater(logicRefIdMap, dbit, jm, battleId);

		}catch(SQLException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(SameLogicException e){

			//エラー通知オブジェクトの取得
			JSONObject gameInfo=this.ie.sameLogicError();

			//オブジェクト送信
			samqm.sendMessage(gameInfo,this.logicMap.get(this.clb.getFirstLogic()).getAddress());

			samqm.sendMessage(gameInfo,this.logicMap.get(this.clb.getSecondLogic()).getAddress());

			throw e;

		}finally{
			//Queueの終了
			if(samqm!=null){
				samqm.quitQueue();
			}

			//DB接続の切断
			if(dbi!=null){
				dbi.disconnect();
			}
		}
	}

}
