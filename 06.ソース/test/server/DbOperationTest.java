package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbOperationTest {
	private static DbOperation dbi;
	// プロファイル(パラメータ)読込み定義
	// プロパティファイルバンドル
	private static final ResourceBundle bundle=ResourceBundle.getBundle("config");

	// パラメータ取得
	private static final String driver=bundle.getString("driverClassName");
	private static final String url=bundle.getString("url");
	private static final String user=bundle.getString("username");
	private static final String password= bundle.getString("password");

	//コネクション
	private static Connection testCon=null;

	//プリペアドステートメント
	private static PreparedStatement pStateBattleResultInsert=null;
	private static PreparedStatement pStateBattleResultDelete=null;
	private static PreparedStatement pStateBattleResultSelect=null;
	private static PreparedStatement pStateLogicInsert=null;
	private static PreparedStatement pStateLogicDelete=null;
	private static PreparedStatement pStateLogicSelect=null;
	private static PreparedStatement pStateLocationInsertCheckSelect=null;
	private static PreparedStatement pStateLocationDeleteCheckSelect=null;
	private static PreparedStatement pStateLocationInsert=null;
	private static PreparedStatement pStateLocationDelete=null;
	private static PreparedStatement pStateLocationCorrectMaxIdSelect=null;
	private static PreparedStatement pStateLogicCorrectMaxIdSelect=null;
	private static PreparedStatement pStateLocationCorrectMaxIdAlter=null;
	private static PreparedStatement pStateLogicCorrectMaxIdAlter=null;

	//SQL文
	private static final String BATTLE_RESULT_INSERT_SQL="insert into battle_result value "+
			"(?,?,?,?,?,?,?)";

	private static final String BATTLE_RESULT_DELITE_SQL="delete from battle_result where "
			+"battle_id=? or battle_id=?";

	private static final String BATTLE_RESULT_SELECT_SQL="select count(*) from battle_result where "+
	"battle_id=? and logic_id=? and result=? and date=? and start_time=? and end_time=? and first_second=?";

	private static final String LOGIC_INSERT_SQL="insert into logic value "+
			"(?,?,?,?)";

	private static final String LOGIC_DELITE_SQL="delete from logic where "+
			"logic_name=? and logic_writer=? and logic_ver=?";

	private static final String LOGIC_SELECT_SQL="select count(*) from logic where "+
			"logic_name=? and logic_writer=? and logic_ver=?";

	private static final String LOGIC_SELECT_MAX_ID_SQL="select ifnull(max(logic_id),0) from logic";

	private static final String LOGIC_ALTER_MAX_ID_SQL="alter table logic auto_increment=?";

	private static final String LOCATION_DELITE_SQL="delete from location where battle_id=? and logic_id=?";

	private static final String LOCATION_INSERT_SQL="insert into location "+
			"(battle_id,logic_id,location_x,location_y,turn,play_start,play_end) "+
			"value (?,?,?,?,?,?,?)";

	private static final String LOCATION_INSERT_CHECK_SELECT_SQL="select count(*) from location where "+
			"battle_id=? and logic_id=? and location_x=? and location_y=? and turn=? and "+
			"play_start=? and play_end=?";

	private static final String LOCATION_DELITE_CHECK_SELECT_SQL="select count(*) from location where "+
			"battle_id=?";

	private static final String LOCATION_SELECT_MAX_ID_SQL="select ifnull(max(location_id),0) from location";

	private static final String LOCATION_ALTER_MAX_ID_SQL="alter table location auto_increment=?";


	//登録期待値
	private static final int EXPECT_BATTLE_ID=1100;
	private static final int EXPECT_LOGIC_ID=100;
	private static final int NOT_EXPECT_LOGIC_ID=99;
	private static LogicInfoBean testLib;
	private static LogicInfoBean testNotRecordLib;
	private static LocationInfoBean testLcib;

	//auto_increment
	private static int logicAutoIncrement=0;
	private static int locationAutoIncrement=0;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//DbOperationクラスDB接続
		dbi=new DbOperation();
		dbi.connect();

		//DB接続
		createConection();

		//logic_idとlocation_idの最大値を取得
		getMaxIdToCorrectAutoIncrement();

		//テスト用ロジック情報格納Bean作成
		beanSetter();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//auto_incrementの値を復元
		correctAutoIncrement();

		//DB切断
		closeConnection();

		//DbOperationクラスDB切断
		dbi.disconnect();
	}

	@Before
	public void setUp() throws Exception {

		//テストデータDB投入
		insertTestDataToDb();

	}

	@After
	public void tearDown() throws Exception {
		//テストデータ削除
		deleteTestDataFromDb();


	}

	@Test
	public void testHaveLogicInsert() throws SQLException {

		ResultSet rset=null;

		//テスト対象メソッド実行
		dbi.insertLogic(testLib);

		try{

			if(pStateLogicSelect==null){
				//プリコンパイル
				pStateLogicSelect=testCon.prepareStatement(LOGIC_SELECT_SQL);
			}


			//検索条件セット
			pStateLogicSelect.setString(1, testLib.getLogicName());
			pStateLogicSelect.setString(2, testLib.getWriter());
			pStateLogicSelect.setString(3, testLib.getVersion());

			//SQL実行
			rset=pStateLogicSelect.executeQuery();

			//値取得
			int enrolled=0;

			while(rset.next()){
				enrolled=rset.getInt("count(*)");
			}

			//比較
			assertThat(enrolled,is(1));

		}finally{
			if(rset!=null){
				rset.close();
			}
		}
	}

	@Test
	public void testNotHaveLogicInsert() throws SQLException {
		ResultSet rset=null;

		//テスト対象メソッド実行
		dbi.insertLogic(testNotRecordLib);

		try{

			if(pStateLogicSelect==null){
				//プリコンパイル
				pStateLogicSelect=testCon.prepareStatement(LOGIC_SELECT_SQL);
			}

			//検索条件セット
			pStateLogicSelect.setString(1, testNotRecordLib.getLogicName());
			pStateLogicSelect.setString(2, testNotRecordLib.getWriter());
			pStateLogicSelect.setString(3, testNotRecordLib.getVersion());

			//SQL実行
			rset=pStateLogicSelect.executeQuery();

			//値取得
			int enrolled=0;

			while(rset.next()){
				enrolled=rset.getInt("count(*)");
			}

			//比較
			assertThat(enrolled,is(1));
		}finally{
			if(rset!=null){
				rset.close();
			}
		}
	}

	@Test
	public void testLocationInsert() throws SQLException {
		ResultSet rset=null;

		//テスト対象メソッド実行
		dbi.insertLocation(testLcib);

		try{

			if(pStateLocationInsertCheckSelect==null){
				//プリコンパイル
				pStateLocationInsertCheckSelect=testCon.prepareStatement(LOCATION_INSERT_CHECK_SELECT_SQL);
			}


			//検索条件セット
			pStateLocationInsertCheckSelect.setInt(1, testLcib.getBattleId());
			pStateLocationInsertCheckSelect.setInt(2, testLcib.getLogicId());
			pStateLocationInsertCheckSelect.setInt(3, testLcib.getLocationX());
			pStateLocationInsertCheckSelect.setInt(4, testLcib.getLocationY());
			pStateLocationInsertCheckSelect.setInt(5, testLcib.getTurn());
			pStateLocationInsertCheckSelect.setString(6, testLcib.getPlayStart());
			pStateLocationInsertCheckSelect.setString(7, testLcib.getPlayEnd());

			//SQL実行
			rset=pStateLocationInsertCheckSelect.executeQuery();

			//値取得
			int insertRecord=0;

			while(rset.next()){
				insertRecord=rset.getInt("count(*)");
			}

			//比較
			assertThat(insertRecord,is(1));

		}finally{
			if(rset!=null){
				rset.close();
			}
		}
	}

	@Test
	public void testGetFormerId() throws SQLException {

		//テスト対象メソッドの実行
		int formerId=dbi.getFormerId();

		//DB内のbattle_idの最大値を取得できているか比較
		assertThat(formerId,is(EXPECT_BATTLE_ID));
	}

	@Test
	public void testResultInsert() throws SQLException {
		final int BATTLE_ID=1000;
		final String START_TIME="10:10:10";
		final String END_TIME="10:10:11";
		final String RESULT="win";
		final int LOGIC_ID=2000;
		final String START_DATE="2017-11-11";
		final String FIRST_SECOND="first";

		ResultSet rset=null;

		//対象テストメソッド実行
		dbi.insertResult(BATTLE_ID, START_TIME, END_TIME, RESULT, LOGIC_ID, START_DATE, FIRST_SECOND);

		try{

			if(pStateBattleResultSelect==null){
				//プリコンパイル
				pStateBattleResultSelect=testCon.prepareStatement(BATTLE_RESULT_SELECT_SQL);
			}

			//検索条件セット
			pStateBattleResultSelect.setInt(1, BATTLE_ID);
			pStateBattleResultSelect.setInt(2,LOGIC_ID);
			pStateBattleResultSelect.setString(3,RESULT);
			pStateBattleResultSelect.setString(4,START_DATE);
			pStateBattleResultSelect.setString(5, START_TIME);
			pStateBattleResultSelect.setString(6,END_TIME);
			pStateBattleResultSelect.setString(7,FIRST_SECOND);

			//SQL実行
			rset=pStateBattleResultSelect.executeQuery();

			int enrolled=0;

			//値取得
			while(rset.next()){
				enrolled=rset.getInt("count(*)");
			}

			assertThat(enrolled,is(1));

		}finally{
			if(rset!=null){
				rset.close();
			}
		}

	}

	@Test
	public void testGetLogicId() throws SQLException {
		//DB内の渡した情報を元に正しいlogic_id取得できているか確認
		assertThat(dbi.getLogicId(testLib),is(EXPECT_LOGIC_ID));
	}

	@Test
	public void testDeleteLocation() throws SQLException{
		ResultSet rset=null;

		try{
			//削除するテストデータを挿入
			if(pStateLocationInsert==null){
				//プリコンパイル
				pStateLocationInsert=testCon.prepareStatement(LOCATION_INSERT_SQL);
			}
			//挿入するデータ
			pStateLocationInsert.setInt(1, testLcib.getBattleId());
			pStateLocationInsert.setInt(2, testLcib.getLogicId());
			pStateLocationInsert.setInt(3, testLcib.getLocationX());
			pStateLocationInsert.setInt(4, testLcib.getLocationY());
			pStateLocationInsert.setInt(5, testLcib.getTurn());
			pStateLocationInsert.setString(6,testLcib.getPlayStart());
			pStateLocationInsert.setString(7,testLcib.getPlayEnd());

			//SQL実行
			pStateLocationInsert.executeUpdate();

			//コミット
			testCon.commit();

			//テスト対象メソッド実行
			dbi.deleteLocation(testLcib.getBattleId());

			//レコードが削除されたか否かをカウント
			if(pStateLocationDeleteCheckSelect==null){
				pStateLocationDeleteCheckSelect=testCon.prepareStatement(LOCATION_DELITE_CHECK_SELECT_SQL);
			}

			//削除条件指定
			pStateLocationDeleteCheckSelect.setInt(1, testLcib.getBattleId());

			//SQL実行
			rset=pStateLocationDeleteCheckSelect.executeQuery();

			//値取得
			int record=1;

			while(rset.next()){
				record=rset.getInt("count(*)");
			}

			assertThat(record,is(0));

		}finally{
			if(rset!=null){
				rset.close();
			}
		}

	}

	/**
	 * DB接続メソッド
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void createConection() throws ClassNotFoundException, SQLException{
		// JDBCドライバロード
		Class.forName(driver);

		//DB接続
		testCon= DriverManager.getConnection(url,user,password);

		//自動コミットオフ
		testCon.setAutoCommit(false);
	}

	/**
	 * DB切断メソッド
	 * @throws SQLException
	 */
	private static void closeConnection() throws SQLException{
		if(testCon!=null){
			testCon.close();
		}

		if(pStateBattleResultInsert!=null){
			pStateBattleResultInsert.close();
		}

		if(pStateBattleResultDelete!=null){
			pStateBattleResultDelete.close();
		}

		if(pStateBattleResultSelect!=null){
			pStateBattleResultSelect.close();
		}

		if(pStateLogicInsert!=null){
			pStateLogicInsert.close();
		}

		if(pStateLogicDelete!=null){
			pStateLogicDelete.close();
		}

		if(pStateLogicSelect!=null){
			pStateLogicSelect.close();
		}

		if(pStateLocationInsertCheckSelect!=null){
			pStateLocationInsertCheckSelect.close();
		}

		if(pStateLocationDeleteCheckSelect!=null){
			pStateLocationDeleteCheckSelect.close();
		}

		if(pStateLocationInsert!=null){
			pStateLocationInsert.close();
		}

		if(pStateLocationDelete!=null){
			pStateLocationDelete.close();
		}

		if(pStateLocationCorrectMaxIdSelect!=null){
			pStateLocationCorrectMaxIdSelect.close();
		}

		if(pStateLocationCorrectMaxIdAlter!=null){
			pStateLocationCorrectMaxIdAlter.close();
		}

		if(pStateLogicCorrectMaxIdSelect!=null){
			pStateLogicCorrectMaxIdSelect.close();
		}

		if(pStateLogicCorrectMaxIdAlter!=null){
			pStateLogicCorrectMaxIdAlter.close();
		}
	}

	private static void insertTestDataToDb() throws SQLException{

		try{

			//battle_resultテーブルのプリコンパイル
			pStateBattleResultInsert=testCon.prepareStatement(BATTLE_RESULT_INSERT_SQL);

			//テストデータセット(logic_id=99)
			pStateBattleResultInsert.setInt(1,EXPECT_BATTLE_ID);
			pStateBattleResultInsert.setInt(2, NOT_EXPECT_LOGIC_ID);
			pStateBattleResultInsert.setString(3, "win");
			pStateBattleResultInsert.setString(4, "1111-11-11");
			pStateBattleResultInsert.setString(5, "11:11:11");
			pStateBattleResultInsert.setString(6, "11:11:12");
			pStateBattleResultInsert.setString(7, "first");

			//SQL実行
			pStateBattleResultInsert.executeUpdate();

			//テストデータセット(logic_id=100)
			pStateBattleResultInsert.setInt(2, EXPECT_LOGIC_ID);
			pStateBattleResultInsert.setString(3, "lose");
			pStateBattleResultInsert.setString(7, "second");

			//SQL実行
			pStateBattleResultInsert.executeUpdate();

			//logicテーブルのプリコンパイル
			pStateLogicInsert=testCon.prepareStatement(LOGIC_INSERT_SQL);

			//テストデータセット
			pStateLogicInsert.setInt(1, EXPECT_LOGIC_ID);
			pStateLogicInsert.setString(2, testLib.getLogicName());
			pStateLogicInsert.setString(3,testLib.getWriter());
			pStateLogicInsert.setString(4,testLib.getVersion());

			//SQL実行
			pStateLogicInsert.executeUpdate();

			//コミット
			testCon.commit();

		}catch(SQLException e){
			e.printStackTrace();

			if(testCon!=null){
				testCon.rollback();
			}
		}
	}
	/**
	 * テストデータ削除
	 * @throws SQLException
	 */
	private static void deleteTestDataFromDb() throws SQLException{
		try{
			if(pStateBattleResultDelete==null){
				//battle_resultテーブルレコード削除のプリコンパイル
				pStateBattleResultDelete=testCon.prepareStatement(BATTLE_RESULT_DELITE_SQL);
			}


			//削除条件セット
			pStateBattleResultDelete.setInt(1, EXPECT_BATTLE_ID);
			pStateBattleResultDelete.setInt(2, 1000);

			//SQL実行
			pStateBattleResultDelete.executeUpdate();

			if(pStateLogicDelete==null){
				//logicテーブルレコード削除のプリコンパイル
				pStateLogicDelete=testCon.prepareStatement(LOGIC_DELITE_SQL);
			}

			//削除条件セット(事前登録）
			pStateLogicDelete.setString(1, testLib.getLogicName());
			pStateLogicDelete.setString(2, testLib.getWriter());
			pStateLogicDelete.setString(3, testLib.getVersion());

			//SQL実行
			pStateLogicDelete.executeUpdate();

			//削除条件セット
			pStateLogicDelete.setString(1, testNotRecordLib.getLogicName());
			pStateLogicDelete.setString(2, testNotRecordLib.getWriter());
			pStateLogicDelete.setString(3, testNotRecordLib.getVersion());

			//SQL実行
			pStateLogicDelete.executeUpdate();

			if(pStateLocationDelete==null){
				//locationテーブルレコード削除プリコンパイル
				pStateLocationDelete=testCon.prepareStatement(LOCATION_DELITE_SQL);
			}

			//削除条件セット
			pStateLocationDelete.setInt(1, testLcib.getBattleId());
			pStateLocationDelete.setInt(2, testLcib.getLogicId());

			//SQL実行
			pStateLocationDelete.executeUpdate();

			//コミット
			testCon.commit();

		}catch(SQLException e){
			e.printStackTrace();

			if(testCon!=null){
				testCon.rollback();
			}
		}
	}

	private static void beanSetter(){
		//ロジック情報Bean作成(事前登録)
		testLib=new LogicInfoBean();

		testLib.setLogicName("ai");
		testLib.setWriter("kanayama");
		testLib.setVersion("0.3");

		//ロジック情報Bean作成(無登録)
		testNotRecordLib=new LogicInfoBean();

		testNotRecordLib.setLogicName("empty");
		testNotRecordLib.setWriter("kanayama");
		testNotRecordLib.setVersion("0.4");

		//指し手情報Bean作成
		testLcib=new LocationInfoBean();

		testLcib.setBattleId(1000);
		testLcib.setLogicId(2000);
		testLcib.setLocationX(0);
		testLcib.setLocationY(1);
		testLcib.setTurn(1);
		testLcib.setPlayStart("10:10:10");
		testLcib.setPlayEnd("10:10:11");


	}

	private static void correctAutoIncrement() throws SQLException{

		//location_idのauto_incrementの値を復元
		pStateLocationCorrectMaxIdAlter=testCon.prepareStatement(LOCATION_ALTER_MAX_ID_SQL);

		pStateLocationCorrectMaxIdAlter.setInt(1,++locationAutoIncrement);

		pStateLocationCorrectMaxIdAlter.executeUpdate();

		//logic_idのauto_incrementの値を復元
		pStateLogicCorrectMaxIdAlter=testCon.prepareStatement(LOGIC_ALTER_MAX_ID_SQL);

		pStateLogicCorrectMaxIdAlter.setInt(1,++logicAutoIncrement);

		pStateLogicCorrectMaxIdAlter.executeUpdate();
	}

	private static void getMaxIdToCorrectAutoIncrement() throws SQLException{
		ResultSet rsetLocation=null;
		ResultSet rsetLogic=null;

		try{
			//location_idの最大値を取得する
			pStateLocationCorrectMaxIdSelect=testCon.prepareStatement(LOCATION_SELECT_MAX_ID_SQL);

			rsetLocation=pStateLocationCorrectMaxIdSelect.executeQuery();

			while(rsetLocation.next()){

				locationAutoIncrement=rsetLocation.getInt("ifnull(max(location_id),0)");
			}

			//logic_idの最大値を取得する
			pStateLogicCorrectMaxIdSelect=testCon.prepareStatement(LOGIC_SELECT_MAX_ID_SQL);

			rsetLogic=pStateLogicCorrectMaxIdSelect.executeQuery();

			while(rsetLogic.next()){
				logicAutoIncrement=rsetLogic.getInt("ifnull(max(logic_id),0)");
			}

		}finally{
			//クローズ処理
			if(rsetLocation!=null){
				rsetLocation.close();
			}

			if(rsetLogic!=null){
				rsetLogic.close();
			}
		}

	}
}
