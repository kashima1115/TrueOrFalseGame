package client;

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
		SequenceControl.myTurn();
	}

}
