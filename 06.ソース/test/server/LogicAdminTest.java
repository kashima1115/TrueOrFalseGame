package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.json.JSONObject;

public class LogicAdminTest {
	private LogicAdmin la;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.la=new LogicAdmin();

		for(int i=1;i<3;i++){
			//JSONObject生成
			JSONObject testGameInfo=new JSONObject();
			testGameInfo.accumulate("logicName",Integer.toString(i));
			testGameInfo.accumulate("creator",Integer.toString(i));
			testGameInfo.accumulate("version",Integer.toString(i));
			testGameInfo.accumulate("address",Integer.toString(i));

			//ロジック情報をロジック管理クラスで保持
			la.logicSet(testGameInfo);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLogicSetFirstKey() throws IllegalArgumentException, IllegalAccessException{

		try{
			//対象テストクラスのフィールド取得
			Field fieldLogicMap=LogicAdmin.class.getDeclaredField("logicMap");

			//アクセス権限付与
			fieldLogicMap.setAccessible(true);

			//比較
			assertThat((Map<String,LogicInfoBean>)fieldLogicMap.get(this.la),hasKey("1111"));

		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}

	@Test
	public void testLogicSetSecondKey() {

		try{
			//対象テストクラスのフィールド取得
			Field fieldLogicMap=LogicAdmin.class.getDeclaredField("logicMap");

			//アクセス権限付与
			fieldLogicMap.setAccessible(true);

			//比較
			assertThat((Map<String,LogicInfoBean>)fieldLogicMap.get(this.la),hasKey("2222"));

		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
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
