package client;

import brain.BrainBean;
import net.sf.json.JSONObject;

/**
 * JSONObjectに変換したりJSONObjectからデータを取り出したりするクラスです.
 * @author hatsugai
 *
 */

public class ConvertJSON {
	JSONObject obj;
	/**
	 * ロジック情報をJSONObjectに変換します
	 * @param IPAdress 動作させているマシンのIPアドレスです。サーバー側でクライアントにデータを送るときに使用します
	 * @return ロジック情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONF(BrainBean logic,String IPAdress){
		obj.put("logicName", logic.getLogicName());
		obj.put("logicVersion", logic.getLogicVersion());
		obj.put("logicWriter", logic.getWriter());
		obj.put("address", IPAdress);
		obj.put("event", "ready");

		return obj;
	}
	/**
	 * JSONObjectからデータを取り出してBattleInfoBeanに格納します
	 * @param rcvmsg サーバーから送られてきたJSONObjectです
	 */
	public BattleInfoBean convertFromJSON(JSONObject rcvmsg){
		BattleInfoBean bib = new BattleInfoBean();
		//エラー条件や終了条件がメッセージ中に含まれていた場合にeventにその旨を格納します。
		if(rcvmsg.containsKey("error[]")){
			bib.setEvent("error");
			bib.setError((String[])rcvmsg.get("error[]"));
		}else if(rcvmsg.getString("event").equals("win")||rcvmsg.getString("event").equals("lose")||rcvmsg.getString("event").equals("draw")){
			bib.setEvent(rcvmsg.getString("event"));
		//問題が無ければ盤面の情報を格納します。
		}else{
			bib.setEvent(rcvmsg.getString("event"));
			bib.setLocation((String[][])rcvmsg.get("location"));
		}
		return bib;
	}
	/**
	 * 指し手情報をJSONObjectに変換します
	 * @return 指し手情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONS(BattleInfoBean bib){
		//指し手情報をBattleInfoBeanから取り出して格納します。
		obj.put("xAxis", bib.getxAxis());
		obj.put("yAxis", bib.getyAxis());
		obj.put("event", "TurnEnd");

		return obj;
	}

}
