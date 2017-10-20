package messageQueue;

public class MessageQueueControllerFactory {

	private static boolean isDummy = false;

	public static MessageQueueController create() {
		if (isDummy)
			return new DummyActiveMQMessaging();
		else
			return new ActiveMQMessaging();
	}

	public static void isDummy(boolean x) {
		isDummy = x;
	}
}
