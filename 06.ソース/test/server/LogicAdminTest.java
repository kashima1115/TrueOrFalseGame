package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class LogicAdminTest {
	private static DbInsert dbi;
	private static final String FIRST_LOGIC_NAME="Alogic";
	private static final String SECOND_LOGIC_NAME="Clogic";
	private static final String FIRST_CREATOR="arahari";
	private static final String SECOND_CREATOR="kanayama";
	private static final String FIRST_VERSION="0.1";
	private static final String SECOND_VERSION="0.1";
	private static final String FIRST_ADDRESS="1";
	private static final String SECOND_ADDRESS="2";
	private static final String FIRST_KEY=FIRST_LOGIC_NAME+FIRST_CREATOR+FIRST_VERSION+FIRST_ADDRESS;
	private static final String SECOND_KEY=SECOND_LOGIC_NAME+SECOND_CREATOR+SECOND_VERSION+SECOND_ADDRESS;

	public static class LogicFixture{
		private final String exepectMapKey;
		private final int expectMapValue;

		private LogicFixture(String expectKey,int expectMapValue){
			this.exepectMapKey=expectKey;
			this.expectMapValue=expectMapValue;
		}

	}

	@DataPoints
	public static LogicFixture[] getParameters() {
		return new LogicFixture[] {
				new LogicFixture(FIRST_KEY,1),
				new LogicFixture(SECOND_KEY,3)
		};

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbi=new DbInsert();
		dbi.connect();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbi.disconnect();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Theory
	public void testAttachId(LogicFixture logicFixture) throws SQLException {

		//テスト用のロジック情報を格納するMapを生成
		Map<String,LogicInfoBean> testLogicMap=new HashMap<String,LogicInfoBean>();

		//生成したロジック情報を格納するBeanをMapに格納
		testLogicMap.put(FIRST_KEY, createLogicInfoBean(FIRST_LOGIC_NAME,FIRST_CREATOR,
				FIRST_VERSION,FIRST_ADDRESS));

		testLogicMap.put(SECOND_KEY, createLogicInfoBean(SECOND_LOGIC_NAME,SECOND_CREATOR,
				SECOND_VERSION,SECOND_ADDRESS));

		//テスト用ロジック情報Mapのキーを保管するBeanを生成
		ClientLogicBean testClb=new ClientLogicBean();

		testClb.setFirstLogic(FIRST_KEY);
		testClb.setSecondLogic(SECOND_KEY);

		//テスト対象クラスのインスタンス化
		LogicAdmin la=new LogicAdmin();

		//比較
		assertThat(la.attachId(dbi, testLogicMap, testClb),
				hasEntry(logicFixture.exepectMapKey,logicFixture.expectMapValue));
	}

	@Test
	public void testSameJudge() {

		Map<String,Integer> logicRefIdMap=new HashMap<String,Integer>();
		//キー名・値
		final String FIRST_KEY="1111";
		final String SECOND_KEY="1112";
		final int SAME_VALUE=1;

		//同じロジックID（同名ロジック）のロジック情報をMapに格納
		logicRefIdMap.put(FIRST_KEY, SAME_VALUE);
		logicRefIdMap.put(SECOND_KEY, SAME_VALUE);

		//キー名を保管するBeanを保管
		ClientLogicBean clb=new ClientLogicBean();
		clb.setFirstLogic(FIRST_KEY);
		clb.setSecondLogic(SECOND_KEY);

		//ロジック情報加工クラスのインスタンス化
		LogicAdmin la=new LogicAdmin();

		//同名ロジック判定
		boolean judge=la.sameJudge(logicRefIdMap, clb);

		//比較
		assertThat(judge,is(true));

	}

	@Test
	public void testNonSameJudge(){
		Map<String,Integer> logicRefIdMap=new HashMap<String,Integer>();
		//キー名・値
		final String FIRST_KEY="1111";
		final String SECOND_KEY="2222";
		final int FIRST_VALUE=1;
		final int SECOND_VALUE=2;

		//同じロジックID（同名ロジック）のロジック情報をMapに格納
		logicRefIdMap.put(FIRST_KEY, FIRST_VALUE);
		logicRefIdMap.put(SECOND_KEY, SECOND_VALUE);

		//キー名を保管するBeanを保管
		ClientLogicBean clb=new ClientLogicBean();
		clb.setFirstLogic(FIRST_KEY);
		clb.setSecondLogic(SECOND_KEY);

		//ロジック情報加工クラスのインスタンス化
		LogicAdmin la=new LogicAdmin();

		//同名ロジック判定
		boolean judge=la.sameJudge(logicRefIdMap, clb);

		//比較
		assertThat(judge,is(false));
	}

	/**
	 * Beanの生成を行う
	 */
	private LogicInfoBean createLogicInfoBean(String logicName,String creator,
			String version,String address){
		//Beanの生成
		LogicInfoBean testLib=new LogicInfoBean();

		testLib.setLogicName(logicName);
		testLib.setCreator(creator);
		testLib.setVersion(version);
		testLib.setAddress(address);

		return testLib;
	}

}
