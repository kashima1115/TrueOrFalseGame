package messageQueue;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import net.sf.json.JSONObject;

/**
 * サーバー側でのActiveMQの操作を行うクラスです.
 *
 * @author kanayama
 *
 */
public class ServerActiveMQMessaging implements MessageQueueController {

	QueueConnection connectionS = null;
	QueueSession sessionS = null;
	QueueConnection connectionR = null;
	QueueSession sessionR = null;
	QueueReceiver receiver = null;
	QueueSender sender = null;

	@Override
	public void sendMessage(JSONObject gameInfo) {
		return;

	}

	@Override
	public JSONObject receiveMessage() throws JMSException{
		//
		try {
			// メッセージの受信
			TextMessage msg = (TextMessage)receiver.receive();
			JSONObject obj = JSONObject.fromObject(msg.getText());
			msg.acknowledge();
			return obj;
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
				throw e;
		}
	}

	@Override
	public void createQueue(String IPAddress) throws JMSException {
		mypcip = IPAddress;
		// Queueの作成
		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connectionR = factory.createQueueConnection();
			connectionR.start();

			// Receiverの作成
			sessionR = connectionR.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
			Queue queue = sessionR.createQueue("server");// キュー名はとりあえず自分のPCのIPアドレス
			receiver = sessionR.createReceiver(queue);
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
			throw e;
		}
	}

	@Override
	public void quitQueue() {
		// Queueの終了処理
		try {
			receiver.close();
			sessionR.close();
			connectionR.close();
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
			try {
				throw e;
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void sendMessage(JSONObject gameInfo, String IPAdress) throws JMSException {
		try {
			/*
			 * クライアントから受信したロジック情報を元にIPアドレスを取得 Connectionを作成
			 */
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://" + IPAdress + ":61616");
			connectionS = factory.createQueueConnection();
			connectionS.start();

			// Senderの作成（メッセージ配信モードをclientモードに設定）
			sessionS = connectionS.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
			// 送り先はサーバー
			Queue queue = sessionS.createQueue(IPAdress);
			sender = sessionS.createSender(queue);
			// メッセージの送信
			TextMessage smsg = sessionS.createTextMessage(gameInfo.toString());
			sender.send(smsg);

		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
				throw e;

		}finally{
			try{
				if(sender!=null){
					sender.close();
				}

				if(connectionS!=null){
					connectionS.close();
				}

				if(sessionS!=null){
					sessionS.close();
				}
			}catch(JMSException e){
				e.printStackTrace();
			}

		}

	}

	/**
	 * このクラスを呼び出しているマシンのIPアドレスを格納します
	 */
	public static String mypcip;

}
