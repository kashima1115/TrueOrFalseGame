package server;

import net.sf.json.JSONObject;

/**
 * 手番管理クラス
 * @author kanayama
 *
 */
public class TurnAdmin {
	private static int turn;
	private final int FIRST_PLAYER;
	private final int SECOND_PLAYER;
	private final String INFORM_EVENT;
	private ClientLogicBean clb;

	/**
	 * コンストラクタ
	 * ターン管理、クライアント区別、イベント情報通達定数を初期化
	 * @return なし
	 */
	TurnAdmin(ClientLogicBean clb){
		turn=1;
		FIRST_PLAYER=1;
		SECOND_PLAYER=0;
		INFORM_EVENT="YourTurn";
		this.clb=clb;
	}

	/**
	 * 先攻・後攻決定処理
	 * @param logicList ロジック情報を含むBean
	 * @return 先手のクライアントのIPアドレス
	 */
	public String decideFirst(){

		//先に受信したメッセージを送信したクライアントが先攻
		return this.clb.getFirstLogic();
	}

	/**
	 * 手番判定処理
	 * @param logicList ロジック情報を含むBean
	 * @return 次手番のクライアントのIPアドレス
	 */
	public String judgeTurn(){
		TurnAdmin.turn++;
		String nextPlayer="";

		if(TurnAdmin.turn%2==FIRST_PLAYER){
			nextPlayer=this.clb.getFirstLogic();
		}else if(TurnAdmin.turn%2==SECOND_PLAYER){
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
	public JSONObject informTurn(String[][] location){
		JSONObject gameInfo=new JSONObject();

		gameInfo.accumulate("event", INFORM_EVENT);
		gameInfo.accumulate("location", location);

		return gameInfo;
	}

	/**
	 * 試合の経過ターン数を返すメソッド
	 * @return 経過ターン数
	 */
	public int getTurn(){
		return TurnAdmin.turn;
	}
}
