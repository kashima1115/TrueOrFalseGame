package servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
*
* 試合詳細取得クラス
*
* @author arahari
* @version 1.0
*/
public class BattleDetailDBAccess {
	/**
	 * @param id
	 * ラジオボタンで選択された試合ID
	 *
	 * @return
	 * ロジック情報と試合結果、指し手情報をリストに格納して返す
	 * 指し手情報は9手未満だった場合も必ず9手分格納する
	 * ロジック情報と試合結果取得は、内部結合を行い選択した試合の
	 * 情報を対戦者ごとに取り出す。
	 *
	 * @throws Exception
	 * 取得に問題があった場合に起こり得る例外
	 */
	Context context;
	DataSource ds;
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;

	protected List<LocationBean> LocationBean(String id) throws Exception {

        // データベースへの検索処理**************************************************/
    	// datasourceを使用
    	context = new InitialContext();
    	ds = (DataSource)context.lookup("java:comp/env/jdbc/library");
    	con = ds.getConnection();

//    	Map<String, LocationBean> map = new HashMap<String, LocationBean>();

        List<LocationBean> locationList = new ArrayList<LocationBean>();
//        int x;
//        int y;
//        String cc;

		try {
           // SQL文生成
            String locationSQL ="SELECT location_x, location_y, turn FROM location where battle_id = ?";
            stmt = con.prepareStatement(locationSQL);
            stmt.setString(1, id);
            // SQL実行
            rs =stmt.executeQuery();


//            LogicInfoUtil liu = new LogicInfoUtil();

            // 取得データをListにセット
            while(rs.next()) {
            	LocationBean locationBn = new LocationBean();

//                x = rs.getInt("location_x");
//                y = rs.getInt("location_y");

                locationBn.setLocation_x(rs.getInt("location_x"));
                locationBn.setLocation_y(rs.getInt("location_y"));
            	locationBn.setTurn(rs.getInt("turn"));

//                cc = liu.generateLogicInfoKey(x, y);
//                locationBn.setLocation(cc);

            	locationList.add(locationBn);

//            	map.put("locationConcat", locationBn);
            }

        }catch(Exception e){
            // 例外をthrow
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
        }
        return locationList;
    }

    protected List<BattleDetailBean> BattleDetailBean(String id) throws Exception {

        List<BattleDetailBean> detailList = new ArrayList<BattleDetailBean>();

        try{
            // SQL文生成
            String detailSQL = "SELECT battle_result.battle_id, logic_name, logic_writer, logic_ver,"
            		+ " result, date, start_time, end_time, first_second FROM logic"
            		+ " INNER JOIN battle_result ON logic.logic_id = battle_result.logic_id"
            		+ " where battle_result.battle_id = ?;";
            stmt = con.prepareStatement(detailSQL);
            stmt.setString(1, id);
            // SQL実行
            rs =stmt.executeQuery();
//
//            DateTime dt;

            LogicInfoUtil liu = new LogicInfoUtil();

			// 取得データをListにセット
			while (rs.next()) {
				BattleDetailBean detailBn = new BattleDetailBean();
				detailBn.setBattle_id(rs.getInt("battle_id"));
				detailBn.setLogic_name(rs.getString("logic_name"));
				detailBn.setLogic_writer(rs.getString("logic_writer"));
				detailBn.setLogic_ver(rs.getString("logic_ver"));
				detailBn.setResult(rs.getString("result"));
				//ハイフンやコロンでsplitし、個別にリストへ格納する

//	            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
//	            Date date = sdFormat.parse(rs.getString("date"));
//
//	            dt = liu.dateTime(rs.getString("date"));
//
//	            System.out.println(date);
//	            System.out.println(dt);

				//複数のsplitで分けられた戻り値を取得
	            LogicInfoUtil sp = liu.splitMethod(rs.getString("date"),
	            		rs.getString("start_time"), rs.getString("end_time"));

//	            System.out.println(sp.year);
//	            System.out.println(sp.month);
//	            System.out.println(sp.day);

//				String[] battleDaySpl = rs.getString("date").split("-");
//				detailBn.setYear(battleDaySpl[0]);
//				detailBn.setMonth(battleDaySpl[1]);
//				detailBn.setDay(battleDaySpl[2]);


				detailBn.setYear(sp.year);
				detailBn.setMonth(sp.month);
				detailBn.setDay(sp.day);

				detailBn.setStart_hour(sp.startHour);
				detailBn.setStart_min(sp.startMin);
				detailBn.setStart_sec(sp.startSec);

				detailBn.setEnd_hour(sp.endHour);
				detailBn.setEnd_min(sp.endMin);
				detailBn.setEnd_sec(sp.endSec);
				detailList.add(detailBn);
			}
        }catch(Exception e){
            // 例外をthrow
            throw e;
        } finally {
            if(rs != null) {
            	rs.close();
            }
            if(stmt != null) {
            	stmt.close();
            }
            if(con != null) {
                con.close();
            }
        }
        return detailList;
    }
}