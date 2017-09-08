package server;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JudgeMatchTest {
	//盤面情報の配列に格納する値
	private final String NOT_FILL="_";
	private final String FIRST="○";
	String[][] testLocation;

	//盤面パターン
	private final int pattern=2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//テスト用盤面情報作成
		this.testLocation=new String [3][3];

		switch(pattern){
			//継戦の場合
			case 0:
				continueLocation();
			break;
			//X＝0の列で勝利条件達成
			case 1:
				winLineX0();
			break;
			//X=1の列で勝利条件
			case 2:
				winLineX1();
			break;
			//X=2の列で勝利条件
			case 3:
				winLineX2();
			break;
			//Y=0の列で勝利条件
			case 4:
				winLineY0();
			break;
			//Y=1の列で勝利条件
			case 5:
				winLineY1();
			break;
		}


	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRuleJudge() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetLocationInfo() {
		fail("まだ実装されていません");
	}

	@Test
	public void testBattleJudge() {
		//返り値の候補
		final String WIN="win";
		final String DRAW="draw";
		final String CONTINUE="continue";

		//返り値候補を格納する変数
		String result=null;

		if(this.pattern==0){
			result=CONTINUE;
		}else if(this.pattern>0){
			result=WIN;
		}else{
			result=DRAW;
		}

		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//勝敗判定の返り値を比較
		assertThat(result,is(jm.battleJudge(this.testLocation)));
	}

	@Test
	public void testInformResult() {
		fail("まだ実装されていません");
	}
/**
 * 継戦状態の盤面を作成
 */
	private void continueLocation(){

		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){
				this.testLocation[x][y]=NOT_FILL;
			}
		}
	}
/**
 * X＝0の列で勝利条件達成
 */
	private void winLineX0(){
		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){

				if(x==0){
					this.testLocation[x][y]=FIRST;
				}else{
					this.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
/**
 * X=1の列で勝利条件
 */
	private void winLineX1(){
		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){

				if(x==1){
					this.testLocation[x][y]=FIRST;
				}else{
					this.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}

	private void winLineX2(){
		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){

				if(x==2){
					this.testLocation[x][y]=FIRST;
				}else{
					this.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}

	private void winLineY0(){
		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){

				if(y==0){
					this.testLocation[x][y]=FIRST;
				}else{
					this.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}

	private void winLineY1(){
		for(int x=0;x<this.testLocation.length;x++){
			for(int y=0;y<this.testLocation[x].length;y++){

				if(y==1){
					this.testLocation[x][y]=FIRST;
				}else{
					this.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
}
