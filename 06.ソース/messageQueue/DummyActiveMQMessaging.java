package messageQueue;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class DummyActiveMQMessaging implements MessageQueueController {

	// List -> Queue
	public List<JSONObject> sendMessageList = new ArrayList<>();

	int i = 0;
	int j = 0;
	@Override
	public void sendMessage(JSONObject gameInfo) {
		// TODO 自動生成されたメソッド・スタブ
		sendMessageList.add(gameInfo);
		System.out.println("データ格納");
		if(i == 0){
			assertEquals("{\"xAxis\":2,\"yAxis\":0,\"event\":\"TurnEnd\"}", sendMessageList.get(0).toString());
		}else{
			assertEquals("{\"xAxis\":2,\"yAxis\":0,\"event\":\"TurnEnd\"}", sendMessageList.get(0).toString());
		}
		i++;
	}

	public void assertSendMessage(JSONObject gameInfo) {

	}

	@Override
	public JSONObject receiveMessage() {
		JSONObject gameInfo;
		if(j == 0){
			System.out.println("サーバーからの通信待ち");
			String rcvmsg = "{\"event\":\"YourTurn\",\"location\":[[\"○\",\"×\",\"-\"],[\"-\",\"-\",\"-\"],[\"-\",\"-\",\"-\"]]}";
			gameInfo = JSONObject.fromObject(rcvmsg);
			System.out.println("データ受信");
		}else{
			System.out.println("サーバーからの通信待ち");
			String rcvmsg = "{\"event\":\"win\"}";
			gameInfo = JSONObject.fromObject(rcvmsg);
			System.out.println("データ受信");
		}
		j++;
		return gameInfo;
	}

	@Override
	public void createQueue(String IPAdress) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void quitQueue() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void sendMessage(JSONObject gameInfo, String IPAdress) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
