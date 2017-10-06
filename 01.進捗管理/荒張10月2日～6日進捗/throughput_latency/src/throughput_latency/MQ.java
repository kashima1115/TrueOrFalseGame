package throughput_latency;

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

public class MQ {

	static TextMessage msg;
	static QueueConnection connection = null;
	static QueueSession session = null;
	static QueueSender sender = null;
	static QueueReceiver receiver = null;

	public static TextMessage accses(int loop) {

		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.6.103:61616");
			connection = factory.createQueueConnection();
			connection.start();

			// Senderの作成
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("accses");
			sender = session.createSender(queue);


			// メッセージの送信
			msg = session.createTextMessage("" + loop + "");
			msg.acknowledge();
			sender.send(msg);

		} catch (JMSException e) {
			e.printStackTrace();

		} finally {
			try {
				if (sender != null)
					sender.close();
				if (session != null)
					session.close();
				if (connection != null)
					connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	public static TextMessage receive() {

		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();

			// Receiverの作成
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("accses");
			receiver = session.createReceiver(queue);

			// メッセージの受信
			msg = (TextMessage) receiver.receive();

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				receiver.close();
				session.close();
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	public static TextMessage reAccses(int id) {

		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.6.103:61616");
			connection = factory.createQueueConnection();
			connection.start();

			// Senderの作成
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("reAccses");
			sender = session.createSender(queue);

			// メッセージの送信
			msg = session.createTextMessage("" + id + "");
			msg.acknowledge();
			sender.send(msg);

		} catch (JMSException e) {
			e.printStackTrace();

		} finally {
			try {
				if (sender != null)
					sender.close();
				if (session != null)
					session.close();
				if (connection != null)
					connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	public static TextMessage reReceive() {

		try {
			// Connectionを作成
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();

			// Receiverの作成
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("reAccses");
			receiver = session.createReceiver(queue);

			// メッセージの受信
			msg = (TextMessage) receiver.receive();

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				receiver.close();
				session.close();
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

}