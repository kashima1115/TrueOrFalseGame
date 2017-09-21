package client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import messageQueue.MessageQueueControllerFactory;
import net.sf.json.JSONObject;

public class SequenceControlUnitTest {
	CreateGameInfo cgi = new CreateGameInfo();
	BattleInfoBean bib = new BattleInfoBean();
	JSONObject obj = new JSONObject();
	ConvertJSONForTest cj = new ConvertJSONForTest();

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
		JSONObject obj = new JSONObject();
		obj = SequenceControlForTest.startGame();
		assertEquals(cgi.ready().toString(),obj.toString());
	}

	@Test
	public void testMyTurnノーマル() {
		obj = cgi.yourturn();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		String returns = SequenceControlForTest.myTurn(bib);
		assertEquals("YourTurn11", returns);
	}

	@Test
	public void testMyTurnウィン() {
		obj = cgi.win();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		String returns = SequenceControlForTest.myTurn(bib);
		assertEquals("win", returns);
	}

	@Test
	public void testMyTurnエラー() {
		obj = cgi.error();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		String returns = SequenceControlForTest.myTurn(bib);
		assertEquals("error", returns);
	}

}
