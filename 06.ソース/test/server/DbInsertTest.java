package server;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

public class DbInsertTest {
	private static DbInsert dbi;


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

	@Test
	public void testLogicInsert() {
		fail("まだ実装されていません");
	}

	@Test
	public void testLocationInsert() {
		fail("まだ実装されていません");
	}

	@Test
	public void testResultInsert() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetLogicId() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetFormerId() throws SQLException {
		new MockUp<ResultSet>() {
		      @Mock
		      public int getInt() {
		        return 5;
		      }
		};

		//テスト対象メソッドの実行
		dbi.getFormerId();
	}

	@Test
	public void testConnect() {
		fail("まだ実装されていません");
	}

	@Test
	public void testDisconnect() {
		fail("まだ実装されていません");
	}

}
