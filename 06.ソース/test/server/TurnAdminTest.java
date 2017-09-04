package server;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TurnAdminTest {
	private Map<String,LogicInfoBean> logicMap;
	private ClientAddressBean cab;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//ロジック情報Map作成
		this.logicMap=new HashMap<String,LogicInfoBean>();

		//クライアントアドレス保持Beanをインスタンス化
		this.cab=new ClientAddressBean();

		for(int i=0;i<2;i++){
			LogicInfoBean lib=new LogicInfoBean();
			//beanに格納
			lib.setLogicName(Integer.toString(i));
			lib.setCreator(Integer.toString(i));
			lib.setVersion(Integer.toString(i));
			lib.setAddress(Integer.toString(i));

			//Listに格納
			this.logicMap.put(Integer.toString(i),lib);

			//クライアントアドレス保持Beanにアドレスをセット
			if(i==0){
				this.cab.setFirstAddress(Integer.toString(i));
			}else if(i==1){
				this.cab.setSecondAddress(Integer.toString(i));
			}
		}


	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void decideFirstTest() {
		//手番管理クラスをインスタンス化
		TurnAdmin ta=new TurnAdmin(cab);

		//先攻手番クライアントのアドレスを取得(早いもの順）
		String turnAdd=ta.decideFirst();

		//比較
		assertEquals(this.logicMap.get("0").getAddress(),turnAdd);
	}

	@Test
	public void judgeTurnTest() {
		//手番管理クラスをインスタンス化
		TurnAdmin ta=new TurnAdmin(cab);

		//次手番クライアントアドレス格納用アドレス
		String nextAdd=null;

		//次手番のクライアントを判定・取得
		for(int turn=1;turn<4;turn++){
			nextAdd=ta.judgeTurn();
		}

		//比較
		assertEquals(this.logicMap.get("1").getAddress(),nextAdd);
	}

	@Test
	public void informTurnTest() {
		fail("まだ実装されていません");
	}

	@Test
	public void getTurnTest() {
		fail("まだ実装されていません");
	}
}
