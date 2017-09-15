package client;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.json.JSONObject;

public class SequenceControlTest {
	static ConvertJSON cj = new ConvertJSON();
	BufferedReader br;
	static messageQueue.ActiveMQMessaging amq = new messageQueue.ActiveMQMessaging();
	JSONObject obj = new JSONObject();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMyTurn() {

		System.out.println("行番号(横)を数字で入力してエンターキーを押してください。");
		//行を入力
		br = new BufferedReader(new InputStreamReader(System.in));
		String row = console();
		System.out.println("列番号(縦)を数字で入力してエンターキーを押してください。");
		//列を入力
		String col = console();
		//行、列の順番にStringに変換
		String loc = row + col;

		BattleInfoBean bib = convertFromString(loc);

		send(bib);

	      assertEquals("{\"xAxis\":" + row + ",\"yAxis\":" + col + ",\"event\":\"TurnEnd\"}"
	    		  , cj.convertToJSONS(bib).toString());

	   }



	private BattleInfoBean convertFromString(String location){
		BattleInfoBean bib = new BattleInfoBean();
		bib.setxAxis(Integer.parseInt(location.substring(0,1)));
		bib.setyAxis(Integer.parseInt(location.substring(1)));
		return bib;
	}
	public String console(){
		String console = null;
		for(boolean f=false;f==false;){
			try {
				console = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			f=console.matches("[0-9０-９]{1}");
			if(f==false){
				System.out.println("数字を１文字入力してください。");
			}
		}
		return console;
	}
	public static void send(BattleInfoBean bib){
		//JSONに変換
		JSONObject jo2 = cj.convertToJSONS(bib);
		//ActiveMQを通してサーバープログラムに送信する
		amq.sendMessage(jo2);
	}

}
