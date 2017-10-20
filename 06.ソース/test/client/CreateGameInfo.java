package client;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import net.sf.json.JSONObject;

public class CreateGameInfo {
	JSONObject gameInfo = new JSONObject();

	public JSONObject ready(){
		gameInfo.put("logicName", "jyuunisai");
		gameInfo.put("logicVersion", "0.1");
		gameInfo.put("logicWriter", "初谷惇志");
		gameInfo.put("address", SequenceControlForTest.myIP());
		gameInfo.put("event", "ready");
		return gameInfo;
	}

	public JSONObject yourturn(){
		gameInfo.put("event", "YourTurn");
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		gameInfo.put("location",loc);
		return gameInfo;
	}

	public JSONObject win(){
		JSONObject gameInfo = new JSONObject();
		gameInfo.put("event", "win");
		return gameInfo;
	}

	public JSONObject finish(){
		gameInfo.put("event", "finish");
		return gameInfo;
	}

	public JSONObject turnEnd(){
		gameInfo.put("xAxis", "1");
		gameInfo.put("yAxis", "1");
		gameInfo.put("event", "TurnEnd");
		return gameInfo;
	}

	public JSONObject error(){
		String error = "oversubscribed";
		gameInfo.put("error", error);
		return gameInfo;
	}

	public JSONObject convertFromJSONViaMessageToString(){
		JSONObject obj = this.yourturn();
		QueueSession session = null;
        QueueSender sender = null;
        QueueConnection connection = null;
        try {
            //Connectionを作成
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createQueueConnection();
            connection.start();

            //Senderの作成
            session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("queue_test");
            sender= session.createSender(queue);

            //メッセージの送信
            TextMessage msg = session.createTextMessage(obj.toString());
            JSONObject rcv = new JSONObject();
//            System.out.println(msg);
//            String m[] = msg.toString().split("text = ");
//            String ms = m[1].substring(0,m[1].length()-1);
    		rcv = JSONObject.fromObject(msg.getText());
    		System.out.println(rcv);
    		return rcv;
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        } finally {

            try {
                if (sender != null) sender.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

	}

}
