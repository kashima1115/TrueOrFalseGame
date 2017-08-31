package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import brain.BrainBean;
import net.sf.json.JSONObject;
/**
 * クライアント機能の動作をコントロールするクラスです.
 * @author hatsugai
 *
 */
public class SequenceControl{
	/**
	 * 試合開始時の動作です
	 */
	public static void startGame(){
		//ロジック情報を取得(bean)
		AccessBrain ab = new AccessBrain();
		BrainBean logic = ab.getLogicInfo();

		//起動しているマシンのIPアドレスを特定する
		String IPAdress ="/123.123.1.123";//値は適当に書いた値です。後でマシンのIPアドレスが入ります。
		IPAdress = myIP();

		//JSONに変換するためインスタンス化
		ConvertJSON cj = new ConvertJSON();
		//JSONに変換。(ロジックの情報は既にlogicに入っておりConvertJSONでまとめて変換します)
		JSONObject jo = cj.convertToJSONF(logic,IPAdress);

		//ActiveMQを通してサーバープログラムに送信する
		messageQueue.ActiveMQMessaging amq = new messageQueue.ActiveMQMessaging();
		//受信用Queueを作成
		amq.createQueue(IPAdress);
		//サーバーにロジック情報を送信する
		amq.sendMessage(jo);
	}
	/**
	 * IPアドレスを特定します
	 * @return IPアドレスです。IPv4を想定しています
	 */

	public static String myIP(){
		//起動しているマシンのIPアドレスを特定する
				String IPAdress ="/123.123.1.123";
//				try{
//					Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//					while(interfaces.hasMoreElements()){
//						NetworkInterface mypc = interfaces.nextElement();
//						List<InterfaceAddress> addresses = mypc.getInterfaceAddresses();
//						for(InterfaceAddress address:addresses){
//							//IPアドレスだけを取得（そのままだと先頭にスラッシュが付いてしまうので切り取る
//							IPAdress = address.getAddress().toString().substring(1);
//						}
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
				try{
					InetAddress host = InetAddress.getLocalHost();
					IPAdress = host.getHostAddress();
				}catch(UnknownHostException e){
					e.printStackTrace();
				}
		return IPAdress;
	}
	/**
	 * 自分のターンごとに状況に応じた処理を行います
	 */
	public static void myTurn(){
		for(int turn=0;turn<5;turn++){
			messageQueue.ActiveMQMessaging amq = new messageQueue.ActiveMQMessaging();
			//JSONの宣言
			JSONObject obj;
			//ActiveMQからメッセージを受信する
			obj = amq.receiveMessage();
			//JSONから変換
			ConvertJSON cj = new ConvertJSON();
			List<BattleInfoBean> bib =cj.convertFromJSON(obj);
			//変数eventの宣言
			String event = "blank";
			//brainに送るための変数を宣言
			String[][]location = null;
			//event情報だけbeanから取り出す
			for(BattleInfoBean binfob:bib){
				event = binfob.getEvent();
			}
			//event情報をとりあえず表示する
			System.out.println(event);
			//例外条件分岐
			if(event.equals("error")){
				for(BattleInfoBean binfob:bib){
					//エラーメッセージを表示する
					System.out.println(binfob.getError());
				}
				break;
			//終了条件分岐
			}else if(event.equals("win")||event.equals("lose")||event.equals("draw")){
				break;
			}
			//BattleInfoBeanから盤面情報を取得する
			for(BattleInfoBean binfob:bib){
				location = binfob.getLocation();
			}
			//盤面情報をbrainに渡して指し手情報を取得する
			AccessBrain ab = new AccessBrain();
			List<BattleInfoBean> bibList = ab.getLocation(location);
			//JSONに変換
			JSONObject jo = cj.convertToJSONS(bibList);
			//ActiveMQを通してサーバープログラムに送信する
			amq.sendMessage(jo);
		}
		//例外・終了条件時の受信用Queueの処理
		messageQueue.ActiveMQMessaging amq = new messageQueue.ActiveMQMessaging();
		amq.quitQueue();
	}
}
