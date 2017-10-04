package server;
/**
 * サーバー機能を実行するクラス
 * @author kanayama
 *
 */
public class Main {
	/**
	 * サーバー機能を実行するメソッド
	 * @param args
	 * @exception Exception
	 */
	public static void main(String[] args) {

		//試合管理クラスをインスタンス化
		BattleFlow ba=new BattleFlow();

		try{
			//試合管理メソッドを呼び出し
			ba.gameStart();

		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
