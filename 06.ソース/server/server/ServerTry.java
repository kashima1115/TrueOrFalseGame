package server;

import java.net.InetAddress;

/**各クラスの試運転クラス
 *完成時までには削除する
 * @author kanayama
 *
 */
public class ServerTry {
	public static int readyCount=0;

	public static void main(String[] args) throws Exception {
//		JSONObject  gameInfo=null;
//		JSONObject clientMessage=null;
//		LogicAdmin la=new LogicAdmin();
//		for(int i=0;i<2;i++){
//
//			readyCount++;
//
//			gameInfo=new JSONObject();
//			clientMessage=new JSONObject();
//
//			clientMessage.put("logicName", "a");
//			clientMessage.put("creator", "b");
//			clientMessage.put("version", "c");
//			clientMessage.put("address", "0.1");
//
//			gameInfo.put("gameInfo", clientMessage);
//
//			la.logicSet(gameInfo);
//
//		}
//
//
//
//		List<LogicInfoBean> logicList=la.getLogicList();
//
//		for(LogicInfoBean lb:logicList){
//			System.out.println(lb.getLogicName());
//			System.out.println(lb.getCreator());
//			System.out.println(lb.getVersion());
//			System.out.println(lb.getAddress());
//		}
//
//		DbInsert di=new DbInsert();
//		try{
//			di.logicInsert(logicList);
//		}catch(Exception e){
//			throw e;
//		}
//
//		System.out.println(la.attachId());
//
//		System.out.println(la.sameJudge());

//		LocationInfoBean lifb=new LocationInfoBean();
//
//		lifb.setBattleId(4);
//		lifb.setLogicId(5);
//		lifb.setLocationX(0);
//		lifb.setLocationY(1);
//		lifb.setTurn(1);
//		Date date=new Date();
//		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
//		lifb.setPlayStart(sdf.format(date.getTime()));
//		lifb.setPlayEnd(sdf.format(date.getTime()));
//
//		di.locationInsert(lifb);
//
//		BattleIdAdmin bia=new BattleIdAdmin();
//
//		System.out.println(bia.getBattleID());
//
//		LocationAdmin lca=new LocationAdmin();
//
//		String location[][]=lca.createLocation();
//
//		TurnAdmin ta=new TurnAdmin();
//
//		System.out.println(ta.informTurn(location).toString());

		InetAddress ia=InetAddress.getLocalHost();

		String ip = ia.getHostAddress();

		System.out.println(ip);

	}

}
