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
	 * ロジック情報と試合結果取得は、先攻後攻を決めるため指し手情報も参照せねばならず
	 * 3つのテーブルを内部結合し1手目と2手目の情報を取得している
	 *
	 * @throws Exception
	 * 取得に問題があった場合に起こり得る例外
	 *
	 */







    protected List<LocationBean> LocationBean(String id) throws Exception {


        // データベースへの検索処理**************************************************/
    	// datasourceを使用
         Context context = new InitialContext();
         DataSource ds = (DataSource)context.lookup(
             "java:comp/env/jdbc/library");
         Connection con = ds.getConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<LocationBean> list = new ArrayList<LocationBean>();

		try {


            // SQL文生成
            String sqlstr ="SELECT * FROM location where battle_id = ?";

            stmt = con.prepareStatement(sqlstr);

            stmt.setString(1, id);

            // SQL実行
            rs =stmt.executeQuery();

            int i = 1;

            // 取得データをListにセット
            while(rs.next()) {
            	LocationBean bn = new LocationBean();
                bn.setLocation_x(rs.getInt(4));
                bn.setLocation_y(rs.getInt(5));
                bn.setTurn(rs.getInt(6));

                list.add(bn);

                i++;

            }

            //9手以内に試合が終わった場合も必ずリストに9個データが入るよう調整

            for(int q = i ; q <= 9; q++){

            	LocationBean bn = new LocationBean();
            	bn.setLocation_x(3);
            	bn.setLocation_y(3);

            	list.add(bn);

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
        return list;
    }

    protected List<BattleDetailBean> BattleDetailBean(String id) throws Exception {


        // データベースへの検索処理**************************************************/
    	// datasourceを使用
         Context context = new InitialContext();
         DataSource ds = (DataSource)context.lookup(
             "java:comp/env/jdbc/library");
         Connection con = ds.getConnection();

        PreparedStatement smt = null;
        ResultSet rst = null;

        List<BattleDetailBean> list2 = new ArrayList<BattleDetailBean>();

        try{

            // SQL文生成
            String sqlstr2 = "SELECT battle_result.battle_id, logic_name, logic_writer, "
            		+ "logic_ver, result, date, start_time, end_time, turn"
            		+ " FROM logic INNER JOIN battle_result ON logic.logic_id = battle_result.logic_id"
            		+ " INNER JOIN location ON battle_result.battle_id = location.battle_id "
            		+ " where battle_result.battle_id = ? and battle_result.logic_id ="
            		+ " location.logic_id and turn = 1 "
            		+ " or battle_result.battle_id = ? and battle_result.logic_id ="
            		+ " location.logic_id and turn = 2"
            		+ " order by turn";

            smt = con.prepareStatement(sqlstr2);

            smt.setString(1, id);
            smt.setString(2, id);

            // SQL実行
            rst =smt.executeQuery();

			// 取得データをListにセット
			while (rst.next()) {
				BattleDetailBean bn2 = new BattleDetailBean();
				bn2.setBattle_id(rst.getInt(1));
				bn2.setLogic_name(rst.getString(2));
				bn2.setLogic_writer(rst.getString(3));
				bn2.setLogic_ver(rst.getString(4));
				bn2.setResult(rst.getString(5));
				String[] battleDaySpl = rst.getString(6).split("-");
				bn2.setYear(battleDaySpl[0]);
				bn2.setMonth(battleDaySpl[1]);
				bn2.setDay(battleDaySpl[2]);
				String[] startTime = rst.getString(7).split(":");
				bn2.setStart_hour(startTime[0]);
				bn2.setStart_min(startTime[1]);
				bn2.setStart_sec(startTime[2]);
				String[] endTime = rst.getString(8).split(":");
				bn2.setEnd_hour(endTime[0]);
				bn2.setEnd_min(endTime[1]);
				bn2.setEnd_sec(endTime[2]);

				list2.add(bn2);
			}

        }catch(Exception e){
            // 例外をthrow
            throw e;
        } finally {

            if(rst != null) {
                rst.close();
            }
            if(smt != null) {
                smt.close();
            }

            if(con != null) {
                con.close();
            }
        }
        return list2;
    }
}


