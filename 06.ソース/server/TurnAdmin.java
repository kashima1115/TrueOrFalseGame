package server;

import net.sf.json.JSONObject;

/**
 * 手番管理クラス
 * @author kanayama
 *
 */
class TurnAdmin {
	private int turn;
	private static final int FIRST_PLAYER=1;
	private static final int SECOND_PLAYER=0;
	private static final String INFORM_EVENT="YourTurn";
	private ClientLogicBean clb;

	/**
	 * コンストラクタ
	 * ターン管理、クライアント区別、イベント情報通達定数を初期化
	 * @return なし
	 */
	TurnAdmin(ClientLogicBean clb){
		turn=1;
		this.clb=clb;
	}

	/**
	 * 先攻・後攻決定処理
	 * @return 先手のクライアントのロジックキー
	 */
	String decideFirst(){

		//先に受信したメッセージを送信したクライアントが先攻
		return this.clb.getFirstLogic();
	}

	/**
	 * 手番判定処理
	 * @return 次手番のクライアントのロジックキー
	 */
	String judgeTurn(){
		this.turn++;
		String nextPlayer="";

		if(this.turn%2==FIRST_PLAYER){
			nextPlayer=this.clb.getFirstLogic();
		}else if(this.turn%2==SECOND_PLAYER){
			nextPlayer=this.clb.getSecondLogic();
		}

		//次手番のプレイヤーのIPアドレスを返す
		return nextPlayer;
	}

	/**
	 * 手番通知処理（通知オブジェクト作成）
	 * @param location 最新の状態の盤面
	 * @return クライアントに送信するJSONObject
	 */
	static JSONObject informTurn(String[][] location){
		JSONObject gameInfo=new JSONObject();

		gameInfo.accumulate("event", INFORM_EVENT);
		gameInfo.accumulate("location", location);

		return gameInfo;
	}

	/**
	 * 試合の経過ターン数を返すメソッド
	 * @return 経過ターン数
	 */
	int getTurn(){
		return this.turn;
	}
}
