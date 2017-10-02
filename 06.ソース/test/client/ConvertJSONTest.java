package client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import brain.BrainBean;
import net.sf.json.JSONObject;

public class ConvertJSONTest {

	//ConvertJSONForTest cj = new ConvertJSONForTest();
	ConvertJSON cj = new ConvertJSON();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertToJSONF() {
		JSONObject obj = new JSONObject();
		//BrainBeanに値をセットする
		BrainBean logic = new BrainBean();
		String logName = "testName";
		String logVer = "1.0";
		String writer = "testCode";
		String IPAdress = "192.168.6.123";
		logic.setLogicName(logName);
		logic.setLogicVersion(logVer);
		logic.setWriter(writer);
		//ConvertJSONFを呼び出す
		obj = cj.convertToJSONF(logic, IPAdress);
		System.out.println(obj);
		//objに実際に値が入っているのか確認する
		assertEquals(logName, obj.get("logicName"));
		assertEquals(logVer,obj.get("logicVersion"));
		assertEquals(writer,obj.get("logicWriter"));
		assertEquals(IPAdress,obj.get("address"));
		assertEquals("ready",obj.get("event"));
	}

	@Test
	public void testConvertFromJSONユアターンの場合() {
		JSONObject obj = new JSONObject();
		CreateGameInfo cgi = new CreateGameInfo();
		obj = cgi.yourturn();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		System.out.println(obj);
		assertEquals("YourTurn",bib.getEvent());
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		assertArrayEquals(loc,bib.getLocation());
		}

	@Test
	public void testConvertFromJSONウィンの場合() {
		JSONObject obj;
		CreateGameInfo cgi = new CreateGameInfo();
		obj = cgi.win();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		System.out.println(obj);
		assertEquals("win",bib.getEvent());
		}

	@Test
	public void testConvertFromJSONエラー発生(){
		JSONObject obj;
		CreateGameInfo cgi = new CreateGameInfo();
		obj = cgi.error();
		BattleInfoBean bib = cj.convertFromJSON(obj);
		System.out.println(obj);
		assertEquals("error",bib.getEvent());
		String expectError = "oversubscribed";
		assertEquals(expectError,bib.getError());
	}

	@Test
	public void testConvertToJSONS() {
		JSONObject obj = new JSONObject();
		//BattleInfoBeanに値をセットする
		BattleInfoBean bib = new BattleInfoBean();
		int xa = 1;
		int ya = 2;
		bib.setxAxis(xa);
		bib.setyAxis(ya);
		bib.setEvent("TurnEnd");
		//ConvertToJSONSを呼び出す
		obj = cj.convertToJSONS(bib);
		System.out.println(obj);
		//objに実際に値が入っているのか確認する
		assertEquals(xa,obj.get("xAxis"));
		assertEquals(ya,obj.get("yAxis"));
		assertEquals("TurnEnd",obj.get("event"));
	}
	@Test
	public void testConvertFromJSON空白(){
		JSONObject obj = new JSONObject();
		obj.put("blank", "blank");
		BattleInfoBean bib = cj.convertFromJSON(obj);
		System.out.println(obj);
		System.out.println("Eventは"+bib.getEvent());
		assertEquals("blank",bib.getEvent());
	}

}
