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
import org.apache.log4j.Logger;

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
	private QueueConnection connectionS = null;
	private QueueConnection connectionR = null;
	private QueueSession sessionS = null;
	private QueueSession sessionR = null;
	private QueueReceiver receiver = null;
	private QueueSender sender = null;

/**
 * このクラスを呼び出しているマシンのIPアドレスを格納します.
 * ※（事前にconfig.propertyのserverIPAdressにサーバーのIPアドレスを入れてください）
 */
	final private static Logger logger = Logger.getLogger(ActiveMQMessaging.class.getName());

	@Override
	public void sendMessage(JSONObject gameInfo) throws JMSException{
		try {
			//config.propertyからサーバーのIPアドレスを取得する
    		ResourceBundle config = ResourceBundle.getBundle("config");
    		String serverIP = config.getString("serverIPAdress");

			//Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + serverIP +":61616");
            connectionS = factory.createQueueConnection();
            connectionS.start();

            //Senderの作成（メッセージ配信モードをclientモードに設定）
            sessionS = connectionS.createQueueSession(false,QueueSession.CLIENT_ACKNOWLEDGE);
            //送り先はサーバー
            Queue queueS = sessionS.createQueue(serverIP);
            sender= sessionS.createSender(queueS);

            //メッセージの送信
            TextMessage msg = sessionS.createTextMessage(gameInfo.toString());
            //TODO 動作確認用に付き
            System.out.println(msg);
            sender.send(msg);
        } catch (JMSException e) {
        	logger.fatal("送信失敗", e);
        	throw e;
        } finally {
            try {
                if (sender != null) {
                	sender.close();
                }
                if (sessionS != null){
                	sessionS.close();
                }
                if (connectionS != null) {
                	connectionS.close();
                }
            } catch (JMSException e) {
                logger.fatal("送信後の処理失敗",e);
                throw e;
            }
        }

	}

	@Override
	public JSONObject receiveMessage() throws JMSException{
		try {
            //メッセージの受信
            System.out.println("サーバーからの通信待ち");
            TextMessage msg = (TextMessage)receiver.receive();
            JSONObject gameInfo = JSONObject.fromObject(msg.getText());
            System.out.println("データ受信");
            //受信確認メッセージ送信
            msg.acknowledge();
            return gameInfo;
        } catch (JMSException e) {
        	logger.fatal("受信失敗",e);
        	throw e;
        }
	}

	@Override
	public void createQueue(String IPAddress) throws JMSException{
		//Queueの作成
		try {
            //Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connectionR = factory.createQueueConnection();
            connectionR.start();

            //Receiverの作成
            sessionR = connectionR.createQueueSession(false,QueueSession.CLIENT_ACKNOWLEDGE);
            Queue queue = sessionR.createQueue(IPAddress);//キュー名は自分のPCのIPアドレス
            receiver = sessionR.createReceiver(queue);

		}catch(JMSException e ){
			logger.fatal("キュー作成失敗",e);
			throw e;
		}
	}

	@Override
	public void quitQueue() {
		//Queueの終了処理
		try {
            if(receiver != null){
            	receiver.close();
            }
			if(sessionR != null){
				sessionR.close();
            }
			if(connectionR != null){
				connectionR.close();
			}
        } catch (JMSException e) {
            logger.fatal("キュー終了失敗");
        	e.printStackTrace();
        }

	}

	@Override
	public void sendMessage(JSONObject gameInfo,String IPAdress){
		return;
	}
}
