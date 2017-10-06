package throughput_latency;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

public class Apro {
	private static Logger logger = Logger.getLogger(Apro.class.getName());

	public static void main(String[] args) throws JMSException {

		long start = System.nanoTime();
		// DBに書き込む回数を設定する
		int loop = 100;

		MQ.accses(loop);

		//DBからセレクトした値を取得
		MQ.reReceive();
		//レイテンシとスループットの計算
		long end = System.nanoTime();

		long lTime = end - start;

		double sTime = lTime / loop;

		double tTime = 1000000000 / sTime;

		logger.info("レイテンシ処理時間は" + lTime + "ナノ秒です。");
		logger.info("スループット処理時間は" + tTime + "件/秒です。");
	}

}
