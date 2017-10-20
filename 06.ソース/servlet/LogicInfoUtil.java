package servlet;

import org.joda.time.LocalTime;
public class LogicInfoUtil {

	String year;
	String month;
	String day;
	String startHour;
	String startMin;
	String startSec;
	String endHour;
	String endMin;
	String endSec;



//　複数の変数を持った1つのクラスにして返す
	public LogicInfoUtil splitMethod(String spdt, String spst, String spet) {

		LogicInfoUtil liu = new LogicInfoUtil();

		try{
		//DateTime型に変換する
		org.joda.time.LocalDate date = new org.joda.time.LocalDate(spdt);

		liu.year = String.valueOf(date.getYear());
		liu.month = String.valueOf(date.getMonthOfYear());
		liu.day = String.valueOf(date.getDayOfMonth());

		//DateTime型に変換する
		LocalTime stTime = new LocalTime(spst);

		liu.startHour = String.valueOf(stTime.getHourOfDay());
		liu.startMin = String.valueOf(stTime.getMinuteOfHour());
		liu.startSec = String.valueOf(stTime.getSecondOfMinute());

		//DateTime型に変換する
		LocalTime edTime = new LocalTime(spet);

		liu.endHour = String.valueOf(edTime.getHourOfDay());
		liu.endMin = String.valueOf(edTime.getMinuteOfHour());
		liu.endSec = String.valueOf(edTime.getSecondOfMinute());

		return liu;

		}catch (Exception e){
			System.out.println(e.getMessage());

			return null;

		}
	}
	
	
}

