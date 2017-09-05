package clientSingleTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import brain.BrainBean;
import client.AccessBrain;
import client.BattleInfoBean;

/**
 * AccessBrainのテストコードです。座標入力は手作業です。
 * @author hatsugai
 *
 */

public class AccessBrainTest1 {

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
		AccessBrain ab = new AccessBrain();
		ab.createBrain();
		//getLogicInfoを実行
		BrainBean bb = ab.getLogicInfo();
		//ロジック名が一致するかチェック
		assertEquals("manual",bb.getLogicName());
		System.out.println("ロジック名―OK!");
		//ロジックバージョンが一致するかチェック
		assertEquals("0.0",bb.getLogicVersion());
		System.out.println("ロジックバージョン―OK!");
		//作者が一致するかチェック
		assertEquals("初谷惇志",bb.getWriter());
		System.out.println("作者―OK!");
	}

	@Test
	public void testGetLocation() {
		BattleInfoBean bib = new BattleInfoBean();
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		//getLocation実行（引数に盤面情報を渡す）
		AccessBrain ab = new AccessBrain();
		ab.createBrain();
		bib = ab.getLocation(loc);
			//最初にコンソールに入れた値
			System.out.println("xAxis is " + bib.getxAxis());
			//次にコンソールに入れた値
			System.out.println("yAxis is " + bib.getyAxis());
		System.out.println("getLocation―OK!");
	}

//	private class TestBrain implements BrainControl{
//
//		@Override
//		public BrainBean logicInfo() {
//			// TODO 自動生成されたメソッド・スタブ
//			return null;
//		}
//
//		@Override
//		public String getLocation(String[][] location) {
//			// TODO 自動生成されたメソッド・スタブ
//			return null;
//		}
//
//	}

}
