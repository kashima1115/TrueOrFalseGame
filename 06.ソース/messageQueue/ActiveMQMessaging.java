package messageQueue;

import java.util.ResourceBundle;

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

import client.SequenceControl;
import net.sf.json.JSONObject;
/**
 * ActiveMQの操作を行うクラスです.
 * @author hatsugai
 *
 */
public class ActiveMQMessaging implements MessageQueueController {
/**
 * ActiveMQ関連の変数です.
 */
	QueueConnection connection = null;
    QueueSession session = null;
    QueueReceiver receiver = null;
    QueueSender sender = null;

/**
 * このクラスを呼び出しているマシンのIPアドレスを格納します.
 */
	public static String mypcip = SequenceControl.myIP();

	@Override
	public void sendMessage(JSONObject gameInfo) {
		try {
			//config.propertyからサーバーのIPアドレスを取得する（事前にサーバーのIPアドレスを入れてください）
    		ResourceBundle config = ResourceBundle.getBundle("config");
    		String serverIP = config.getString("serverIPAdress");

			//Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + serverIP +":61616");
            connection = factory.createQueueConnection();
            connection.start();

            //Senderの作成（メッセージ配信モードをclientモードに設定）
            session = connection.createQueueSession(false,QueueSession.CLIENT_ACKNOWLEDGE);
            //送り先はサーバー
            Queue queue = session.createQueue(serverIP);
            sender= session.createSender(queue);

            //メッセージの送信
            TextMessage msg = session.createTextMessage(gameInfo.toString());
            sender.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
            System.out.println("通信に問題が発生しました。");
        } finally {

            try {
                if (sender != null) {
                	sender.close();
                }
                if (session != null){
                	session.close();
                }
                if (connection != null) {
                	connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

	}

	@Override
	public JSONObject receiveMessage() {
		try {
            //Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createQueueConnection();
            connection.start();

            //Receiverの作成
            session = connection.createQueueSession(false,QueueSession.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue(mypcip);//キュー名は自分のIPアドレス
            receiver = session.createReceiver(queue);

            //メッセージの受信
            System.out.println("サーバーからの通信待ち");
            TextMessage msg = (TextMessage)receiver.receive();
            JSONObject gameInfo = JSONObject.fromObject(msg.getText());
            System.out.println("データ受信");
            //受信確認メッセージ送信
            msg.acknowledge();
            return gameInfo;
        } catch (JMSException e) {
            e.printStackTrace();
            System.out.println("通信に問題が発生しました。");
        }
		return null;
	}

	@Override
	public void createQueue(String IPAddress) {
		mypcip = IPAddress;


		//Queueの作成
		try {
            //Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createQueueConnection();
            connection.start();

            //Receiverの作成
            session = connection.createQueueSession(false,QueueSession.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue(IPAddress);//キュー名はとりあえず自分のPCのIPアドレス
            receiver = session.createReceiver(queue);
		}catch(JMSException e ){
			e.printStackTrace();
			System.out.println("通信に問題が発生しました。");
		}
	}

	@Override
	public void quitQueue() {
		//Queueの終了処理
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
	public void sendMessage(JSONObject gameInfo,String IPAdress){
		return;
	}
}
