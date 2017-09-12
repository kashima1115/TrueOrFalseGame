package server;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.json.JSONObject;

public class LogicAdminTest {

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
	public void testLogicSet() {

		//ロジック管理クラスをインスタンス化
		LogicAdmin la=new LogicAdmin();

		//JSONObject生成
		JSONObject testGameInfo=new JSONObject();

		for(int i=1;i<3;i++){
			testGameInfo.accumulate("logicName",Integer.toString(i));
			testGameInfo.accumulate("creator",Integer.toString(i));
			testGameInfo.accumulate("version",Integer.toString(i));
			testGameInfo.accumulate("address",Integer.toString(i));
		}

		//ロジック情報をロジック管理クラスで保持
		la.logicSet(testGameInfo);

		try{
			Field fieldLogicMap=LogicAdmin.class.getDeclaredField("logicMap");
		}catch(Exception e){

		}



	}

	@Test
	public void testGetLogicMap() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetClientAddressBean() {
		fail("まだ実装されていません");
	}

	@Test
	public void testFillClient() {
		fail("まだ実装されていません");
	}

	@Test
	public void testAttachId() {
		fail("まだ実装されていません");
	}

	@Test
	public void testSameJudge() {
		fail("まだ実装されていません");
	}

}
