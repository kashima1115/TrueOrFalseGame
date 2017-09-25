package messageQueue;

import javax.jms.JMSException;

import net.sf.json.JSONObject;
/**
 * MQの制御のためのインターフェース.
 * @author hatsugai
 *
 */
public interface MessageQueueController {
	/**
	 * メッセージを送信します.
	 * @param gameInfo 試合に関わる情報が入っています（JSONObject方式です)
	 * @throws JMSException
	 */
	public void sendMessage(JSONObject gameInfo) throws JMSException;
	/**
	 * メッセージを受信します.
	 * @return 受信したメッセージです（JSONObject方式です)
	 * @throws JMSException
	 */
	public JSONObject receiveMessage() throws JMSException;
	/**
	 * 受信用Queueを作成します.
	 * @param IPAdress 自身のマシンのIPアドレスです
	 */
	public void createQueue(String IPAdress) throws JMSException;
	/**
	 * 受信用Queueを破棄します.
	 */
	public void quitQueue();
	/**
	 * サーバーがメッセージをクライアントに送信.
	 * @param gameInfo 試合に関わる情報が入っています（JSONObject方式です)
	 * @param IPAdress メッセージの送信先IPアドレス
	 */
	public void sendMessage(JSONObject gameInfo,String IPAdress);

}
