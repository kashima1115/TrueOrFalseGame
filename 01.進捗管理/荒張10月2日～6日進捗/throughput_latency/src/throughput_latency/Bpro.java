package throughput_latency;

import javax.jms.JMSException;

public class Bpro {

	public static void main(String[] args) throws JMSException {

		try {
			DBAccess bda = new DBAccess();
			//受信したループ回数をdbaの引数としセレクトされたものを送信する
			MQ.reAccses(bda.access(Integer.parseInt(MQ.receive().getText())));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
