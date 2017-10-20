package server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
/**
 * 試合管理クラスのテストクラス
 * @author kanayama
 *
 */
public class BattleIdAdminTest {

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
	public void testGetBattleId(@Mocked DbOperation dbi) throws Exception {
		//期待値
		int expectId=51;

		//モックDBからの返り値
		new Expectations(){
			{
				dbi.getFormerId();
				result=expectId-1;
			}
		};
		//試合ID取得
		int battleId=BattleIdAdmin.getBattleID(dbi);

		assertEquals(expectId,battleId);

	}

}
