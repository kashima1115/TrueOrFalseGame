package server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 試合管理クラスのテストクラス
 * @author kanayama
 *
 */
public class BattleIdAdminTest {
	private static DbInsert dbi;
	private static int formerId;
	private static int expectId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//コネクション接続
		BattleIdAdminTest.dbi=new DbInsert();
		BattleIdAdminTest.dbi.connect();
		BattleIdAdminTest.formerId=dbi.getFormerId();
		BattleIdAdminTest.expectId=++BattleIdAdminTest.formerId;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//コネクション切断
		BattleIdAdminTest.dbi.disconnect();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBattleId() throws Exception {
		//試合ID取得
		int battleId=BattleIdAdmin.getBattleID(BattleIdAdminTest.dbi);

		assertEquals(expectId,battleId);

	}

}
