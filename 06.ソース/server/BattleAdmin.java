package server;

import java.net.InetAddress;
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
	//「ready」カウント用変数
	private int readyCount;
	//試合開始・終了時間
	private String startTime;
	private String endTime;
	//対戦日付
	private String startDate;
	//受信するイベント情報
	private final String READY;
	private final String TURN_END;
	private Map<String,LogicInfoBean> logicMap;
	private ClientAddressBean cab;

	//ゲーム勝敗関係
	private final String WIN;
	private final String LOSE;
	private final String CONTINUE;
	private final String DRAW;

	/**
	 * コンストラクタ
	 */
	BattleAdmin(){
		readyCount=0;
		startTime=null;
		endTime=null;
		startDate=null;
		READY="ready";
		TURN_END="TurnEnd";
		logicMap=null;
		cab=null;
		WIN="win";
		LOSE="lose";
		CONTINUE="continue";
		DRAW="draw";
	}
	/**
	 * 試合全体を管理するメソッド
	 * @throws Exception
	 */
	public void gameStart() throws Exception{
		//メッセージングクラスのオブジェクトを格納
		ServerActiveMQMessaging samqm=null;

		//情報登録クラスのオブジェクトを格納
		DbInsert dbi=null;


		try{
			//DBアクセスクラスをインスタンス化
			dbi=new DbInsert();

			//コネクションを生成
			dbi.connect();

			//メッセージングクラスをインスタンス化
			 samqm=new ServerActiveMQMessaging();

			//起動しているマシンのIPアドレスを取得
			InetAddress ia=InetAddress.getLocalHost();

			String IPAddress = ia.getHostAddress();

			//Queue作成メソッドの呼び出し
			samqm.createQueue(IPAddress);

			//ロジック管理クラスのインスタンス化
			LogicAdmin la=new LogicAdmin();

			//2つのクライアントが揃うまで受信処理を繰り返す
			while(this.readyCount<2){

				//メッセージ受信メソッドの呼び出し
				JSONObject gameInfo=samqm.receiveMessage();

				//イベント情報取得
				String event=gameInfo.getString("event");

				//受信メッセージ振り分け
				if(event.equals(READY)){

					la.logicSet(gameInfo);

					this.readyCount++;

				}else if(!event.equals(READY)){

				}
			}

			//ロジック情報を格納したMapを取得
			this.logicMap=la.getLogicMap();

			//クライアントアドレスを格納したBeanを取得
			this.cab=la.getClientAddressBean();

			//最初に受信したロジック情報のBeanを取得
			LogicInfoBean lib=this.logicMap.get(this.cab.getFirstAddress());

			//DBにロジック情報を登録
			dbi.logicInsert(lib);

			//2番目に受信したロジック情報のBeanを取得
			lib=this.logicMap.get(this.cab.getSecondAddress());

			//DBにロジック情報を登録
			dbi.logicInsert(lib);

			//ロジック情報（IPアドレス）とロジックIDを紐付け・取得
			Map<String,Integer> addRefIdMap=la.attachId(dbi);

			//同名ロジック判定
			boolean sameJudge=la.sameJudge();

			if(sameJudge==false){
				throw new SameLogicException();
			}

			//試合ID取得
			int battleId=BattleIdAdmin.getBattleID(dbi);

			//手番管理クラスのインスタンス化
			TurnAdmin ta=new TurnAdmin(this.cab);

			//先攻・後攻決定処理
			String turnAdd=ta.decideFirst();

			//盤面保持・更新クラスのインスタンス化
			LocationAdmin lca=new LocationAdmin();

			//盤面情報作成取得
			String location[][]=lca.createLocation();

			//試合判定クラスをインスタンス化
			JudgeMatch jm=new JudgeMatch();

			//試合判定結果格納用変数の用意
			String result=this.CONTINUE;

			//以下試合中の処理
			while(true){
				//手番通知オブジェクト作成
				JSONObject gameInfo=ta.informTurn(location);

				//オブジェクト送信処理
				samqm.sendMessage(gameInfo, turnAdd);

				//処理開始時間取得
				Date startDate=new Date();
				SimpleDateFormat sdfTime= new SimpleDateFormat("HH:mm:ss");
				String playStartTime=sdfTime.format(startDate);

				//受信メッセージ取得
				gameInfo=samqm.receiveMessage();

				//処理終了時刻取得
				Date endDate=new Date();
				String playEndTime=sdfTime.format(endDate);


				//JSONObject内のイベント情報を取得
				String event=gameInfo.getString("event");

				//ターン数取得
				int turn=ta.getTurn();

				//フィールドの変数に開始日付を代入
				if(turn==1){
					SimpleDateFormat sdfDate= new SimpleDateFormat("yyyy/MM/dd");
					this.startDate=sdfDate.format(startDate);
					this.startTime=playEndTime;
				}

				//指し手情報を受け取ったかで分岐
				if(event.equals(this.TURN_END)){
					//指し手情報のルール判定を行う
					boolean judge=jm.ruleJudge(turn, location, gameInfo, battleId, playStartTime, playEndTime,
							addRefIdMap.get(turnAdd));

					//ルール判定結果を元に分岐
					if(judge==true){

						//指し手情報Beanを取得
						LocationInfoBean lifb=jm.getLocationInfo();

						//盤面情報を更新・取得
						location=lca.updateLocation(lifb);

						//DBに指し手情報を登録
						dbi.locationInsert(lifb);

						//勝敗判定結果を取得
						result=jm.battleJudge(location);

						//試合終了と継戦で条件分岐
						if(result.equals(this.WIN)||result.equals(this.DRAW)){

							this.endTime=playEndTime;

							break;
						}else if(result.equals(this.CONTINUE)){
							//次手番のクライアントのIPアドレスを取得
							turnAdd=ta.judgeTurn();
						}
					}else if(judge==false){

						this.endTime=playEndTime;

						break;

					}
				}else if(!event.equals(this.TURN_END)){

					//アクセス過多の場合
					if(event.equals(READY)&&this.readyCount>=2){

						//エラー通知クラスのインスタンス化
						InformError ie=new InformError();

						//エラー通知オブジェクトの取得
						JSONObject gameInfoErr=ie.oversubscribedError();

						//メッセージを送信してきたクライアントのIPアドレス取得
						String ErrIPAddress=gameInfo.getString("address");

						//通知オブジェクト送信
						samqm.sendMessage(gameInfoErr,ErrIPAddress);
					}else{

					}
				}
			}

			//先攻クライアントに勝敗通知するオブジェクトを格納する変数
			JSONObject firstPlayerGameInfo=null;

			//後攻クライアントに勝敗通知するオブジェクトを格納する変数
			JSONObject secondPlayerGameInfo=null;

			//試合結果DB登録作業
			//正常な試合終了か、反則による試合終了によるものか分岐
			//正常な試合終了
			if(!result.equals(this.CONTINUE)){

				//先攻が勝利した場合
				if(turnAdd.equals(this.cab.getFirstAddress())){

					//先攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,
							result,addRefIdMap.get(this.cab.getFirstAddress()),this.startDate);

					//先攻クライアントに渡す勝敗通知オブジェクトを作成
					firstPlayerGameInfo=jm.informResult(result);

					//後攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,this.LOSE,
							addRefIdMap.get(this.cab.getSecondAddress()), this.startDate);

					//後攻クライアントに渡す勝敗通知オブジェクトを作成
					secondPlayerGameInfo=jm.informResult(this.LOSE);

					//後攻が勝利した場合
				}else if(turnAdd.equals(this.cab.getSecondAddress())){

					//先攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,
							this.LOSE,addRefIdMap.get(this.cab.getFirstAddress()),this.startDate);

					//先攻クライアントに渡す勝敗通知オブジェクトを作成
					firstPlayerGameInfo=jm.informResult(this.LOSE);

					//後攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,result,
							addRefIdMap.get(this.cab.getSecondAddress()), this.startDate);

					//後攻クライアントに渡す勝敗通知オブジェクトを作成
					secondPlayerGameInfo=jm.informResult(result);

					//引き分けの場合
				}else if(result.equals(this.DRAW)){

					//先攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,
							result,addRefIdMap.get(this.cab.getFirstAddress()),this.startDate);

					//先攻クライアントに渡す勝敗通知オブジェクトを作成
					firstPlayerGameInfo=jm.informResult(result);

					//後攻の試合結果を登録
					dbi.resultInsert(battleId, this.startTime, this.endTime,result,
							addRefIdMap.get(this.cab.getSecondAddress()), this.startDate);

					//後攻クライアントに渡す勝敗通知オブジェクトを作成
					secondPlayerGameInfo=jm.informResult(result);
				}

				//異常な試合終了
			}else{

				//先攻反則負けした場合
				if(turnAdd.equals(this.cab.getFirstAddress())){

				}else if(turnAdd.equals(this.cab.getSecondAddress())){

				}
			}

			//各クライアントに勝敗通知オブジェクトを送信
			samqm.sendMessage(firstPlayerGameInfo,this.cab.getFirstAddress());

			samqm.sendMessage(secondPlayerGameInfo, this.cab.getSecondAddress());

			//同名ロジックでのアクセス時の例外処理
		}catch(SameLogicException e){

			//エラー通知オブジェクト作成クラスをインスタンス化
			InformError ie=new InformError();

			//エラー通知オブジェクトの取得
			JSONObject gameInfo=ie.sameLogicError();

			//オブジェクト送信
			samqm.sendMessage(gameInfo,this.cab.getFirstAddress());

			samqm.sendMessage(gameInfo, this.cab.getSecondAddress());

			throw e;

		}catch(Exception e){
			e.printStackTrace();
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
