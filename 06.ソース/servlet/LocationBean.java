package servlet;

import java.io.Serializable;


/**
*
* ゲーム情報格納Bean
*
* @author arahari
* @version 1.0
*/

public class LocationBean implements Serializable {

	private int location_x;
	private int location_y;
	private int turn;

	public int getLocation_x() {
		return location_x;
	}
	public void setLocation_x(int location_x) {
		this.location_x = location_x;
	}
	public int getLocation_y() {
		return location_y;
	}
	public void setLocation_y(int location_y) {
		this.location_y = location_y;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}

}
