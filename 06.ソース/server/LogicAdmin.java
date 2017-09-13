package server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
/**
 * ロジック情報管理クラス
 *@author kanayama
 */
public class LogicAdmin {


	/**ロジック情報保持（Map化）メソッド
	 * @param gameInfo クライアントから受信ロジック情報を含むオブジェクト
	 * @return なし
	 */
	public LogicInfoBean logicSet(JSONObject gameInfo){

		//ロジック情報Beanに各ロジック情報をセット
		LogicInfoBean lib=new LogicInfoBean();
		lib.setLogicName(gameInfo.getString("logicName"));
		lib.setCreator(gameInfo.getString("creator"));
		lib.setVersion(gameInfo.getString("version"));
		lib.setAddress(gameInfo.getString("address"));

		return lib;
	}


	/**
	 * ロジック情報をロジックIDと紐付け・取得
	 * @return キー…IPアドレス 値…logicIdのMap
	 * @throws Exception
	 */
	public Map<String,Integer> attachId(DbInsert dbi,Map<String,LogicInfoBean> logicMap,
			ClientLogicBean clb) throws SQLException{

		Map<String,Integer> logicRefIdMap=null;

		try{
			//IPアドレスとロジックIDのMap作成
			logicRefIdMap=new HashMap<String,Integer>();

			//ロジックIDをDBから取得
			int logicId=dbi.getLogicId(logicMap.get(clb.getFirstLogic()));

			logicRefIdMap.put(clb.getFirstLogic(), logicId);

			logicId=dbi.getLogicId(logicMap.get(clb.getSecondLogic()));

			logicRefIdMap.put(clb.getSecondLogic(), logicId);

		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
		return logicRefIdMap;
	}

	/**同名ロジック判定を行うメソッド
	 *  @return 同名ロジックの場合true、それ以外の場合falseを返す
	 */
	public boolean sameJudge(Map<String,Integer> logicRefIdMap,ClientLogicBean clb){

		//同名ロジック判定
		if(logicRefIdMap.get(clb.getFirstLogic())==logicRefIdMap.get(clb.getSecondLogic())){
			return true;
		}

		return false;
	}
}
