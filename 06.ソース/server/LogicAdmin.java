package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
/**
 * ロジック情報管理クラス
 *@author kanayama
 */
public class LogicAdmin {
private List<LogicInfoBean> logicList;
private Map<String,Integer> addRefIdMap;

	/**ロジック情報保持（リスト化）メソッド
	 * @param gameInfo クライアントから受信ロジック情報を含むオブジェクト
	 * @return なし
	 */
	public void logicSet(JSONObject gameInfo){

		JSONObject clientMessage=gameInfo.getJSONObject("gameInfo");

		if(ServerTry.readyCount==1){
			this.logicList=new ArrayList<LogicInfoBean>();
		}

		//Beanに各ロジック情報をセット
		LogicInfoBean lib=new LogicInfoBean();
		lib.setLogicName(clientMessage.getString("logicName"));
		lib.setCreator(clientMessage.getString("creator"));
		lib.setVersion(clientMessage.getString("version"));
		lib.setAddress(clientMessage.getString("address"));

		//ListにBeanを追加
		this.logicList.add(lib);

	}
	/**試合管理クラスがロジック情報リストを取得
	 * @return logicList ロジック情報を含むBean
	 */
	public List<LogicInfoBean> getLogicList(){
		return this.logicList;
	}

	/**
	 * ロジック情報をロジックIDと紐付け・取得
	 * @return キー…IPアドレス 値…logicIdのMap
	 * @throws Exception
	 */
	public Map<String,Integer> attachId() throws Exception{
		try{
			for(LogicInfoBean lb:this.logicList){
				//DBアクセスクラスをインスタンス化
				DbInsert dbi=new DbInsert();

				//ロジックID取得
				int logicId=dbi.getLogicId(lb);

				//IPアドレスとロジックIDを紐付け
				this.addRefIdMap.put(lb.getAddress(), logicId);

			}

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return this.addRefIdMap;
	}

	/**同名ロジック判定を行うメソッド
	 *  @return 同名ロジックの場合false、それ以外の場合trueを返す
	 */
	public boolean sameJudge(){
		int compareId=0;
		int sameCount=0;

		//同名ロジックがあった場合カウントする
		for(LogicInfoBean lb:this.logicList){
			int getId=this.addRefIdMap.get(lb.getAddress());
			if(getId==compareId){
				sameCount++;
			}
			compareId=getId;
		}

		//同名ロジック判定
		if(sameCount>0){
			return false;
		}

		return true;
	}
}
