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
* 試合結果取得クラス
*
* @author arahari
* @version 1.0
*/

public class BattleResultDBAccess {

	/**
	 * @return
	 *
	 * ロジック情報と試合結果をリストに格納して返す
	 * ロジック情報と試合結果取得は、先攻後攻を決めるため指し手情報も参照せねばならず
	 * 3つのテーブルを内部結合し1手目と2手目の情報を取得している
	 *
	 * @throws Exception
	 * 取得に問題があった場合に起こり得る例外
	 */
	Context context;
	DataSource ds;
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;

	protected List<BattleResultBean> BattleResultBean() throws Exception {

        // データベースへの検索処理**************************************************/
		// datasourceを使用
		this.context = new InitialContext();
	    ds = (DataSource)context.lookup("java:comp/env/jdbc/library");
	    con = ds.getConnection();
        List<BattleResultBean> resultList = new ArrayList<BattleResultBean>();

		try {
			// SQL文生成
			String resultSQL = "SELECT battle_result.battle_id, logic_name, logic_writer, logic_ver,"
					+ " result, date, first_second FROM (logic INNER JOIN battle_result ON"
					+ " logic.logic_id = battle_result.logic_id) order by battle_id desc, first_second" ;

			// ステートメント生成
			stmt = con.prepareStatement(resultSQL);
			// SQL実行
			rs = stmt.executeQuery();

			LogicInfoUtil liu = new LogicInfoUtil();

			//先攻のロジック情報と後攻のロジック情報を違うbeanに格納し数を合わせるためダミーを作成する。
			// 取得データをListにセット
			while (rs.next()) {

				BattleResultBean resultBn = new BattleResultBean();
				resultBn.setBattle_id(rs.getInt(1));

				if(rs.getString(7).equals("first")){
					resultBn.setPFlogic_name(rs.getString("logic_name"));
					resultBn.setPFlogic_writer(rs.getString("logic_writer"));
					resultBn.setPFlogic_ver(rs.getString("logic_ver"));
					resultBn.setLogic_name("ダミー");
					resultBn.setLogic_writer("ダミー");
					resultBn.setLogic_ver("ダミー");
				}else{
					resultBn.setLogic_name(rs.getString("logic_name"));
					resultBn.setLogic_writer(rs.getString("logic_writer"));
					resultBn.setLogic_ver(rs.getString("logic_ver"));
					resultBn.setPFlogic_name("ダミー");
					resultBn.setPFlogic_writer("ダミー");
					resultBn.setPFlogic_ver("ダミー");
				}
				//クラスの戻り値を取得
				// 引数を合わせるためダミー時間を代入
	            LogicInfoUtil sp = liu.splitMethod(rs.getString("date"),
	            		"00:00:00", "00:00:00");
	            resultBn.setYear(sp.year);
	            resultBn.setMonth(sp.month);
	            resultBn.setDay(sp.day);
				resultBn.setFirst_second(rs.getString("first_second"));
				resultList.add(resultBn);
			}

		} catch (Exception e) {
			// 例外をthrow
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
            if(con != null) {
                con.close();
            }
		}
		return resultList;
	}
}
