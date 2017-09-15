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
	 * ロジック情報受信管理
	 * @param la
	 */
	private void stayReady(LogicAdmin la){
		//ロジック情報を2つ受け取るまで、ループ
		int readyConut=0;
		while(readyConut<2){

			//メッセージ受信メソッドの呼び出し
			JSONObject logicInfo=this.samqm.receiveMessage();

			//送信されたメッセージのイベント情報をチェック
			boolean eventJudge=logicEventCheck(la,logicInfo);

			//ロジック情報がMapに格納できた場合と、できなかった場合で条件分岐
			if(eventJudge){
				//受信したロジック情報をMapに格納
				logicPutMap(readyConut, logicInfo);
				readyConut++;
			}else{
				System.out.println("受信メッセージを受け付けられません");
				System.out.println("再度メッセージを受信します");
			}
		}
	}

	/**
	 * 送信されたロジック情報のイベント情報チェック
	 */
	private boolean logicEventCheck(LogicAdmin la,JSONObject logicInfo){

		boolean eventJudge=true;

		//イベント情報取得
		String event=logicInfo.getString("event");

		//受信メッセージ振り分け
		//期待していたメッセージを受信できた場合
		if(this.READY.equals(event)){
			eventJudge=true;

		//期待していたメッセージ受信できなかった場合
		}else if(!this.READY.equals(event)){
			eventJudge=false;
		}

		return eventJudge;
	}

	/**
	 * ロジック情報を保持
	 * @param readyConut
	 */
	private void logicPutMap(int readyCount,JSONObject logicInfo){
		//ロジック情報Beanを取得
		LogicInfoBean lib=new LogicInfoBean();

		//JSONObject内の値を取得
		String logicName=logicInfo.getString("logicName");
		String creator=logicInfo.getString("creator");
		String version=logicInfo.getString("version");
		String address=logicInfo.getString("address");

		//Beanにロジック情報の値を格納
		lib.setLogicName(logicName);
		lib.setCreator(creator);
		lib.setVersion(version);
		lib.setVersion(version);

		//Mapのキー名を作成
		String logicKey=logicName+creator+version+address;

		//BeanをMapに格納
		this.logicMap.put(logicKey,lib);

		//キー名をbeanで保持
		if(readyCount<=0){
			this.clb.setFirstLogic(logicKey);
		}else{
			this.clb.setSecondLogic(logicKey);
		}
	}

	/**
	 * ロジック情報を登録
	 */
	private void enrollLogic(){
		try{
			///最初に受信したロジック情報をDBにロジック情報を登録
			this.dbi.logicInsert(this.logicMap.get(this.clb.getFirstLogic()));

			//2番目に受信したロジック情報をDBにロジック情報を登録
			this.dbi.logicInsert(this.logicMap.get(this.clb.getSecondLogic()));

		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * 同名ロジック判定を行う
	 * @param la
	 * @param logicRefIdMap
	 * @throws SameLogicException
	 */
	private void sameLogicJudge(LogicAdmin la,Map<String,Integer> logicRefIdMap) throws SameLogicException{
		//同名ロジック判定
		if(la.sameJudge(logicRefIdMap, this.clb)){
			throw new SameLogicException();
		}
	}

	/**
	 *試合中に使用するオブジェクトを用意
	 * @return
	 */
	private DuringBattleInfoTrade dbitSetObject(){
		//メソッド間、値引渡し用Bean
		DuringBattleInfoTrade dbit=new DuringBattleInfoTrade();

		//手番管理クラスのインスタンス化
		dbit.setTa(new TurnAdmin(this.clb));

		//先攻・後攻決定処理
		dbit.setTurnLogic(dbit.getTa().decideFirst());

		//盤面保持・更新クラスのインスタンス化
		dbit.setLca(new LocationAdmin());

		//試合判定クラスをインスタンス化
		dbit.setJm(new JudgeMatch());

		//試合判定結果格納用変数の用意
		dbit.setResult(this.CONTINUE);

		return dbit;
	}

	/**
	 * クライアントに手番通知オブジェクトを送信
	 * @param dbit
	 */
	private void sendPlayStart(DuringBattleInfoTrade dbit){

		TurnAdmin ta=dbit.getTa();
		LocationAdmin lca=dbit.getLca();

		//手番通知オブジェクト作成
		JSONObject gameInfo=ta.informTurn(lca.getLocation());

		//オブジェクト送信処理
		this.samqm.sendMessage(gameInfo, dbit.getTurnLogic());

	}


	/**
	 * イベントエラーチェック
	 * @param dbit
	 * @return
	 */
	private DuringBattleInfoTrade eventErrJudge(DuringBattleInfoTrade dbit){

		boolean errJudge=true;

		//JSONObject内のイベント情報を取得
		String event=dbit.getReceiveGameInfo().getString("event");

		//正しいイベント情報を取得できているか判定
		if(!this.TURN_END.equals(event)){
			errJudge=false;
		}else{
			errJudge=true;
		}

		dbit.setErrJudge(errJudge);

		return dbit;
	}



	/**
	 * 処理開始時刻を取得する
	 * @param dbit
	 */
	private DuringBattleInfoTrade setTimeStart(DuringBattleInfoTrade dbit){

		TurnAdmin ta=dbit.getTa();

		//処理開始時間取得
		Date startDate=new Date();
		SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
		String playStartTime=sdfTime.format(startDate);
		dbit.setPlayStartTime(playStartTime);

		//一番最初のターンのみ試合開始時刻をセット
		if(ta.getTurn()==1){
			dbit.setStartTime(playStartTime);
		}

		return dbit;
	}
	/**
	 * 処理終了時刻を取得
	 * @param dbit
	 * @param sdfTime
	 */
	private DuringBattleInfoTrade setTimeEnd(DuringBattleInfoTrade dbit){
		//処理終了時刻取得
		Date endDate=new Date();
		SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
		String playEndTime=sdfTime.format(endDate);
		dbit.setPlayEndTime(playEndTime);

		return dbit;
	}
	/**
	 * 試合開始日付を取得
	 * @param dbit
	 */
	private String setDate(){
		//試合開始日付取得
		Date startDate=new Date();
		SimpleDateFormat sdfDate= new SimpleDateFormat("yyyy/MM/dd");

		return sdfDate.format(startDate);
	}

/**
 * 試合の継戦・終了判断
 *
 */
	private DuringBattleInfoTrade expectEventDuringBattle(DuringBattleInfoTrade dbit){

		//ループを終了させるか否かを格納する変数
		boolean stopRoop=false;

		JudgeMatch jm=dbit.getJm();
		TurnAdmin ta=dbit.getTa();
		LocationAdmin lca=dbit.getLca();

		//勝敗判定結果を取得
		String result=jm.battleJudge(lca.getLocation());
		dbit.setResult(result);

		//試合終了と継戦で条件分岐
		if(result.equals(this.WIN)||result.equals(this.DRAW)){

			//試合終了時刻を取得
			dbit.setEndTime(jm.getLocationInfo().getPlayEnd());

			stopRoop=true;

		}else if(result.equals(this.CONTINUE)){
			//次手番のクライアントのIPアドレスを取得
			dbit.setTurnLogic(ta.judgeTurn());
		}

		dbit.setStopRoop(stopRoop);

		return dbit;
	}

	/**
	 * 指し手情報登録
	 * @param dbit
	 * @return
	 */
	private DuringBattleInfoTrade locationInsert(DuringBattleInfoTrade dbit){

		JudgeMatch jm=dbit.getJm();
		LocationAdmin lca=dbit.getLca();

		//DBに指し手情報を登録
		try {
			//指し手情報Beanを取得
			LocationInfoBean lifb=jm.getLocationInfo();

			//指し手情報を元に盤面情報を更新
			lca.updateLocation(lifb);

			//メソッド間、値引渡し用Bean内のlcaオブジェクトを更新
			dbit.setLca(lca);

			//指し手情報登録
			dbi.locationInsert(lifb);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbit;
	}

	/**
	 * 受信したイベント情報が期待値ではなかったときの処理
	 * @param dbit
	 */
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
	 * 試合中を管理するメソッド
	 * @throws Exception
	 */
	private DuringBattleInfoTrade gameLater(Map<String,Integer> logicRefIdMap,DuringBattleInfoTrade dbit,
			JudgeMatch jm,int battleId) throws Exception{
		try{
			//試合終了までループを繰り返す
			while(true){
				//クライアントに手番通知
				sendPlayStart(dbit);

				//クライアント処理開始時間を取得
				dbit=setTimeStart(dbit);

				//受信メッセージ取得
				dbit.setReceiveGameInfo(samqm.receiveMessage());

				//クライアント処理終了時間取得
				dbit=setTimeEnd(dbit);

				//正しいイベント情報を送信しているか判定
				dbit=eventErrJudge(dbit);

				//正しいイベント情報の場合
				if(dbit.isErrJudge()){
					//指し手情報のルール判定を行う
					boolean ruleJudge=jm.ruleJudge(dbit.getTa().getTurn(), dbit.getLca().getLocation(),
							dbit.getReceiveGameInfo(), battleId,dbit.getPlayStartTime(),
							dbit.getPlayEndTime(),logicRefIdMap.get(dbit.getTurnLogic()));

					//メソッド間、値引渡し用Bean内のjmオブジェクトを更新
					dbit.setJm(jm);

					if(ruleJudge){
						//指し手情報登録
						dbit=locationInsert(dbit);

						//継戦・試合終了の判定をする
						dbit=expectEventDuringBattle(dbit);

						//試合が終了条件に当てはまった場合、ループから抜け出す
						if(dbit.isStopRoop()){
							break;
						}

					//不正なイベント情報の場合
					}else{
						break;
					}
				}else{
					//期待するイベント情報を得られなかったときの処理
					notExpectEventDuringBattle(dbit);

					continue;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return dbit;
	}

/**
 * 試合終了後の処理を管理するメソッド
 * @param battleId
 * @param logicRefIdMap
 * @param dbit
 */
	private void gameEnd(int battleId,Map<String,Integer> logicRefIdMap,DuringBattleInfoTrade dbit){
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

			//ロジック情報受信管理
			stayReady(la);

			//ロジック情報をDBに登録
			enrollLogic();

			//ロジック情報（IPアドレス）とロジックIDを紐付け・取得
			Map<String,Integer> logicRefIdMap=la.attachId(this.dbi, this.logicMap, this.clb);

			//同名ロジック判定
			sameLogicJudge(la, logicRefIdMap);

			//試合ID取得
			int battleId=BattleIdAdmin.getBattleID(dbi);

			//メソッド間、値引渡し用Bean
			DuringBattleInfoTrade dbit=dbitSetObject();

			//試合開始日付をBeanにセット
			dbit.setStartDate(setDate());

			//試合中の処理に移行
			dbit=gameLater(logicRefIdMap,dbit,dbit.getJm(), battleId);

			//試合終了後の処理に移行
			gameEnd(battleId, logicRefIdMap, dbit);

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
