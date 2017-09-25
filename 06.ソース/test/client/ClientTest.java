package client;

import javax.jms.JMSException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import messageQueue.MessageQueueControllerFactory;

public class ClientTest {

	@Before
	public void setUp() throws Exception {
		MessageQueueControllerFactory.isDummy(true);
		SequenceControl.initialize();
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void mainTest() {
		try {
			SequenceControl.myTurn();
		} catch (JMSException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
