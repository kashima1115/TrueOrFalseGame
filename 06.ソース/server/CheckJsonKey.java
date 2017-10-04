package server;

import net.sf.json.JSONObject;

/**
 * 受信したJSONObjectが正しい構成かチェック
 * @author kanayama
 *
 */
class CheckJsonKey {
	private final static String EVENT="event";
	private final static String LOGIC_NAME="logicWriter";
	private final static String LOGIC_VERSION="logicVersion";
	private final static String ADDRESS="address";
	private final static String X_AXIS="xAxis";
	private final static String Y_AXIS="yAxis";

	/**
	 * 送信されたロジック情報の構成が正しいか判定
	 * @param logicInfo
	 * @return
	 */
	static boolean checkLogicStructure(JSONObject logicInfo){

		if(logicInfo.containsKey(EVENT)==true && logicInfo.containsKey(LOGIC_NAME)==true &&
				logicInfo.containsKey(LOGIC_VERSION)==true && logicInfo.containsKey(ADDRESS)==true){
			return true;
		}else{
			return false;
		}
	}

/**
 * 送信された指し手情報の構成が正しいかで判定
 * @param receiveGameInfo
 * @return
 */
	static boolean checkLocationSturcture(JSONObject receiveGameInfo){

		if(receiveGameInfo.containsKey(X_AXIS)==true &&
				receiveGameInfo.containsKey(Y_AXIS)==true){
			return true;
		}else{
			return false;
		}
	}

	static boolean checkExitenceOfEvent(JSONObject receiveGameInfo){

		if(receiveGameInfo.containsKey(EVENT)==true){
			return true;
		}else{
			return false;
		}

	}
}
