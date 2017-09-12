package server;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import net.sf.json.JSONObject;

@RunWith(Theories.class)
public class JudgeMatchTest {
	//盤面情報の配列に格納する値
	private final String NOT_FILL="_";
	private final String FIRST="○";
	private final String SECOND="×";
	private static String[][] testLocation;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//テスト用盤面情報作成
		testLocation=new String [3][3];
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		int pattern=0;
		decideLocation(pattern);
	}

	@After
	public void tearDown() throws Exception {
	}

	@DataPoint
	public static final String WIN="win";

	@DataPoint
	public static String LOSE="lose";

	@DataPoint
	public static String DRAW="draw";

	@DataPoints
	public static int[] patternKey={0,1,2,3,4,5,6,7,8,9};

	@Test
	public void testRuleJudgeTrue() {
		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//JSONObject生成
		JSONObject testGameInfo=new JSONObject();
		testGameInfo.accumulate("xAxis", 0);
		testGameInfo.accumulate("yAxis", 0);

		boolean ruleJudge=jm.ruleJudge(9, JudgeMatchTest.testLocation, testGameInfo, 1, "1", "1", 1);

		//比較
		assertThat(ruleJudge,is(true));
	}

	@Test
	public void testRuleJudgeFalse() {
		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//JSONObject生成
		JSONObject testGameInfo=new JSONObject();
		testGameInfo.accumulate("xAxis", 0);
		testGameInfo.accumulate("yAxis", 0);

		//盤面情報変更
		decideLocation(1);

		boolean ruleJudge=jm.ruleJudge(9, JudgeMatchTest.testLocation, testGameInfo, 1, "1", "1", 1);

		//比較
		assertThat(ruleJudge,is(false));
	}

	@Test
	public void testGetLocationInfo() {
		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//JSONObject生成
		JSONObject testGameInfo=new JSONObject();
		testGameInfo.accumulate("xAxis", 0);
		testGameInfo.accumulate("yAxis", 0);

		jm.ruleJudge(9, JudgeMatchTest.testLocation, testGameInfo, 1, "1", "1", 1);

		//返すインスタンスを比較
		assertThat(jm.getLocationInfo(),instanceOf(LocationInfoBean.class));
	}

	@Theory
	public void testBattleJudge(int pattern) {
		//返り値の候補
		final String WIN="win";
		final String DRAW="draw";
		final String CONTINUE="continue";

		//返り値候補を格納する変数
		String result=null;

		//盤面状態を決定
		decideLocation(pattern);

		if(pattern==0){
			result=CONTINUE;
		}else if(pattern>0 && pattern<9){
			result=WIN;
		}else{
			result=DRAW;
		}

		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//勝敗判定の返り値を比較
		assertThat(jm.battleJudge(JudgeMatchTest.testLocation),is(result));
	}

	@Theory
	public void testInformResult(String result) {
		//テスト対象クラスのインスタンス化
		JudgeMatch jm=new JudgeMatch();

		//JSONObject作成
		JSONObject testGameInfo=new JSONObject();

		testGameInfo.accumulate("event", result);

		assertThat(jm.informResult(result),is(testGameInfo));
	}
/**
 * 継戦状態の盤面を作成
 */
	private void continueLocation(){

		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){
				JudgeMatchTest.testLocation[x][y]=NOT_FILL;
			}
		}
	}
/**
 * X＝0の列で勝利条件達成
 */
	private void winLineX0(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(x==0){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
/**
 * X=1の列で勝利条件
 */
	private void winLineX1(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(x==1){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
	/**
	 * X=2の列で勝利条件
	 */
	private void winLineX2(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(x==2){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
/**
 * Y=0の行で勝利条件
 */
	private void winLineY0(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(y==0){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
/**
 * Y=1の行で勝利条件
 */
	private void winLineY1(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(y==1){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
	/**
	 * Y=2の行で勝利条件
	 */
	private void winLineY2(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){

				if(y==2){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
	/**
	 * 右斜め上で勝利条件
	 */
	private void upperRightDiagonalLine(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){
				if(x==0 && y==2){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else if(x==1 && y==1){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else if(x==2 && y==0){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
	/**
	 * 右斜め下で勝利条件
	 */
	private void lowerRightDiagonal(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){
				if(x==0 && y==0){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else if(x==1 && y==1){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else if(x==2 && y==2){

						JudgeMatchTest.testLocation[x][y]=FIRST;

				}else{
					JudgeMatchTest.testLocation[x][y]=NOT_FILL;
				}
			}
		}
	}
	/**
	 * 勝利条件が達成されなかったとき
	 */
	private void noWinLine(){
		for(int x=0;x<JudgeMatchTest.testLocation.length;x++){
			for(int y=0;y<JudgeMatchTest.testLocation[x].length;y++){
				if(y==0 && x==0){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else if(y==0 && x==1){
					JudgeMatchTest.testLocation[x][y]=SECOND;
				}else if(y==0 && x==2){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else if(y==1 && x==0){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}else if(y==1 && x>0){
					JudgeMatchTest.testLocation[x][y]=SECOND;
				}else if(y==2 && x==0){
					JudgeMatchTest.testLocation[x][y]=SECOND;
				}else if(y==2 && x>0){
					JudgeMatchTest.testLocation[x][y]=FIRST;
				}
			}
		}
	}

	private void decideLocation(int pattern){
		//盤面情報の設定
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
					//Y=0の行で勝利条件
					case 4:
						winLineY0();
					break;
					//Y=1の行で勝利条件
					case 5:
						winLineY1();
					break;
					//Y=2の行で勝利条件
					case 6:
						winLineY2();
					break;
					//右斜め下で勝利条件
					case 7:
						upperRightDiagonalLine();
					break;
					//右斜め上で勝利条件
					case 8:
						lowerRightDiagonal();
					break;
					//勝利条件が達成されなかったとき
					default:
						noWinLine();
					break;
				}
	}
}
