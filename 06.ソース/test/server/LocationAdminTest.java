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

@RunWith(Theories.class)
public class LocationAdminTest {
	private final int MAX_X = 3;
	private final int MAX_Y = 3;
	private final String NOT_FILL = "_";
	private static final String FIRST_PLAYER = "○";
	private static final String SECOND_PLAYER = "×";

	public static class LocationFixture {
		private final int inputTurn;
		private final int inputX;
		private final int inputY;
		private final String PLAYER_MARK;


		private LocationFixture(int inputTurn, int inputX, int inputY,String playerMark) {
			this.inputTurn = inputTurn;
			this.inputX = inputX;
			this.inputY = inputY;
			this.PLAYER_MARK=playerMark;
		}

	}

	@DataPoints
	public static LocationFixture[] getParameters() {
		return new LocationFixture[] {
				// 手番の設定
				new LocationFixture(1, 0, 0,FIRST_PLAYER),
				new LocationFixture(2, 1, 1,SECOND_PLAYER) };
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

	@Test
	public void testGetLocation() {
		// 期待値の配列を作成
		String[][] testLocation = new String[MAX_X][MAX_Y];

		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				testLocation[x][y] = NOT_FILL;
			}
		}

		// 対象テストクラスをインスタンス化
		LocationAdmin la = new LocationAdmin();

		//比較
		assertThat(la.getLocation(), arrayContaining(testLocation));
	}

	@Theory
	public void testUpdateLocation(LocationFixture locationFixture) {
		// 期待値の配列を作成
		String[][] testLocation = new String[MAX_X][MAX_Y];

		//各プレイヤー毎に設定した盤面情報に更新する
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {

				if(x==locationFixture.inputX && y==locationFixture.inputY){
					testLocation[x][y] = locationFixture.PLAYER_MARK;
				}else{
					testLocation[x][y] = NOT_FILL;
				}
			}
		}

		//LogicInfoBeanのインスタンス化
		LocationInfoBean lifb=new LocationInfoBean();

		//挿入値をセット
		lifb.setTurn(locationFixture.inputTurn);
		lifb.setLocationX(locationFixture.inputX);
		lifb.setLocationY(locationFixture.inputY);

		//対象テストクラスをインスタンス化
		LocationAdmin la = new LocationAdmin();

		assertThat(la.updateLocation(lifb), arrayContaining(testLocation));
	}

}
