package server;

import net.sf.json.JSONObject;

/**
 * 試合管理クラスのメソッド間の値を引き渡すためのbeanクラス
 * @author kanayama
 *
 */
public class DuringBattleInfoTrade {
	private String  startDate;
	private String playStartTime;
	private String playEndTime;
	private boolean errJudge;
	private String turnLogic;
	private String result;
	private boolean stopRoop;
	private String startTime;
	private String endTime;
	private String[][] location;
	private JSONObject receiveGameInfo;
	private JudgeMatch jm;
	private LocationAdmin lca;
	private TurnAdmin ta;


	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getPlayStartTime() {
		return playStartTime;
	}
	public void setPlayStartTime(String playStartTime) {
		this.playStartTime = playStartTime;
	}
	public String getPlayEndTime() {
		return playEndTime;
	}
	public void setPlayEndTime(String playEndTime) {
		this.playEndTime = playEndTime;
	}
	public boolean isErrJudge() {
		return errJudge;
	}
	public void setErrJudge(boolean errJudge) {
		this.errJudge = errJudge;
	}
	public String getTurnLogic() {
		return turnLogic;
	}
	public void setTurnLogic(String turnLogic) {
		this.turnLogic = turnLogic;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public boolean isStopRoop() {
		return stopRoop;
	}
	public void setStopRoop(boolean stopRoop) {
		this.stopRoop = stopRoop;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String[][] getLocation() {
		return location;
	}
	public void setLocation(String[][] location) {
		this.location = location;
	}
	public JSONObject getReceiveGameInfo() {
		return receiveGameInfo;
	}
	public void setReceiveGameInfo(JSONObject receiveGameInfo) {
		this.receiveGameInfo = receiveGameInfo;
	}
	public JudgeMatch getJm() {
		return jm;
	}
	public void setJm(JudgeMatch jm) {
		this.jm = jm;
	}
	public LocationAdmin getLca() {
		return lca;
	}
	public void setLca(LocationAdmin lca) {
		this.lca = lca;
	}
	public TurnAdmin getTa() {
		return ta;
	}
	public void setTa(TurnAdmin ta) {
		this.ta = ta;
	}
}
