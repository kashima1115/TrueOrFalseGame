package client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import brain.Brain;

/**
 * 同じロジック同士で対戦した場合を再現しているテストコードです.。
 * ただし、勝敗条件のみ感知します。
 * また、引き分けを前提としたテストコードです。
 */
public class BrainCopyBattleTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		//ブレインのインスタンス化
		Brain bra = new Brain();

		//現在の空白を示す変数
		int notFill = 9;

		//盤面の変数（3*3)の宣言
		String[][] board = new String[3][3];

		//座標を格納する変数の宣言
		String locate;

		//boardに空白を示すハイフンの挿入
		board[0][0] = "-";
		board[1][0] = "-";
		board[2][0] = "-";
		board[0][1] = "-";
		board[1][1] = "-";
		board[2][1] = "-";
		board[0][2] = "-";
		board[1][2] = "-";
		board[2][2] = "-";

		//試合の再現
		while(notFill>0){
			//ブレインから座標を取得
			locate = bra.getLocation(board);

			//現在の空白から先攻後攻を判断、盤面に駒を置く
			if(notFill%2==1){
				board[Integer.parseInt(locate.substring(0,1))][Integer.parseInt(locate.substring(1))]
						="○";
			}else{
				board[Integer.parseInt(locate.substring(0,1))][Integer.parseInt(locate.substring(1))]
						="×";
			}
			//コンソールに駒を置いた後の盤面を表示する
			System.out.println("　|０|１|２|");
			System.out.println("０|"+board[0][0]+"|"+board[1][0]+"|"+board[2][0]+"|");
			System.out.println("１|"+board[0][1]+"|"+board[1][1]+"|"+board[2][1]+"|");
			System.out.println("２|"+board[0][2]+"|"+board[1][2]+"|"+board[2][2]+"|");
			//座標取得前の空白を表示する
			System.out.println(notFill);
			//勝負がついた場合breakする
			if(!board[0][0].contains("-")&&board[0][0].equals(board[1][0])&&board[0][0].equals(board[2][0])){
				break;
			}else if(!board[0][1].contains("-")&&board[0][1].equals(board[1][1])&&board[0][1].equals(board[2][1])){
				break;
			}else if(!board[0][2].contains("-")&&board[0][2].equals(board[1][2])&&board[0][2].equals(board[2][2])){
				break;
			}else if(!board[0][0].contains("-")&&board[0][0].equals(board[0][1])&&board[0][0].equals(board[0][2])){
				break;
			}else if(!board[1][0].contains("-")&&board[1][0].equals(board[1][1])&&board[1][0].equals(board[1][2])){
				break;
			}else if(!board[2][0].contains("-")&&board[2][0].equals(board[2][1])&&board[2][0].equals(board[2][2])){
				break;
			}else if(!board[0][0].contains("-")&&board[0][0].equals(board[1][1])&&board[0][0].equals(board[2][2])){
				break;
			}else if(!board[2][0].contains("-")&&board[2][0].equals(board[1][1])&&board[2][0].equals(board[0][2])){
				break;
			}else{
				//勝負続行の場合、メッセージを表示させて続ける
				System.out.println("no winner");
				System.out.println();
			}
			//空白を一つ減らす
			notFill--;
		}
		//引き分けを期待する
		assertEquals(0,notFill);

	}

}
