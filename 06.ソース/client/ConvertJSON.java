package client;

import java.util.ArrayList;
import java.util.List;

import brain.BrainBean;
import net.sf.json.JSONObject;

/**
 * JSONObjectに変換したりJSONObjectからデータを取り出したりするクラスです.
 * @author hatsugai
 *
 */

public class ConvertJSON {
	/**
	 * ロジック情報をJSONObjectに変換します
	 * @param IPAdress 動作させているマシンのIPアドレスです。サーバー側でクライアントにデータを送るときに使用します
	 * @return ロジック情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONF(List<BrainBean> logList,String IPAdress){
		JSONObject obj = new JSONObject();
		for(BrainBean brb:logList){
			obj.put("logicName", brb.getLogicName());
			obj.put("logicVersion", brb.getLogicVersion());
			obj.put("writer", brb.getWriter());
			obj.put("IPAdress", IPAdress);
		}
			return obj;
	}
	/**
	 * JSONObjectからデータを取り出してBattleInfoBeanに格納します
	 * @param rcvmsg サーバーから送られてきたJSONObjectです
	 */
	public List<BattleInfoBean> convertFromJSON(JSONObject rcvmsg){
		List<BattleInfoBean> bl = new ArrayList<BattleInfoBean>();
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
		bl.add(bib);
		return bl;
	}
	/**
	 * 指し手情報をJSONObjectに変換します
	 * @return 指し手情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONS(List<BattleInfoBean> bibn){
		JSONObject obj = new JSONObject();
		//指し手情報をBattleInfoBeanから取り出して格納します。
		for(BattleInfoBean bib:bibn){
			obj.put("xAxis", bib.getxAxis());
			obj.put("yAxis", bib.getyAxis());
		}
		return obj;
	}

}
