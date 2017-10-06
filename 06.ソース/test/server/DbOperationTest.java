package server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbOperationTest {
	private static DbOperation dbi;
	private static IDatabaseTester databaseTester;
	private static IDatabaseConnection dbcon;
	private static File file;



	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FileOutputStream out=null;
		dbi=new DbOperation();
		dbi.connect();

		try{
			//プロパティファイルから値を取得
			final ResourceBundle BUNDLE=ResourceBundle.getBundle("config");
			final String DRIVER=BUNDLE.getString("driverClassName");
			final String URL=BUNDLE.getString("url");
			final String USER=BUNDLE.getString("username");
			final String PASSWORD= BUNDLE.getString("password");

			//コネクション取得
			databaseTester=new JdbcDatabaseTester(DRIVER,URL,USER,PASSWORD);
			dbcon =databaseTester.getConnection();

			//データバックアップ
			QueryDataSet partialDataSet = new QueryDataSet(dbcon);
			partialDataSet.addTable("battle_result");
			partialDataSet.addTable("location");
			partialDataSet.addTable("logic");
			file=File.createTempFile("battle_result",".xml");
			out=new FileOutputStream(file);
			FlatXmlDataSet.write(partialDataSet,out);

			//データセット
			IDataSet dataSet=new FlatXmlDataSetBuilder().build(new File("dbSet/DbSet.xml"));
			databaseTester.setDataSet(dataSet);
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.onSetup();

			if(out!=null){
				out.flush();
			}

		}finally{
			if(out!=null){
				//クローズ処理
				out.close();
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileInputStream in=null;
		try{
			//データ復元処理
			in=new FileInputStream(file);
			IDataSet dataSet=new FlatXmlDataSetBuilder().build(in);
			databaseTester.setDataSet(dataSet);
			databaseTester.setTearDownOperation(DatabaseOperation.CLEAN_INSERT);
	        databaseTester.onTearDown();
		}finally{
			if(in!=null){
				//クローズ処理
				in.close();
			}
		}

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

		//テスト対象メソッドの実行
		int formerId=dbi.getFormerId();

		assertThat(formerId,is(50));
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
