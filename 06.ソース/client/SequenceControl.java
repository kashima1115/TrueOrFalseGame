package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import brain.BrainBean;
import messageQueue.MessageQueueController;
import messageQueue.MessageQueueControllerFactory;
import net.sf.json.JSONObject;
/**
 * クライアント機能の動作をコントロールするクラスです.
 * @author hatsugai
 * @version 0.1
 */
public class SequenceControl{
	//Log用
	private static Logger logger = Logger.getLogger(SequenceControl.class.getName());

	//フィールド
	private static AccessBrain ab = new AccessBrain();
	private static ConvertJSON cj = new ConvertJSON();
	private static MessageQueueController amq;
	private static BattleInfoBean bib;

	public static void initialize() {
		amq = MessageQueueControllerFactory.create();
	}

	/**
	 * 試合開始時の動作です.
	 * @throws UnknownHostException
	 * @throws JMSException
	 */
	public static void startGame() throws UnknownHostException, JMSException{
		//ロジック情報を取得(bean)
		ab.createBrain();
		BrainBean logic = ab.getLogicInfo();

		//起動しているマシンのIPアドレスを特定する
		String IPAdress ="123.123.1.123";//起動しているマシンのIPアドレスが入ります。
		IPAdress = myIP();

		//JSONに変換。(ロジックの情報は既にlogicに入っておりConvertJSONでまとめて変換します)
		JSONObject jo = cj.convertToJSONF(logic,IPAdress);

		//受信用Queueを作成
		amq.createQueue(IPAdress);

		//サーバーにロジック情報を送信する
		amq.sendMessage(jo);
	}

	/**
	 * IPアドレスを特定します（ローカルホストです）.
	 * @return IPアドレスです。IPv4を想定しています
	 * @throws UnknownHostException
	 */

	public static String myIP() throws UnknownHostException{
		//起動しているマシンのIPアドレスを特定する
		String IPAdress ="123.123.1.123";
		InetAddress host;
		host = InetAddress.getLocalHost();
		IPAdress = host.getHostAddress();
		return IPAdress;
	}

	/**
	 * 自分のターンごとに状況に応じた処理を行います.
	 * @throws JMSException
	 */
	public static void myTurn() throws JMSException{
		//変数eventの宣言
		String event = "blank";
		//Brainのインスタンス化
		ab.createBrain();
		//サーバーから送られてくるeventによって処理の内容が異なる
		while(true){
			//サーバーからのメッセージを受け取る
			bib =receive();

			//event情報だけbeanから取り出す
			event = bib.getEvent();
			EventType et = EventType.getEventType(event);

			//例外条件分岐
			if(et.isError()){
				//サーバーからのエラーメッセージを表示する
				String errors[]=bib.getError();
				logger.warn("以下のエラーが発生");
				for(int error = 0;error <= errors.length - 1;error++){
					logger.warn(errors[error]);
				}
				break;
			//終了条件分岐
			}else if(et.isFinish()){
				logger.info("試合が終了しました。");
				logger.info("You"+event);
				break;
			//差し手選択
			}else if(et.isTurn()){
				logger.debug("あなたの番です。");
				//brainに送るための盤面情報の変数locationを宣言
				String[][]location = null;
				//BattleInfoBeanから盤面情報を取得する
				location = bib.getLocation();
				//Brainから指し手情報を取得する
				bib = getLocation(location);
				//サーバーに送信する
				send(bib);
			//eventに何も入っていない場合（その他）
			}else{
				logger.error("eventの取得に失敗");
				break;
			}
		}

		//例外・終了条件時の受信用Queueの処理
		logger.info("受信用Queue終了処理開始");
		amq.quitQueue();
		logger.info("受信用Queue終了処理完了");
	}

	/**
	 * ActiveMQからのメッセージを受信します.
	 * @return 盤面情報やイベント情報を格納したBattleInfoBeanです
	 * @throws JMSException
	 */
	private static BattleInfoBean receive() throws JMSException {
		//ActiveMQからメッセージを受信する
		JSONObject obj2 = amq.receiveMessage();
		//JSONから変換
		bib =cj.convertFromJSON(obj2);
		return bib;
	}

	/**
	 * 盤面情報を渡して指し手の情報を渡します.
	 * @param location 盤面情報を格納した二次元配列のStringです
	 * @return 指し手の情報が入ったBattleInfoBeanです
	 */
	private static BattleInfoBean getLocation(String[][] location){
		//盤面情報をbrainに渡して指し手情報を取得する
		bib = ab.getLocation(location);
		return bib;
	}

	/**
	 * サーバーにメッセージを送信します.
	 * @param bib 盤面情報を格納したBattleInfoBeanです
	 * @throws JMSException
	 */
	private static void send(BattleInfoBean bib) throws JMSException{
		//JSONに変換
		JSONObject jo2 = cj.convertToJSONS(bib);
		//ActiveMQを通してサーバープログラムに送信する
		amq.sendMessage(jo2);
	}

}
