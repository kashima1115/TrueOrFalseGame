package servlet;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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


	public static String generateLogicInfoKey(int x, int y){
		return String.valueOf(x + "-" + y);
//		return String.valueOf(x).concat("-").valueOf(y);
	}


	//9つのデータを作成しなくても問題ないことが判明したため保留
	public static Map<String, LocationBean> generate(String cc){

		// 初期化(空データを作成）して返す。
		Map<String, LocationBean> map = new HashMap<String, LocationBean>();

		map.get("0-0");
		map.get("1-0");
		map.get("2-0");
		map.get("0-1");
		map.get("1-1");
		map.get("2-1");
		map.get("0-2");
		map.get("1-2");
		map.get("2-2");

	    return map;

	}

	//　DateTime型に変換するメソッド（うまくいかない）
	public  DateTime dateTime(String dt){


		DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dt);

		return dateTime;



	}

//　複数の変数を持った1つのクラスにして返す
	public LogicInfoUtil splitMethod(String spdt, String spst, String spet) {

		LogicInfoUtil liu = new LogicInfoUtil();

		String[] battleDaySpl = spdt.split("-");
		liu.year = battleDaySpl[0];
		liu.month = battleDaySpl[1];
		liu.day = battleDaySpl[2];

		String[] startTime = spst.split(":");
		liu.startHour = startTime[0];
		liu.startMin = startTime[1];
		liu.startSec = startTime[2];

		String[] endTime = spet.split(":");
		liu.endHour = endTime[0];
		liu.endMin = endTime[1];
		liu.endSec = endTime[2];

		return liu;
	}

}

