package server;

import net.sf.json.JSONObject;

/**
 * エラー通知オブジェクトを作成するクラス
 * @author kanayama
 *
 */
class InformError {
	//発生したエラーを格納する配列
	private String error[];

	//各種エラー
	private final String OVERSUBSCRIBED;
	private final String SAME_LOGIC;
	private final String NOT_BLANK;
	private final String NOT_EXPECT_EVENT;

	//エラー数のカウント
	private int errorAmount;

	/**
	 * コンストラクタ
	 */
	InformError(){
		error=new String[4];
		OVERSUBSCRIBED="oversubscribed";
		SAME_LOGIC="sameLogic";
		NOT_BLANK="notBlank";
		NOT_EXPECT_EVENT="notExpectEvent";
		errorAmount=0;
	}

	/**
	 *サーバーに３つ以上のアクセスがあった際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	JSONObject oversubscribedError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列にエラーメッセージを格納
		this.error[this.errorAmount]=OVERSUBSCRIBED;

		//配列をJSONObjectに格納
		gameInfo.accumulate("error[]", error);

		//エラー数を増加させる
		this.errorAmount++;

		return gameInfo;
	}

	/**
	 * サーバーに同名ロジックのアクセスが確認された際、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	JSONObject sameLogicError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列にエラーメッセージを格納
		this.error[this.errorAmount]=SAME_LOGIC;

		//配列をJSONObjectに格納
		gameInfo.accumulate("error[]", error);

		//エラー数を増加させる
		this.errorAmount++;

		return gameInfo;
	}

	/**
	 * 指し手情報がゲームのルール違反をしたい際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	JSONObject ruleError(){
		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列にエラーメッセージを格納
		this.error[this.errorAmount]=NOT_BLANK;

		//配列をJSONObjectに格納
		gameInfo.accumulate("error[]", error);

		//エラー数を増加させる
		this.errorAmount++;

		return gameInfo;
	}

	/**
	 * クライアントから受信したメッセージのイベント情報が期待値ではなかった際に、処理が行われる
	 * @return  エラーメッセージの入ったJSONObject
	 */
	JSONObject notExpectEventError(){

		//JSONObject生成
		JSONObject gameInfo=new JSONObject();

		//配列にエラーメッセージを格納
		this.error[this.errorAmount]=NOT_EXPECT_EVENT;

		//配列をJSONObjectに格納
		gameInfo.accumulate("error[]", error);

		//エラー数を増加させる
		this.errorAmount++;

		return gameInfo;
	}
}
