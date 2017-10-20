package server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/**
 * ロジック情報加工クラス
 *@author kanayama
 */
class LogicUtil {

	/**
	 * ロジック情報をロジックIDと紐付け・取得
	 * @param battleId 試合ID
	 * @param dbit 試合中・試合終了後に使用するオブジェクト
	 * @param logicRefIdMap ロジック情報キーとロジックIDを関連付けたMap
	 * @return キー…IPアドレス 値…logicIdのMap
	 * @throws SQLException
	 */
	static Map<String,Integer> attachId(DbOperation dbi,Map<String,LogicInfoBean> logicMap,
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
	 * @return 同名ロジックの場合true、それ以外の場合falseを返す
	 * @throws SameLogicException
	 */
	static void sameJudge(Map<String,Integer> logicRefIdMap,ClientLogicBean clb) throws SameLogicException{

		try{
			//同名ロジック判定
			if(logicRefIdMap.get(clb.getFirstLogic())==logicRefIdMap.get(clb.getSecondLogic())){
				throw new SameLogicException();
			}else{
				return;
			}
		}catch(SameLogicException e){
			throw e;
		}
	}
}
