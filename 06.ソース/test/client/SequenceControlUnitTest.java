package client;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import javax.jms.JMSException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import messageQueue.DummyActiveMQMessaging;
import messageQueue.MessageQueueControllerFactory;
import net.sf.json.JSONObject;

public class SequenceControlUnitTest {
	CreateGameInfo cgi = new CreateGameInfo();
	BattleInfoBean bib = new BattleInfoBean();
	ConvertJSON cj = new ConvertJSON();
	DummyActiveMQMessaging amq = new DummyActiveMQMessaging();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MessageQueueControllerFactory.isDummy(true);
		SequenceControl.initialize();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartGame() {
		try {
			SequenceControl.startGame();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			System.exit(0);
		}
		assertEquals(cgi.ready(),amq.receiveMessage());
		amq.asssertReceiveMessage();
		System.out.println("startgame");
	}

	@Test
	public void testMyTurnエラー() throws JMSException {
		JSONObject obj = new JSONObject();
		obj = cgi.error();
		amq.sendMessage(obj);
		SequenceControl.myTurn();
		CreateGameInfo cgi2 = new CreateGameInfo();
		assertEquals(cgi2.error(),amq.receiveMessage());
		amq.asssertReceiveMessage();
		System.out.println("error");
	}

	@Test
	public void testMyTurnノーマルエンド() throws JMSException{
		JSONObject obj = new JSONObject();
		obj = cgi.yourturn();
		amq.sendMessage(obj);
		SequenceControl.myTurn();
		assertEquals(cgi.win(),amq.receiveMessage());
		System.out.println("normal");
	}

}
