package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * エラー通知オブジェクト作成クラスのテストクラス
 * @author kanayama
 *
 */
public class InformErrorTest {

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
	public void testOversubscribedError() {
		assertThat(InformError.oversubscribedError().toString(),is("{\"error\":\"oversubscribed\"}"));
	}

	@Test
	public void testSameLogicError() {
		assertThat(InformError.sameLogicError().toString(),is("{\"error\":\"sameLogic\"}"));
	}

	@Test
	public void testRuleError() {
		assertThat(InformError.ruleError().toString(),is("{\"error\":\"notBlank\"}"));
	}

	@Test
	public void testNotExpectEventError() {
		assertThat(InformError.notExpectEventError().toString(),is("{\"error\":\"notExpectEvent\"}"));
	}

}
