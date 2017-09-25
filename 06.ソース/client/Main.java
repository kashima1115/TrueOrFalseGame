package client;

import java.net.UnknownHostException;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

/**
 * メインクラスです。クライアントプログラムを起動させるにはこのクラスを実行してください.
 * @author hatsugai
 * @version 0.1
 */
public class Main {
/**
 * メインメソッドです.
 * @param args 特に使用しません
 */
	public static void main(String[] args) {
		SequenceControl.initialize();
		try {
			SequenceControl.startGame();
			SequenceControl.myTurn();
		} catch (UnknownHostException e) {
			Logger logger = Logger.getLogger(SequenceControl.class.getName());
			logger.fatal("IPアドレスの取得に失敗しました。",e);
			System.exit(0);
		} catch (JMSException e) {
			System.exit(0);
		}
	}

}
