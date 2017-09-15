package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import brain.BrainBean;
import net.sf.json.JSONObject;
/**
 * クライアント機能の動作をコントロールするテスト向けのクラスです.
 * @author hatsugai
 *
 */
public class SequenceControlForTest{
	static AccessBrain ab = new AccessBrain();
	static ConvertJSON cj = new ConvertJSON();
	static messageQueue.ActiveMQMessaging amq = new messageQueue.ActiveMQMessaging();
	static BattleInfoBean bib;

	/**
	 * 試合開始時の動作です.
	 */
	public static JSONObject startGame(){
		//ロジック情報を取得(bean)
		ab.createBrain();
		BrainBean logic = ab.getLogicInfo();

		//起動しているマシンのIPアドレスを特定する
		String IPAdress ="123.123.1.123";//起動しているマシンのIPアドレスが入ります。
		IPAdress = myIP();

		//JSONに変換。(ロジックの情報は既にlogicに入っておりConvertJSONでまとめて変換します)
		JSONObject jo = cj.convertToJSONF(logic,IPAdress);
		//本番ではここでActiveMQにJSONObjectを渡します。
		return jo;
	}

	/**
	 * IPアドレスを特定します（ローカルホストです）.
	 * @return IPアドレスです。IPv4を想定しています
	 */

	public static String myIP(){
		//起動しているマシンのIPアドレスを特定する
				String IPAdress ="123.123.1.123";
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
//				}catch(IOException e){
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
	 * 自分のターンごとに状況に応じた処理を行います.
	 */
	public static String myTurn(BattleInfoBean bib){
		//変数eventの宣言
		String event = "blank";
		//Brainのインスタンス化
		ab.createBrain();
		//TODO ○×ゲームは最大5回+最後のメッセージ受信の1回でこの条件にしています。
		for(int turn=0;turn<5;turn++){
			//event情報だけbeanから取り出す
			event = bib.getEvent();
			EventType et = EventType.getEventType(event);

			//例外条件分岐
			if(et.equals(EventType.ERROR)){
				//エラーメッセージを表示する
				String errors[]=bib.getError();
				for(int error = 0;error<=errors.length-1;error++){
					System.out.println(errors[error]);
				}
				break;
			//終了条件分岐
			}else if(et.equals(EventType.FINISH)){
				System.out.println("試合が終了しました。");
				System.out.println("You "+event);
				break;
			//差し手選択
			}else if(et.equals(EventType.YOURTURN)){
				//brainに送るための盤面情報の変数locationを宣言
				String[][]location = null;
				//BattleInfoBeanから盤面情報を取得する
				location = bib.getLocation();
				//Brainから指し手情報を取得する
				bib = getLocation(location);
				//eventに座標を追加(本来はサーバーに送信する）
				event += Integer.toString(bib.getxAxis());
				event += Integer.toString(bib.getyAxis());
				break;//1回のみ動かすためbreakを設置
			//eventに何も入っていない場合（その他）
			}else{
				System.out.println("イベント情報の取得に失敗しました。");
				break;
			}
		}

		//eventを返す（本来は例外・終了条件時の受信用Queueの処理）
		return event;
	}

	/**
	 * ActiveMQからのメッセージを受信します.
	 * @return 盤面情報やイベント情報を格納したBattleInfoBeanです
	 */
	public static BattleInfoBean receive() {
		//ActiveMQからメッセージを受信する
		JSONObject obj2 = amq.receiveMessage();
		//JSONから変換
		bib =cj.convertFromJSON(obj2);
		return bib;
	}

	/**
	 * 盤面情報を渡して指し手の情報を渡します.
	 * @param location 盤面情報を格納した二次元配列のStringです
	 * @return 指し手の情報が入ったBattleInfoBeanです
	 */
	public static BattleInfoBean getLocation(String[][] location){
		//盤面情報をbrainに渡して指し手情報を取得する
		bib = ab.getLocation(location);
		return bib;
	}

	/**
	 * サーバーにメッセージを送信します.
	 * @param bib 盤面情報を格納したBattleInfoBeanです
	 */
	public static void send(BattleInfoBean bib){
		//JSONに変換
		JSONObject jo2 = cj.convertToJSONS(bib);
		//ActiveMQを通してサーバープログラムに送信する
		amq.sendMessage(jo2);
	}
}
