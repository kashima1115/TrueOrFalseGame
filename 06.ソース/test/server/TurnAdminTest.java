package server;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

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
import net.sf.json.test.JSONAssert;

@RunWith(Theories.class)
public class TurnAdminTest {
	private ClientLogicBean clb;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		//クライアントアドレス保持Beanをインスタンス化
		this.clb=new ClientLogicBean();

		for(int i=0;i<2;i++){

			//Mapに格納するキー名
			String logicMapKey=Integer.toString(i)+Integer.toString(i)+Integer.toString(i)+
					Integer.toString(i);

			//クライアントアドレス保持Beanにアドレスをセット
			if(i==0){
				this.clb.setFirstLogic(logicMapKey);
			}else if(i==1){
				this.clb.setSecondLogic(logicMapKey);
			}
		}


	}

	@After
	public void tearDown() throws Exception {
	}

	@DataPoints
	public static int[] patternTurn={2,3,4,5,6,7,8,9};

	@Test
	public void decideFirstTest() {
		//手番管理クラスをインスタンス化
		TurnAdmin ta=new TurnAdmin(this.clb);

		//先攻手番クライアントのアドレスを取得(早いもの順）
		String turnLogic=ta.decideFirst();

		//比較
		assertEquals(this.clb.getFirstLogic(),turnLogic);
	}

	@Theory
	public void judgeTurnTest(int patternTurn) {
		//手番管理クラスをインスタンス化
		TurnAdmin ta=new TurnAdmin(clb);

		//次手番クライアントアドレス格納用アドレス
		String nextLogic=null;

		//次手番のクライアントを判定・取得
		for(int turn=1;turn<patternTurn;turn++){
			nextLogic=ta.judgeTurn();
		}

		//比較
		if(patternTurn%2==1){
			assertEquals(this.clb.getFirstLogic(),nextLogic);
		}else{
			assertEquals(this.clb.getSecondLogic(),nextLogic);
		}

	}

	@Test
	public void informTurnTest() {

		//取得するイベント情報の期待値
		final String EVENT="YourTurn";

		//盤面情報の配列に格納する値
		final String NOT_FILL="_";

		//テスト用盤面情報作成
		String[][] testLocation=new String [3][3];

		for(int i=0;i<testLocation.length;i++){
			for(int j=0;j<testLocation[i].length;j++){
				testLocation[i][j]=NOT_FILL;
			}

			//期待されるJSONObjectの返り値
			JSONObject expectGameInfo=new JSONObject();

			expectGameInfo.accumulate("location", testLocation);
			expectGameInfo.accumulate("event", EVENT);

			//テスト対象クラスのJSONObjectを取得
			JSONObject actualGameInfo=TurnAdmin.informTurn(testLocation);

			//JSONObjectを比較
			JSONAssert.assertEquals (expectGameInfo,actualGameInfo);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getTurnTest() throws NoSuchFieldException, SecurityException,
	IllegalArgumentException, IllegalAccessException{
		//手番管理クラスをインスタンス化
		TurnAdmin ta=new TurnAdmin(this.clb);

		//リフレクション、フィールド変数turnを書き換え
		Class<TurnAdmin> cta=(Class<TurnAdmin>) ta.getClass();
		Field taTurn=cta.getDeclaredField("turn");

		//アクセス権限付与
		taTurn.setAccessible(true);

		//期待ターン数
		int expectTurn=3;

		//turn数を3に書き換え
		taTurn.set(ta, expectTurn);

		//テスト対象のクラスと比較
		assertThat(expectTurn,is(ta.getTurn()));
	}
}
