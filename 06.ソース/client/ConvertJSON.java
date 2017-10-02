package client;

import brain.BrainBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSONObjectに変換したりJSONObjectからデータを取り出したりするクラスです.
 * @author hatsugai
 * @version 0.1
 */

public class ConvertJSON {
	private JSONObject locateObj = new JSONObject();
	private JSONArray jary = new JSONArray();
	private String[][]locary = new String[3][3];
	private BattleInfoBean bib = new BattleInfoBean();

	final private String READY = "ready";
	final private String TURN_END = "TurnEnd";
	/**
	 * ロジック情報をJSONObjectに変換します.
	 * @param logic brainのロジック情報が入ったBrainBeanです
	 * @param IPAdress 動作させているマシンのIPアドレスです。サーバー側でクライアントにデータを送るときに使用します
	 * @return ロジック情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONF(BrainBean logic,String IPAdress){
		JSONObject logicObj = new JSONObject();
		logicObj.put("logicName", logic.getLogicName());
		logicObj.put("logicVersion", logic.getLogicVersion());
		logicObj.put("logicWriter", logic.getWriter());
		logicObj.put("address", IPAdress);
		logicObj.put("event", READY);

		return logicObj;
	}
	/**
	 * JSONObjectからデータを取り出してBattleInfoBeanに格納します.
	 * @param rcvmsg サーバーから送られてきたJSONObjectです
	 * @return gameInfoから取り出した情報です
	 */
	public BattleInfoBean convertFromJSON(JSONObject rcvmsg){
		//エラー条件や終了条件がメッセージ中に含まれていた場合にeventにその旨を格納します。
		if(rcvmsg.containsKey("error")){
			bib.setEvent("error");
			//エラーメッセージを格納します
			bib.setError(rcvmsg.getString("error"));
		}
		//試合終了のメッセージを格納します
		if(rcvmsg.containsKey("event")){
			if(rcvmsg.getString("event").equals("win")||rcvmsg.getString("event").equals("lose")||
				rcvmsg.getString("event").equals("draw")){
				bib.setEvent(rcvmsg.getString("event"));
				//問題が無ければ盤面の情報を格納します。
			}else if(rcvmsg.getString("event").equals("YourTurn")){
				bib.setEvent(rcvmsg.getString("event"));
				//盤面情報を格納します
				jary = rcvmsg.getJSONArray("location");
				for(int i=0;i<=2;i++){
					for(int j=0;j<=2;j++){
						locary[i][j] = jary.getJSONArray(i).getString(j);
					}
				}
				bib.setLocation(locary);
			}
		}
		//JSONにeventの情報が無かった場合
		if(!rcvmsg.containsKey("event")&&!rcvmsg.containsKey("error[]")){
			bib.setEvent("blank");
		}
		return bib;
	}
	/**
	 * 指し手情報をJSONObjectに変換します.
	 * @param bib 指し手情報が入ったBattleInfoBeanです
	 * @return 指し手情報を格納したJSONObjectです
	 */
	public JSONObject convertToJSONS(BattleInfoBean bib){
		//指し手情報をBattleInfoBeanから取り出して格納します。
		locateObj.put("xAxis", bib.getxAxis());
		locateObj.put("yAxis", bib.getyAxis());
		locateObj.put("event", TURN_END);

		return locateObj;
	}

}
