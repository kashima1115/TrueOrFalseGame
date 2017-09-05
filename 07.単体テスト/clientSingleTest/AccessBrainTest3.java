package clientSingleTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import brain.BrainBean;
import brain.BrainControl;
import client.AccessBrainForTest;
import client.BattleInfoBean;

/**
 * AccessBrainForTestのテストコードです。
 * @author hatsugai
 * @version 0.3
 */

public class AccessBrainTest3 {

	//brain用変数
	public String logicName = "braintests";
	public String logicVersion = "0.2";
	public String writer = "some";
	public String xa = "1";
	public String yb = "2";
	TestBrain bra = new TestBrain();
	AccessBrainForTest ab = new AccessBrainForTest();

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
	public void testGetLogicInfo() {
		ab.setBrainForTest(bra);
		//getLogicInfoを実行
		BrainBean bab = ab.getLogicInfo();
		//ロジック名が一致するかチェック
		assertEquals(logicName,bab.getLogicName());
		System.out.println("ロジック名 "+logicName+"―OK!");
		//ロジックバージョンが一致するかチェック
		assertEquals(logicVersion,bab.getLogicVersion());
		System.out.println("ロジックバージョン "+logicVersion+"―OK!");
		//作者が一致するかチェック
		assertEquals(writer,bab.getWriter());
		System.out.println("作者 "+writer+"―OK!");
		System.out.println("getLogicInfo―OK!");
		System.out.println("---------------------------------------------------------");
	}

	@Test
	public void testGetLocation() {
		ab.setBrainForTest(bra);
		BattleInfoBean bib = new BattleInfoBean();
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		//getLocation実行（引数に盤面情報を渡す）
		bib = ab.getLocation(loc);
		assertEquals(Integer.parseInt(xa),bib.getxAxis());
		//最初にコンソールに入れた値
		System.out.println("xAxis is " + bib.getxAxis() + "―OK!");
		assertEquals(Integer.parseInt(yb),bib.getyAxis());
		//次にコンソールに入れた値
		System.out.println("yAxis is " + bib.getyAxis() + "―OK!");

		System.out.println("getLocation―OK!");
		System.out.println("---------------------------------------------------------");
	}

	public class TestBrain implements BrainControl{

		@Override
		public BrainBean logicInfo() {
			BrainBean bb = new BrainBean();
			bb.setLogicName(logicName);
			bb.setLogicVersion(logicVersion);
			bb.setWriter(writer);
			return bb;
		}

		@Override
		public String getLocation(String[][] location) {
			String loc = xa+yb;
			return loc;
		}

	}

}
