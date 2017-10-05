package brain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *ブレインのクラスです.
 * @author hatsugai
 */

public class Brain implements BrainControl{
	//ロジックの名前やバージョン、作者をここで定義します。
	private String logicName = "manual";
	private String logicVersion = "1.2";
	private String writer ="初谷惇志";

	BufferedReader br;

	public BrainBean logicInfo(){
		/**
		 * ロジックの情報（名前・版・作者）をBrainBeanに格納します。
		 */
		BrainBean bb = new BrainBean();
		bb.setLogicName(logicName);
		bb.setLogicVersion(logicVersion);
		bb.setWriter(writer);
		//BrainBeanを返します
		return bb;
	}

	public String getLocation(String[][] location){
		/**
		 * ロジックによって指し手を決定します。指し手の情報はStringに変換して返します
		 *
		 */
		String[][] board = location;
		int blank = 0;
		String turn = null;
		for(int x = 0;x<=2;x++){
			for(int y = 0;y<=2;y++){
				if("-".equals(board[x][y])){
					blank++;
				}
			}
		}
		if((blank%2)==1){
			turn = "○";
		}else{
			turn = "×";
		}
		System.out.println("あなたの駒は"+turn+"です。");
		//現在の盤面を表示
		System.out.println("　|０|１|２|");
		System.out.println("０|"+board[0][0]+"|"+board[1][0]+"|"+board[2][0]+"|");
		System.out.println("１|"+board[0][1]+"|"+board[1][1]+"|"+board[2][1]+"|");
		System.out.println("２|"+board[0][2]+"|"+board[1][2]+"|"+board[2][2]+"|");
		System.out.println("行番号(横)を数字で入力してエンターキーを押してください。");
		//行を入力
		br = new BufferedReader(new InputStreamReader(System.in));
		String row = console();
		System.out.println("列番号(縦)を数字で入力してエンターキーを押してください。");
		//列を入力
		String col = console();
		//行、列の順番にStringに変換
		String loc = row + col;
		return loc;
	}
/**
 * コンソールに入力した値を取得し、数字であるかどうかチェックします.
 * @return コンソールに入力した値をStringで返します.文字数は1文字を想定しています
 */
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

}
