package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocationAdminTest {
	private final int MAX_X=3;
	private final int MAX_Y=3;
	private final String NOT_FILL="_";

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
	public void testGetLocation() {
		//期待値の配列を作成
		String[][] testLocation=new String[MAX_X][MAX_Y];

		for(int x=0;x<MAX_X;x++){
			for(int y=0;y<MAX_Y;y++){
				testLocation[x][y]=NOT_FILL;
			}
		}

		//対象テストクラスをインスタンス化
		LocationAdmin la=new LocationAdmin();

		assertThat(la.getLocation(),arrayContaining(testLocation));
	}

	@Test
	public void testUpdateLocation() {
		fail("まだ実装されていません");
	}

}
