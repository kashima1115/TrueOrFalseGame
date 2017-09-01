package clientSingleTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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

	public String logicName = "braintest1";
	public String logicVersion = "0.2";
	public String writer = "someone";
	public String a = "1";
	public String b = "1";
	public String sum = a+b;
	Brain bra = new Brain();
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
		BrainBean bb = ab.getLogicInfo();
		//ロジック名が一致するかチェック
		assertEquals(logicName,bb.getLogicName());
		System.out.println("ロジック名 "+logicName+"―OK!");
		//ロジックバージョンが一致するかチェック
		assertEquals(logicVersion,bb.getLogicVersion());
		System.out.println("ロジックバージョン "+logicVersion+"―OK!");
		//作者が一致するかチェック
		assertEquals(writer,bb.getWriter());
		System.out.println("作者 "+writer+"―OK!");
		System.out.println("getLogicInfo―OK!");
		System.out.println("---------------------------------------------------------");
	}

	@Test
	public void testGetLocation() {
		ab.setBrainForTest(bra);
		List<BattleInfoBean> bib = new ArrayList<BattleInfoBean>();
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		//getLocation実行（引数に盤面情報を渡す）
		bib = ab.getLocation(loc);
		for(BattleInfoBean binb:bib){
			assertEquals(Integer.parseInt(a),binb.getxAxis());
			//最初にコンソールに入れた値
			System.out.println("xAxis is " + binb.getxAxis() + "―OK!");
			assertEquals(Integer.parseInt(b),binb.getyAxis());
			//次にコンソールに入れた値
			System.out.println("yAxis is " + binb.getyAxis() + "―OK!");
		}
		System.out.println("getLocation―OK!");
		System.out.println("---------------------------------------------------------");
	}

	public class Brain implements BrainControl{

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
			String a = sum;
			return a;
		}

	}

}
