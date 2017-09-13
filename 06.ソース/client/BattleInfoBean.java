package client;

import java.io.Serializable;

/**
 * 試合中の情報を保管するためのBeanです.
 * @author hatsugai
 *
 */

public class BattleInfoBean implements Serializable{
	/**
	 * そのターンでクライアントプログラムがどのような動作をするべきかという情報を格納します.
	 */
	private String event;
	/**
	 * エラーメッセージを格納します.
	 */
	private String[] error;
	/**
	 * 盤面全体の情報を格納します.
	 */
	private String[][] location;
	/**
	 * 指し手の横座標(列)を格納します。0~2までを想定しています.
	 */
	private int xAxis;
	/**
	 * 指し手の縦座標(行)を格納します。0~2までを想定しています.
	 */
	private int yAxis;

	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String[] getError() {
		return error;
	}
	public void setError(String[] error) {
		this.error = error;
	}
	public String[][] getLocation() {
		return location;
	}
	public void setLocation(String[][] location) {
		this.location = location;
	}
	public int getxAxis() {
		return xAxis;
	}
	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}
	public int getyAxis() {
		return yAxis;
	}
	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}
}
