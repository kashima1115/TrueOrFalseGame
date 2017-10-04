package server;

/**
 * 試合管理クラスのメソッド間の値を引き渡すためのbeanクラス
 * @author kanayama
 *
 */
class DuringBattleInfoTradeBean {
	private String  startDate;
	private String playStartTime;
	private String playEndTime;
	private String turnLogic;
	private String result;
	private boolean stopLoop;
	private String battleStartTime;
	private String battleEndTime;
	private LocationAdmin lca;
	private TurnAdmin ta;
	private boolean matchEnd;
	private boolean preventDoubleTransmissionFlag;


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
	public boolean isStopLoop() {
		return stopLoop;
	}
	public void setStopLoop(boolean stopLoop) {
		this.stopLoop = stopLoop;
	}
	public String getBattleStartTime() {
		return battleStartTime;
	}
	public void setBattleStartTime(String battleStartTime) {
		this.battleStartTime = battleStartTime;
	}
	public String getBattleEndTime() {
		return battleEndTime;
	}
	public void setBattleEndTime(String battleEndTime) {
		this.battleEndTime = battleEndTime;
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
	public boolean isMatchEnd() {
		return matchEnd;
	}
	public void setMatchEnd(boolean matchEnd) {
		this.matchEnd = matchEnd;
	}
	public boolean isPreventDoubleTransmissionFlag() {
		return preventDoubleTransmissionFlag;
	}
	public void setPreventDoubleTransmissionFlag(boolean preventDoubleTransmissionFlag) {
		this.preventDoubleTransmissionFlag = preventDoubleTransmissionFlag;
	}
}
