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

	QueueConnection connection = null;
	QueueSession session = null;
	QueueReceiver receiver = null;
	QueueSender sender = null;

	@Override
	public void sendMessage(JSONObject gameInfo) {
		return;

	}

	@Override
	public JSONObject receiveMessage() {
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
		}
		return null;
	}

	@Override
	public void createQueue(String IPAddress) {
		mypcip = IPAddress;
		// Queueの作成
		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();

			// Receiverの作成
			session = connection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
			Queue queue = session.createQueue(IPAddress);// キュー名はとりあえず自分のPCのIPアドレス
			receiver = session.createReceiver(queue);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
		}
	}

	@Override
	public void quitQueue() {
		// Queueの終了処理
		try {
			receiver.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
		}

	}

	@Override
	public void sendMessage(JSONObject gameInfo, String IPAdress) {
		try {
			/*
			 * クライアントから受信したロジック情報を元にIPアドレスを取得 Connectionを作成
			 */
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://" + IPAdress + ":61616");
			connection = factory.createQueueConnection();
			connection.start();

			// Senderの作成（メッセージ配信モードをclientモードに設定）
			session = connection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
			// 送り先はサーバー
			Queue queue = session.createQueue(IPAdress);
			sender = session.createSender(queue);
			// メッセージの送信
			TextMessage msg = session.createTextMessage(gameInfo.toString());
			sender.send(msg);

		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
		}finally{
			try{
				if(sender!=null){
					sender.close();
				}

				if(connection!=null){
					connection.close();
				}

				if(session!=null){
					session.close();
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
