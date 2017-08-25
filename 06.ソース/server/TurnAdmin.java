package server;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 手番管理クラス
 * @author kanayama
 *
 */
public class TurnAdmin {
	private int turn;
	private final int FIRST_PLAYER;
	private final int SECOND_PLAYER;
	private final String INFORM_EVENT;

	/**
	 * コンストラクタ
	 * ターン管理、クライアント区別、イベント情報通達定数を初期化
	 * @return なし
	 */
	TurnAdmin(){
		turn=1;
		FIRST_PLAYER=0;
		SECOND_PLAYER=1;
		INFORM_EVENT="YourTurn";
	}

	/**
	 * 先攻・後攻決定処理
	 * @param logicList ロジック情報を含むBean
	 * @return 先手のクライアントのIPアドレス
	 */
	public String decideFirst(List<LogicInfoBean> logicList){

		//先に受信したメッセージを送信したクライアントが先攻
		return logicList.get(FIRST_PLAYER).getAddress();
	}

	/**
	 * 手番判定処理
	 * @param logicList ロジック情報を含むBean
	 * @return 次手番のクライアントのIPアドレス
	 */
	public String judgeTurn(List<LogicInfoBean> logicList){
		this.turn++;
		String nextPlayerAdd="";

		if(this.turn%2==FIRST_PLAYER){
			nextPlayerAdd=logicList.get(FIRST_PLAYER).getAddress();
		}else if(this.turn%2==SECOND_PLAYER){
			nextPlayerAdd=logicList.get(SECOND_PLAYER).getAddress();
		}

		//次手番のプレイヤーのIPアドレスを返す
		return nextPlayerAdd;
	}

	/**
	 * 手番通知処理（通知オブジェクト作成）
	 * @param location 最新の状態の盤面
	 * @return クライアントに送信するJSONObject
	 */
	public JSONObject informTurn(String[][] location){
		JSONObject info=new JSONObject();
		JSONObject gameInfo=new JSONObject();

		gameInfo.accumulate("event", INFORM_EVENT);
		gameInfo.accumulate("location", location);

		info.accumulate("gameInfo", gameInfo);

		return info;
	}
}
