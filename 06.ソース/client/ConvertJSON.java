package client;

import brain.BrainBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSONObjectに変換したりJSONObjectからデータを取り出したりするクラスです.
 * @author hatsugai
 *
 */

public class ConvertJSON {
	JSONObject obj = new JSONObject();
	JSONArray jary = new JSONArray();
	String[][]locary = new String[3][3];
	BattleInfoBean bib = new BattleInfoBean();
	/**
	 * ロジック情報をJSONObjectに変換します.
	 * @param IPAdress 動作させているマシンのIPアドレスです。サーバー側でクライアントにデータを送るときに使用します.
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
	 * JSONObjectからデータを取り出してBattleInfoBeanに格納します.
	 * @param rcvmsg サーバーから送られてきたJSONObjectです
	 * @return gameInfoから取り出した情報です
	 */
	public BattleInfoBean convertFromJSON(JSONObject rcvmsg){
		//エラー条件や終了条件がメッセージ中に含まれていた場合にeventにその旨を格納します。
		if(rcvmsg.containsKey("error[]")){
			bib.setEvent("error");
			//エラーメッセージを格納します
			JSONArray ary = rcvmsg.getJSONArray("error[]");
			String[] sary = new String[ary.size()];
			for(int i=0;i<=ary.size()-1;i++){
				sary[i]=ary.getString(i);
			}
			bib.setError(sary);
		//JSONにeventの情報が無かった場合
		}else if(!rcvmsg.containsKey("event")){
			bib.setEvent("blank");
		//試合終了のメッセージを格納します
		}else if(rcvmsg.getString("event").equals("win")||rcvmsg.getString("event").equals("lose")||
				rcvmsg.getString("event").equals("draw")){
			bib.setEvent(rcvmsg.getString("event"));
		//問題が無ければ盤面の情報を格納します。
		}else{
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
		return bib;
	}
	/**
	 * 指し手情報をJSONObjectに変換します.
	 * @param bib 指し手情報が入ったBattleInfoBeanです
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
