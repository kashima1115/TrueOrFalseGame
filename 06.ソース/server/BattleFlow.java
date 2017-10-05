package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import messageQueue.ServerActiveMQMessaging;
import net.sf.json.JSONObject;

/**
 * 試合全体の管理を行うクラス
 * @author kanayama
 *
 */
class BattleFlow {
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
	private DbOperation dbi;


	/**
	 * コンストラクタ
	 */
	BattleFlow(){
		READY="ready";
		TURN_END="TurnEnd";
		logicMap=new HashMap<String,LogicInfoBean>();
		clb=new ClientLogicBean();
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
	 * @throws JMSException
	 */
	private void mqDbAccess() throws SQLException, ClassNotFoundException, UnknownHostException, JMSException{
		//MQクラスをインスタンス化
		this.samqm=new ServerActiveMQMessaging();

		//DBアクセスクラスをインスタンス化
		this.dbi=new DbOperation();

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
	 * @throws JMSException
	 */
	private void stayReady() throws JMSException{
		//ロジック情報を2つ受け取るまで、ループ
		int readyConut=0;
		while(readyConut<2){

			//メッセージ受信メソッドの呼び出し
			JSONObject logicInfo=this.samqm.receiveMessage();

			//送信されたメッセージのイベント情報をチェック
			boolean eventJudge=logicEventCheck(logicInfo);

			//ロジック情報がMapに格納できた場合と、できなかった場合で条件分岐
			if(eventJudge){
				//受信したロジック情報をMapに格納
				logicPutMap(readyConut, logicInfo);
				readyConut++;
			}else{
				System.out.println("受信メッセージを受け付けられません");
				System.out.println("ロジック情報を送信してください");
				System.out.println("再度メッセージを受信します");
			}
		}
	}

	/**
	 * 送信されたロジック情報のイベント情報チェック
	 * @param logicInfo 受信したロジック情報
	 * @return logicInfo内のイベント情報が期待値ならtrueを返す
	 */
	private boolean logicEventCheck(JSONObject logicInfo){

		//イベント情報格納用変数
		String event="";

		//受信したイベント情報が正しい構成か否かで条件分岐
		//正しい構成の場合
		if(CheckJsonKey.checkLogicStructure(logicInfo)){
			//イベント情報取得
			event=logicInfo.getString("event");

		//不正な構成の場合
		}else{
			return false;
		}


		//受信メッセージ振り分け
		//期待していたメッセージを受信できた場合
		if(this.READY.equals(event)){
			return true;

		//期待していたメッセージ受信できなかった場合
		}else{
			return false;
		}

	}

	/**
	 * ロジック情報を保持
	 * @param readyConut イベント情報が「ready」であるlogicInfoを取得した数
	 * @param logicInfo 受信したロジック情報
	 */
	private void logicPutMap(int readyCount,JSONObject logicInfo){
		//ロジック情報Beanを取得
		LogicInfoBean lib=new LogicInfoBean();

		//JSONObject内の値を取得
		String logicName=logicInfo.getString("logicName");
		String logicWriter=logicInfo.getString("logicWriter");
		String logicVersion=logicInfo.getString("logicVersion");
		String address=logicInfo.getString("address");

		//Beanにロジック情報の値を格納
		lib.setLogicName(logicName);
		lib.setWriter(logicWriter);
		lib.setVersion(logicVersion);
		lib.setAddress(address);

		//Mapのキー名を作成
		String logicKey=logicName+logicWriter+logicVersion+address;

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
			this.dbi.insertLogic(this.logicMap.get(this.clb.getFirstLogic()));

			//2番目に受信したロジック情報をDBにロジック情報を登録
			this.dbi.insertLogic(this.logicMap.get(this.clb.getSecondLogic()));

		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 *試合中に使用するオブジェクトを用意
	 * @return dbit 試合中・試合終了後に使用するオブジェクト
	 */
	private DuringBattleInfoTradeBean dbitSetObject(){
		//メソッド間、値引渡し用Bean
		DuringBattleInfoTradeBean dbit=new DuringBattleInfoTradeBean();

		//手番管理クラスのインスタンス化
		dbit.setTa(new TurnAdmin(this.clb));

		//先攻・後攻決定処理
		dbit.setTurnLogic(dbit.getTa().decideFirst());

		//盤面保持・更新クラスのインスタンス化
		dbit.setLca(new LocationAdmin());

		//試合判定結果格納用変数の用意
		dbit.setResult(this.CONTINUE);

		//手番通知二重送信防止フラグ
		dbit.setPreventDoubleTransmissionFlag(true);

		return dbit;
	}

	/**
	 * クライアントに手番通知オブジェクトを送信
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @throws JMSException
	 */
	private void sendPlayStart(DuringBattleInfoTradeBean dbit) throws JMSException{

		LocationAdmin lca=dbit.getLca();

		//手番通知オブジェクト作成
		JSONObject gameInfo=TurnAdmin.informTurn(lca.getLocation());

		//オブジェクト送信処理
		this.samqm.sendMessage(gameInfo, this.logicMap.get(dbit.getTurnLogic()).getAddress());

	}


	/**
	 * 処理開始時刻を取得する
	 *  @param dbit 試合中・試合終了後に使用するオブジェクト
	 *  @return 試合中・試合終了後に使用するオブジェクト
	 */
	private DuringBattleInfoTradeBean setTimeStart(DuringBattleInfoTradeBean dbit){

		TurnAdmin ta=dbit.getTa();

		//処理開始時間取得
		Date startDate=new Date();
		SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
		String playStartTime=sdfTime.format(startDate);
		dbit.setPlayStartTime(playStartTime);

		//一番最初のターンのみ試合開始時刻をセット
		if(ta.getTurn()==1){
			dbit.setBattleStartTime(playStartTime);
		}

		return dbit;
	}
	/**
	 * 処理終了時刻を取得
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @return 試合中・試合終了後に使用するオブジェクト
	 */
	private DuringBattleInfoTradeBean setTimeEnd(DuringBattleInfoTradeBean dbit){
		//処理終了時刻取得
		Date endDate=new Date();
		SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
		String playEndTime=sdfTime.format(endDate);
		dbit.setPlayEndTime(playEndTime);

		return dbit;
	}
	/**
	 * 試合開始日付を取得
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @return 試合開始日付を返す
	 */
	private String setDate(){
		//試合開始日付取得
		Date startDate=new Date();
		SimpleDateFormat sdfDate= new SimpleDateFormat("yyyy/MM/dd");

		return sdfDate.format(startDate);
	}

	/**
	 * 指し手情報をセット
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return 指し手情報Beanを返す
	 */
	private LocationInfoBean setLocationInfo(DuringBattleInfoTradeBean dbit,Map<String,Integer> logicRefIdMap,
			int battleId,JSONObject receiveGameInfo){
		LocationInfoBean lifb=new LocationInfoBean();

		//指し手情報を取得
		int locationX=receiveGameInfo.getInt("xAxis");
		int locationY=receiveGameInfo.getInt("yAxis");

		lifb.setBattleId(battleId);
		lifb.setLocationX(locationX);
		lifb.setLocationY(locationY);
		lifb.setLogicId(logicRefIdMap.get(dbit.getTurnLogic()));
		lifb.setPlayEnd(dbit.getPlayEndTime());
		lifb.setPlayStart(dbit.getPlayStartTime());
		lifb.setTurn(dbit.getTa().getTurn());

		return lifb;
	}

/**
 * 試合の継戦・終了判断
 * @param dbit 試合中・試合終了後に使用するオブジェクト
 * @return 試合中・試合終了後に使用するオブジェクト
 */
	private DuringBattleInfoTradeBean checkMatchEnd(DuringBattleInfoTradeBean dbit){

		//ループを終了させるか否かを格納する変数
		boolean matchEnd=false;

		LocationAdmin lca=dbit.getLca();

		//勝敗判定結果を取得
		String result=JudgeMatch.battleJudge(lca.getLocation());
		dbit.setResult(result);

		//試合終了と継戦で条件分岐
		if(result.equals(this.WIN)||result.equals(this.DRAW)){
			matchEnd=true;
		}else if(result.equals(this.CONTINUE)){
			matchEnd=false;
		}

		dbit.setMatchEnd(matchEnd);

		return dbit;
	}

	/**
	 * 正しいイベント情報取得・ルール無違反のときの処理
	 * @param dbit
	 * @param logicRefIdMap
	 * @param battleId
	 * @param receiveGameInfo
	 * @return
	 */
	private DuringBattleInfoTradeBean insertLocationUnderRule(DuringBattleInfoTradeBean dbit,
			Map<String,Integer> logicRefIdMap,int battleId,JSONObject receiveGameInfo){

		//指し手情報をBeanにセット
		LocationInfoBean lifb=setLocationInfo(dbit, logicRefIdMap, battleId,receiveGameInfo);

		//指し手情報登録
		dbit=insertLocation(dbit, lifb);

		return dbit;
	}

	private DuringBattleInfoTradeBean checkContinueOrbreak(DuringBattleInfoTradeBean dbit,
			String event,JSONObject receiveGameInfo,boolean ruleJudge) throws ClientMulfunctionException, JMSException{

		//継戦・試合終了の判定をする
		dbit=checkMatchEnd(dbit);

		//不正なイベント情報を取得した場合
		if(!this.TURN_END.equals(event)){
			//期待するイベント情報を得られなかったときの処理
			notExpectEventDuringBattle(dbit,receiveGameInfo);

			//continueと同義
			dbit.setStopLoop(false);

		//通常の試合が終了条件に当てはまった場合
		}else if(ruleJudge==false || (ruleJudge==true && dbit.isMatchEnd()==true)){
			//試合終了時刻を取得
			dbit.setBattleEndTime(dbit.getPlayEndTime());

			//breakと同義
			dbit.setStopLoop(true);

		//試合の終了条件に当てはまらない場合の処理
		}else{
			//次手番のクライアントのMapキーを取得
			dbit.setTurnLogic(dbit.getTa().judgeTurn());

			//continueと同義
			dbit.setStopLoop(false);
		}
		return dbit;
	}

	/**
	 * 指し手情報登録
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @return 試合中・試合終了後に使用するオブジェクト
	 */
	private DuringBattleInfoTradeBean insertLocation(DuringBattleInfoTradeBean dbit,LocationInfoBean lifb){

		LocationAdmin lca=dbit.getLca();

		//DBに指し手情報を登録
		try {

			//指し手情報を元に盤面情報を更新
			lca.updateLocation(lifb);

			//メソッド間、値引渡し用Bean内のlcaオブジェクトを更新
			dbit.setLca(lca);

			//指し手情報登録
			dbi.insertLocation(lifb);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbit;
	}

	/**
	 * 受信したイベント情報が期待値ではなかったときの処理
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @throws ClientMulfunctionException
	 * @throws JMSException
	 */
	private DuringBattleInfoTradeBean notExpectEventDuringBattle(DuringBattleInfoTradeBean dbit,
			JSONObject receiveGameInfo) throws ClientMulfunctionException, JMSException {

		JSONObject gameInfoErr=null;

		//アクセス過多の場合
		if(receiveGameInfo.get("event").equals(READY)){

			System.out.println("受信メッセージを受け付けられません");

			//エラー通知オブジェクトの取得
			gameInfoErr=InformError.oversubscribedError();

			//メッセージを送信してきたクライアントのIPアドレス取得
			String errIPAddress=(receiveGameInfo.getString("address"));

			//通知オブジェクト送信
			samqm.sendMessage(gameInfoErr,errIPAddress);

			//手番通知誤送信防止フラグ
			dbit.setPreventDoubleTransmissionFlag(false);

			System.out.println("アクセス過多のため受け付けられません");
			System.out.println("再度メッセージを受信します");

			//その他イベント情報を取得した場合
		}else{

			throw new ClientMulfunctionException();

		}

		return dbit;
	}

	/**
	 * 先攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（通常）
	 * @param battleId 試合ID
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return 先攻クライアントへの勝敗通知オブジェクト
	 */
	private JSONObject normalFirstResult(int battleId,DuringBattleInfoTradeBean dbit,
			Map<String,Integer> logicRefIdMap){

		JSONObject firstPlayerGameInfo=null;

		try{
			//先攻が勝利した場合
			if(dbit.getTurnLogic().equals(this.clb.getFirstLogic())){
				//先攻の試合結果を登録
				dbi.insertResult(battleId, dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						dbit.getResult(),logicRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=JudgeMatch.informResult(dbit.getResult());

				//引き分けの場合
			}else if(dbit.getResult().equals(this.DRAW)){

				//先攻の試合結果を登録
				dbi.insertResult(battleId,dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						dbit.getResult(),logicRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=JudgeMatch.informResult(dbit.getResult());

				//敗北の場合
			}else if(!dbit.getTurnLogic().equals(this.clb.getFirstLogic())){

				//先攻の試合結果を登録
				dbi.insertResult(battleId,dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						this.LOSE,logicRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=JudgeMatch.informResult(this.LOSE);
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		return firstPlayerGameInfo;
	}

	/**
	 *後攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（通常）
	 * @param battleId 試合ID
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return 後攻クライアントへの勝敗通知オブジェクト
	 */
	private JSONObject normalSecondResult(int battleId,DuringBattleInfoTradeBean dbit,
			Map<String,Integer> logicRefIdMap){

		JSONObject secondPlayerGameInfo=null;

		try{
			//後攻が勝利した場合
			if(dbit.getTurnLogic().equals(this.clb.getSecondLogic())){
				//後攻の試合結果を登録
				dbi.insertResult(battleId,dbit.getBattleStartTime(), dbit.getBattleEndTime(),
						dbit.getResult(),logicRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=JudgeMatch.informResult(dbit.getResult());

				//引き分けの場合
			}else if(dbit.getResult().equals(this.DRAW)){

				//後攻の試合結果を登録
				dbi.insertResult(battleId, dbit.getBattleStartTime(), dbit.getBattleEndTime(),
						dbit.getResult(),logicRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=JudgeMatch.informResult(dbit.getResult());

				//敗北の場合
			}else if(!dbit.getTurnLogic().equals(this.clb.getSecondLogic())){

				//後攻の試合結果を登録
				dbi.insertResult(battleId, dbit.getBattleStartTime(), dbit.getBattleEndTime(),
						this.LOSE,logicRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=JudgeMatch.informResult(this.LOSE);
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		return secondPlayerGameInfo;
	}

	/**
	 * 先攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（反則）
	 * @param battleId 試合ID
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return 先攻クライアントの勝敗通知オブジェクト
	 */
	private JSONObject abnormalFirstResult(int battleId,Map<String,Integer> logicRefIdMap,
			DuringBattleInfoTradeBean dbit){

		JSONObject firstPlayerGameInfo=null;

		try{
			//先攻反則負けした場合
			if(dbit.getTurnLogic().equals(this.clb.getFirstLogic())){

			//先攻の試合結果を登録
			dbi.insertResult(battleId, dbit.getBattleStartTime(),dbit.getBattleEndTime(),
							this.LOSE,logicRefIdMap.get(this.clb.getFirstLogic()),
							dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=InformError.ruleError();

				//相手による反則勝ちの場合
			}else{
				//先攻の試合結果を登録
				dbi.insertResult(battleId, dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						this.WIN,logicRefIdMap.get(this.clb.getFirstLogic()),
						dbit.getStartDate(),FIRST);

				//先攻クライアントに渡す勝敗通知オブジェクトを作成
				firstPlayerGameInfo=JudgeMatch.informResult(this.WIN);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return firstPlayerGameInfo;
	}

/**
 * 後攻クライアントの試合結果登録と、勝敗通知オブジェクト作成（反則）
 * @param battleId 試合ID
 * @param dbit 試合中・試合終了後に使用するオブジェクト
 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
 * @return 後攻クライアントの勝敗通知オブジェクト
 */
	private JSONObject abnormalSecondResult(int battleId,Map<String,Integer> logicRefIdMap,
			DuringBattleInfoTradeBean dbit){

		JSONObject secondPlayerGameInfo=null;

		try{
			//後攻反則負けした場合
			if(dbit.getTurnLogic().equals(this.clb.getSecondLogic())){
				//後攻の試合結果を登録
				dbi.insertResult(battleId,dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						this.LOSE,logicRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=InformError.ruleError();

				//相手による反則勝ちの場合
			}else{
				//後攻の試合結果を登録
				dbi.insertResult(battleId,dbit.getBattleStartTime(),dbit.getBattleEndTime(),
						this.WIN,logicRefIdMap.get(this.clb.getSecondLogic()),
						dbit.getStartDate(),SECOND);

				//後攻クライアントに渡す勝敗通知オブジェクトを作成
				secondPlayerGameInfo=JudgeMatch.informResult(this.WIN);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		return secondPlayerGameInfo;
	}





	/**
	 * 試合中を管理するメソッド
	 * @throws Exception
	 * @param battleId 試合ID
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return 試合中・試合終了後に使用するオブジェクト
	 * @throws ClientMulfunctionException
	 * @throws JMSException
	 */
	private DuringBattleInfoTradeBean gameLater(Map<String,Integer> logicRefIdMap,DuringBattleInfoTradeBean dbit,
			int battleId) throws ClientMulfunctionException, JMSException{

		try{

			//試合終了までループを繰り返す
			while(true){
				//ルール判定フラグ
				boolean ruleJudge=false;
				//イベント情報格納用変数
				String event="";

				//クライアントへの手番通知二重送信防止
				if(dbit.isPreventDoubleTransmissionFlag()){

					//クライアントに手番通知
					sendPlayStart(dbit);

					//クライアント処理開始時間を取得
					dbit=setTimeStart(dbit);
				}

				//手番通知二重送信防止フラグ
				dbit.setPreventDoubleTransmissionFlag(true);

				//受信メッセージ取得
				JSONObject receiveGameInfo=samqm.receiveMessage();

				//クライアント処理終了時間取得
				dbit=setTimeEnd(dbit);

				//イベント情報のキー名が存在する場合に条件分岐
				if(CheckJsonKey.checkExitenceOfEvent(receiveGameInfo)){

					//JSONObject内のイベント情報を取得
					event=receiveGameInfo.getString("event");
				}

				//正しいイベント情報取得
				if(this.TURN_END.equals(event)){

					//メッセージの不正なキー名構成の場合に条件分岐
					if(CheckJsonKey.checkLocationSturcture(receiveGameInfo)==false){
						throw new ClientMulfunctionException();
					}

					//指し手情報のルール判定を行う
					ruleJudge=JudgeMatch.ruleJudge(dbit.getLca().getLocation(),receiveGameInfo);

					//ルール違反なし
					if(ruleJudge){
						//指し手情報登録
						insertLocationUnderRule(dbit, logicRefIdMap, battleId, receiveGameInfo);
					}

				}

				//試合継戦・終了判定
				dbit=checkContinueOrbreak(dbit, event, receiveGameInfo, ruleJudge);

				if(dbit.isStopLoop()){
					break;
				}else{
					continue;
				}
			}
		}catch(ClientMulfunctionException e){
			throw e;
		}
		return dbit;
	}

/**
 * 試合終了後の処理を管理するメソッド
 * @param battleId 試合ID
 * @param dbit 試合中・試合終了後に使用するオブジェクト
 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
 * @throws JMSException
 */
	private void gameEnd(int battleId,Map<String,Integer> logicRefIdMap,DuringBattleInfoTradeBean dbit) throws JMSException{
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
	void gameStart() throws Exception{
		try{
			//MQとDBの接続処理
			mqDbAccess();

			//ロジック情報受信管理
			stayReady();

			//ロジック情報をDBに登録
			enrollLogic();

			//ロジック情報（IPアドレス）とロジックIDを紐付け・取得
			Map<String,Integer> logicRefIdMap=LogicUtil.attachId(this.dbi, this.logicMap, this.clb);

			//同名ロジック判定
			LogicUtil.sameJudge(logicRefIdMap, clb);

			//試合ID取得
			int battleId=BattleIdAdmin.getBattleID(dbi);

			//メソッド間、値引渡し用Bean
			DuringBattleInfoTradeBean dbit=dbitSetObject();

			//試合開始日付をBeanにセット
			dbit.setStartDate(setDate());

			//試合中の処理に移行
			dbit=gameLater(logicRefIdMap,dbit,battleId);

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
			JSONObject gameInfo=InformError.sameLogicError();

			//オブジェクト送信
			samqm.sendMessage(gameInfo,this.logicMap.get(this.clb.getFirstLogic()).getAddress());

			samqm.sendMessage(gameInfo,this.logicMap.get(this.clb.getSecondLogic()).getAddress());

			throw e;

		}catch(ClientMulfunctionException e){
			System.out.println("受信メッセージを受け付けられません");

			System.out.println("プログラム終了処理を行います");

			//エラー通知オブジェクトの取得
			JSONObject gameInfoErr=InformError.notExpectEventError();

			//通知オブジェクト送信
			samqm.sendMessage(gameInfoErr,this.logicMap.get(this.clb.getFirstLogic()).getAddress());

			samqm.sendMessage(gameInfoErr,this.logicMap.get(this.clb.getSecondLogic()).getAddress());

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
