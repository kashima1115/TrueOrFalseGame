package client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import brain.BrainBean;
import client.AccessBrain;
import client.BattleInfoBean;

/**
 * AccessBrainのテストコードです。座標入力は自動です。
 * @author hatsugai
 *
 */

public class AccessBrainTest2 {

	//コンソール入力用
	private InputStrings in = new InputStrings();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		System.setIn(in);
	}

	@After
	public void tearDown() throws Exception {
		System.setIn(null);
	}

	@Test
	public void testGetLogicInfo() {
		AccessBrain ab = new AccessBrain();
		ab.createBrain();
		//getLogicInfoを実行
		BrainBean bb = ab.getLogicInfo();
		//ロジック名が一致するかチェック
		assertEquals("manual",bb.getLogicName());
		System.out.println("ロジック名―OK!");
		//ロジックバージョンが一致するかチェック
		assertEquals("0.0",bb.getLogicVersion());
		System.out.println("ロジックバージョン―OK!");
		//作者が一致するかチェック
		assertEquals("初谷惇志",bb.getWriter());
		System.out.println("作者―OK!");
	}

	@Test
	public void testGetLocation() {
		//xAxisに入れる予定の変数
		String xax = "1";
		//yAxisに入れる予定の変数
		String yax = "2";
		//コンソールに入力する処理
		in.inputln(xax);
		in.inputln(yax);
		//getLocationを実行させるための準備
		BattleInfoBean bib = new BattleInfoBean();
		String[][] loc;
		loc = new String[3][3];
		//locには全部"-"を入れる
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		//getLocation実行（引数に盤面情報を渡す）
		new AccessBrain().createBrain();
		bib = new AccessBrain().getLocation(loc);
		//xAxis
		assertEquals(Integer.parseInt(xax),bib.getxAxis());
		System.out.println("xAxis―OK!");
		//yAxis
		assertEquals(Integer.parseInt(yax),bib.getyAxis());
		System.out.println("yAxis―OK!");
		System.out.println("getLocation―OK!");
	}

}
//コンソール入力用
class InputStrings extends InputStream{
	private StringBuilder buffer = new StringBuilder();
	private static String crlf = System.getProperty("line.separator");//改行

	public void inputln(String str){
		buffer.append(str).append(crlf);
	}

	@Override
	public int read() throws IOException{
		if(buffer.length() == 0){
			return -1;
		}
		int result = buffer.charAt(0);
		buffer.deleteCharAt(0);
		return result;
	}
}
