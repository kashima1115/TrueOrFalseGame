package messageQueue;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class DummyActiveMQMessaging implements MessageQueueController {

	// List -> Queue
	public List<JSONObject> sendMessageList = new ArrayList<>();

	@Override
	public void sendMessage(JSONObject gameInfo) {
		// TODO 自動生成されたメソッド・スタブ
		sendMessageList.add(gameInfo);
	}

	public void assertSendMessage(JSONObject gameInfo) {

	}

	@Override
	public JSONObject receiveMessage() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
