package servlet;

import java.io.Serializable;


/**
*
* ゲーム情報格納Bean
*
* @author arahari
* @version 1.0
*/

public class TrueOrFalseBean implements Serializable {






	private int logic_id;
	private String PFlogic_name;
	private String PFlogic_writer;
	private String PFlogic_ver;
	private String logic_name;
	private String logic_writer;
	private String logic_ver;
	private int battle_id;
	private String result;
	private String year;
	private String month;
	private String day;
	private String start_hour;
	private String start_min;
	private String start_sec;
	private String end_hour;
	private String end_min;
	private String end_sec;
	private int location_id;
	private int location_x;
	private int location_y;
	private int turn;
	private int radio;



	public int getLogic_id() {
		return logic_id;
	}
	public void setLogic_id(int logic_id) {
		this.logic_id = logic_id;
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
	public int getBattle_id() {
		return battle_id;
	}
	public void setBattle_id(int battle_id) {
		this.battle_id = battle_id;
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
	public String getStart_hour() {
		return start_hour;
	}
	public void setStart_hour(String start_hour) {
		this.start_hour = start_hour;
	}
	public String getStart_min() {
		return start_min;
	}
	public void setStart_min(String start_min) {
		this.start_min = start_min;
	}
	public String getStart_sec() {
		return start_sec;
	}
	public void setStart_sec(String start_sec) {
		this.start_sec = start_sec;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
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
	public String getEnd_hour() {
		return end_hour;
	}
	public void setEnd_hour(String end_hour) {
		this.end_hour = end_hour;
	}
	public String getEnd_min() {
		return end_min;
	}
	public void setEnd_min(String end_min) {
		this.end_min = end_min;
	}
	public String getEnd_sec() {
		return end_sec;
	}
	public void setEnd_sec(String end_sec) {
		this.end_sec = end_sec;
	}
	public int getRadio() {
		return radio;
	}
	public void setRadio(int radio) {
		this.radio = radio;
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




}
