package servlet;

import java.io.Serializable;


/**
*
* ゲーム情報格納Bean
*
* @author arahari
* @version 1.0
*/

public class BattleResultBean implements Serializable {

	private int battle_id;
	private String PFlogic_name;
	private String PFlogic_writer;
	private String PFlogic_ver;
	private String logic_name;
	private String logic_writer;
	private String logic_ver;
	private String result;
	private String year;
	private String month;
	private String day;
	private int turn;
	public int getBattle_id() {
		return battle_id;
	}
	public void setBattle_id(int battle_id) {
		this.battle_id = battle_id;
	}
	public String getPFlogic_name() {
		return PFlogic_name;
	}
	public void setPFlogic_name(String pFlogic_name) {
		PFlogic_name = pFlogic_name;
	}
	public String getPFlogic_writer() {
		return PFlogic_writer;
	}
	public void setPFlogic_writer(String pFlogic_writer) {
		PFlogic_writer = pFlogic_writer;
	}
	public String getPFlogic_ver() {
		return PFlogic_ver;
	}
	public void setPFlogic_ver(String pFlogic_ver) {
		PFlogic_ver = pFlogic_ver;
	}
	public String getLogic_name() {
		return logic_name;
	}
	public void setLogic_name(String logic_name) {
		this.logic_name = logic_name;
	}
	public String getLogic_writer() {
		return logic_writer;
	}
	public void setLogic_writer(String logic_writer) {
		this.logic_writer = logic_writer;
	}
	public String getLogic_ver() {
		return logic_ver;
	}
	public void setLogic_ver(String logic_ver) {
		this.logic_ver = logic_ver;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}



}
