package server;

import java.io.Serializable;
/**
 * 指し手情報を格納するBeanクラス
 * @author kanayama
 *
 */
class LocationInfoBean implements Serializable {
	private int logicId;
	private int battleId;
	private int locationX;
	private int locationY;
	private String playStart;
	private String playEnd;
	private int turn;

	public int getLogicId() {
		return logicId;
	}

	public void setLogicId(int logicId) {
		this.logicId = logicId;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	public String getPlayStart() {
		return playStart;
	}

	public void setPlayStart(String playStart) {
		this.playStart = playStart;
	}

	public String getPlayEnd() {
		return playEnd;
	}

	public void setPlayEnd(String playEnd) {
		this.playEnd = playEnd;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
