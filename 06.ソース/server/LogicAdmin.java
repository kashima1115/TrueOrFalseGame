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
private Map<String,LogicInfoBean> logicMap;
private Map<String,Integer> addRefIdMap;
private ClientAddressBean cab;
private int readyCount=0;

	/**ロジック情報保持（Map化）メソッド
	 * @param gameInfo クライアントから受信ロジック情報を含むオブジェクト
	 * @return なし
	 */
	public void logicSet(JSONObject gameInfo){

		this.readyCount++;

		//初回に呼び出されたときのみインスタンス化
		if(this.readyCount==1){
			this.logicMap=new HashMap<String,LogicInfoBean>();
			this.cab=new ClientAddressBean();
		}

		//ロジック情報Beanに各ロジック情報をセット
		LogicInfoBean lib=new LogicInfoBean();
		lib.setLogicName(gameInfo.getString("logicName"));
		lib.setCreator(gameInfo.getString("creator"));
		lib.setVersion(gameInfo.getString("version"));

		//MapにBeanを追加
		this.logicMap.put(gameInfo.getString("address"),lib);

		//クライアントアドレスを保管するBeanにセット
		if(this.readyCount==1){
			this.cab.setFirstAddress(gameInfo.getString("address"));
		}else if(this.readyCount==2){
			this.cab.setSecondAddress(gameInfo.getString("address"));
		}

	}
	/**試合管理クラスがロジック情報Mapを取得
	 * @return logicList ロジック情報を含むBean
	 */
	public Map<String,LogicInfoBean> getLogicMap(){
		return this.logicMap;
	}


	public ClientAddressBean getClientAddressBean(){
		return this.cab;
	}

	/**
	 * ロジック情報をロジックIDと紐付け・取得
	 * @return キー…IPアドレス 値…logicIdのMap
	 * @throws Exception
	 */
	public Map<String,Integer> attachId(DbInsert dbi) throws SQLException{
		try{

			//IPアドレスとロジックIDのMap作成
			this.addRefIdMap=new HashMap<String,Integer>();

			//ロジックIDをDBから取得
			int logicId=dbi.getLogicId(this.logicMap.get(this.cab.getFirstAddress()));

			this.addRefIdMap.put(this.cab.getFirstAddress(), logicId);

			logicId=dbi.getLogicId(this.logicMap.get(this.cab.getSecondAddress()));

			this.addRefIdMap.put(this.cab.getSecondAddress(), logicId);

		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
		return this.addRefIdMap;
	}

	/**同名ロジック判定を行うメソッド
	 *  @return 同名ロジックの場合false、それ以外の場合trueを返す
	 */
	public boolean sameJudge(){

		//同名ロジック判定
		if(this.addRefIdMap.get(this.cab.getFirstAddress())==this.addRefIdMap.get(this.cab.getSecondAddress())){
			return false;
		}

		return true;
	}
}
