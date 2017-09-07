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
private ClientLogicBean clb;
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
			this.clb=new ClientLogicBean();
		}

		//ロジック情報Beanに各ロジック情報をセット
		LogicInfoBean lib=new LogicInfoBean();
		lib.setLogicName(gameInfo.getString("logicName"));
		lib.setCreator(gameInfo.getString("creator"));
		lib.setVersion(gameInfo.getString("version"));
		lib.setAddress(gameInfo.getString("address"));

		//MapにBeanを追加
		this.logicMap.put(gameInfo.getString("logicName")+gameInfo.getString("creator")+
				gameInfo.getString("version"),lib);

		//クライアントアドレスを保管するBeanにセット
		if(this.readyCount==1){
			this.clb.setFirstLogic(gameInfo.getString("logicName")+gameInfo.getString("creator")+
					gameInfo.getString("version"));
		}else if(this.readyCount==2){
			this.clb.setSecondLogic(gameInfo.getString("logicName")+gameInfo.getString("creator")+
					gameInfo.getString("version"));
		}

	}
	/**試合管理クラスがロジック情報Mapを取得
	 * @return logicList ロジック情報を含むBean
	 */
	public Map<String,LogicInfoBean> getLogicMap(){
		return this.logicMap;
	}


	public ClientLogicBean getClientAddressBean(){
		return this.clb;
	}

	public boolean fillClient(){
		//readyCountが1以下ならばtrue、2以上ならばfalseを返す

				boolean enoughClient=true;

				if(readyCount<=1){
					enoughClient=false;
				}else if(readyCount>1){
					enoughClient=true;
				}

				return enoughClient;
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
			int logicId=dbi.getLogicId(this.logicMap.get(this.clb.getFirstLogic()));

			this.addRefIdMap.put(this.clb.getFirstLogic(), logicId);

			logicId=dbi.getLogicId(this.logicMap.get(this.clb.getSecondLogic()));

			this.addRefIdMap.put(this.clb.getSecondLogic(), logicId);

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
		if(this.addRefIdMap.get(this.clb.getFirstLogic())==this.addRefIdMap.get(this.clb.getSecondLogic())){
			return false;
		}

		return true;
	}
}
