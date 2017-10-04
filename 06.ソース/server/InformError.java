package server;

import net.sf.json.JSONObject;

/**
 * エラー通知オブジェクトを作成するクラス
 * @author kanayama
 *
 */
class InformError {
	//各種エラー
	private static final String OVERSUBSCRIBED="oversubscribed";
	private static final String SAME_LOGIC="sameLogic";
	private static final String NOT_BLANK="notBlank";
	private static final String NOT_EXPECT_EVENT="notExpectEvent";


	/**
	 *サーバーに３つ以上のアクセスがあった際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	static JSONObject oversubscribedError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列をJSONObjectに格納
		gameInfo.accumulate("error", OVERSUBSCRIBED);

		return gameInfo;
	}

	/**
	 * サーバーに同名ロジックのアクセスが確認された際、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	static JSONObject sameLogicError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列をJSONObjectに格納
		gameInfo.accumulate("error", SAME_LOGIC);

		return gameInfo;
	}

	/**
	 * 指し手情報がゲームのルール違反をした際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	static JSONObject ruleError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列をJSONObjectに格納
		gameInfo.accumulate("error", NOT_BLANK);

		return gameInfo;
	}

	/**
	 * クライアントから受信したメッセージのイベント情報が期待値ではなかった際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	static JSONObject notExpectEventError(){

		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列をJSONObjectに格納
		gameInfo.accumulate("error", NOT_EXPECT_EVENT);

		return gameInfo;
	}
}
