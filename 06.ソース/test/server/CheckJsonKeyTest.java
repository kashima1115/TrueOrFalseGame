package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import net.sf.json.JSONObject;

@RunWith(Theories.class)
public class CheckJsonKeyTest {
	private static final String EVENT="event";
	private static final String NOT_EVENT="notEvent";
	private static final String X_AXIS="xAxis";
	private static final String NOT_X_AXIS="notXAxis";
	private static final String Y_AXIS="yAxis";
	private static final String NOT_Y_AXIS="notYAxis";
	private static final String LOGIC_NAME="logicName";
	private static final String NOT_LOGIC_NAME="notLogicName";
	private static final String LOGIC_WRITER="logicWriter";
	private static final String NOT_LOGIC_WRITER="notLogicWriter";
	private static final String LOGIC_VERSION="logicVersion";
	private static final String NOT_LOGIC_VERSION="notLogicVersion";
	private static final String ADDRESS="address";
	private static final String NOT_ADDRESS="notAddress";

	public static class LogicKeyFixture{
		private final String INPUT_EVENT;
		private final String INPUT_LOGIC_NAME;
		private final String INPUT_LOGIC_WRITER;
		private final String INPUT_LOGIC_VERSION;
		private final String INPUT_ADDRESS;
		private final boolean TRUE_OR_FALSE;

		private LogicKeyFixture(String inputEvent,String inputLogicName,String inputLogicWriter,
				String inputLogicVersion,String inputAddress,boolean trueOrFalse){

			this.INPUT_EVENT=inputEvent;
			this.INPUT_LOGIC_NAME=inputLogicName;
			this.INPUT_LOGIC_WRITER=inputLogicWriter;
			this.INPUT_LOGIC_VERSION=inputLogicVersion;
			this.INPUT_ADDRESS=inputAddress;
			this.TRUE_OR_FALSE=trueOrFalse;
		}
	}

	public static class LocationKeyFixture {
		private final String INPUT_X;
		private final String INPUT_Y;
		private final boolean TRUE_OR_FALSE;


		private LocationKeyFixture(String inputX, String inputY,boolean trueOrFalse) {
			this.INPUT_X = inputX;
			this.INPUT_Y = inputY;
			this.TRUE_OR_FALSE=trueOrFalse;
		}

	}

	@DataPoints
	public static LogicKeyFixture[] getLogicKeyParameters(){
		return new LogicKeyFixture[]{
				new LogicKeyFixture(EVENT,LOGIC_NAME,LOGIC_WRITER,LOGIC_VERSION,ADDRESS,true),
				new LogicKeyFixture(NOT_EVENT,LOGIC_NAME,LOGIC_WRITER,LOGIC_VERSION,ADDRESS,false),
				new LogicKeyFixture(EVENT,NOT_LOGIC_NAME,LOGIC_WRITER,LOGIC_VERSION,ADDRESS,false),
				new LogicKeyFixture(EVENT,LOGIC_NAME,NOT_LOGIC_WRITER,LOGIC_VERSION,ADDRESS,false),
				new LogicKeyFixture(EVENT,LOGIC_NAME,LOGIC_WRITER,NOT_LOGIC_VERSION,ADDRESS,false),
				new LogicKeyFixture(EVENT,LOGIC_NAME,LOGIC_WRITER,LOGIC_VERSION,NOT_ADDRESS,false)
		};
	}


	@DataPoints
	public static LocationKeyFixture[] getLocationKeyParameters(){
		return new LocationKeyFixture[]{
				new LocationKeyFixture(X_AXIS,Y_AXIS,true),
				new LocationKeyFixture(NOT_X_AXIS,Y_AXIS,false),
				new LocationKeyFixture(X_AXIS,NOT_Y_AXIS,false)
		};
	}



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

	@Theory
	public void testCheckLogicStructure(LogicKeyFixture logicKeyFixture) {
		//テスト用JSONObject作成
		JSONObject logicObj=new JSONObject();

		logicObj.accumulate(logicKeyFixture.INPUT_EVENT, "");
		logicObj.accumulate(logicKeyFixture.INPUT_LOGIC_NAME, "");
		logicObj.accumulate(logicKeyFixture.INPUT_LOGIC_WRITER, "");
		logicObj.accumulate(logicKeyFixture.INPUT_LOGIC_VERSION, "");
		logicObj.accumulate(logicKeyFixture.INPUT_ADDRESS, "");

		assertThat(CheckJsonKey.checkLogicStructure(logicObj),is(logicKeyFixture.TRUE_OR_FALSE));
	}

	@Theory
	public void testCheckLocationSturcture(LocationKeyFixture locationKeyFixture) {
		//テスト用JSONObject作成
		JSONObject locationObj=new JSONObject();

		locationObj.accumulate(locationKeyFixture.INPUT_X, "");
		locationObj.accumulate(locationKeyFixture.INPUT_Y, "");

		//比較
		assertThat(CheckJsonKey.checkLocationSturcture(locationObj),is(locationKeyFixture.TRUE_OR_FALSE));
	}

	@Test
	public void testCheckExitenceOfEventIsTrue() {
		//テスト用JSONObject作成
		JSONObject existObj=new JSONObject();

		existObj.accumulate(EVENT, "");

		//比較
		assertThat(CheckJsonKey.checkExitenceOfEvent(existObj),is(true));
	}

	@Test
	public void testCheckExitenceOfEventIsFalse(){
		//テスト用JSONObject作成
		JSONObject existObj=new JSONObject();

		existObj.accumulate(NOT_EVENT, "");

		//比較
		assertThat(CheckJsonKey.checkExitenceOfEvent(existObj),is(false));
	}

}
