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
        List<BattleResultBean> list = new ArrayList<BattleResultBean>();

		try {
			// SQL文生成
			String sqlstr = "SELECT battle_result.battle_id, logic_name, logic_writer, logic_ver, result, date, turn"
					 + " FROM (logic INNER JOIN battle_result ON logic.logic_id = battle_result.logic_id)"
					 + " INNER JOIN location ON battle_result.battle_id = location.battle_id"
					 + " where turn = 1 and battle_result.logic_id = location.logic_id or turn = 2"
					 + " and battle_result.logic_id = location.logic_id order by battle_id desc, turn" ;

			// ステートメント生成
			stmt = con.prepareStatement(sqlstr);
			// SQL実行
			rs = stmt.executeQuery();
			//先攻のロジック情報と後攻のロジック情報を違うbeanに格納し数を合わせるためダミーを作成する。
			// 取得データをListにセット
			while (rs.next()) {

				BattleResultBean bn = new BattleResultBean();
				bn.setBattle_id(rs.getInt(1));

				if(rs.getInt(7) == 1){
					bn.setPFlogic_name(rs.getString(2));
					bn.setPFlogic_writer(rs.getString(3));
					bn.setPFlogic_ver(rs.getString(4));
					bn.setLogic_name("ダミー");
					bn.setLogic_writer("ダミー");
					bn.setLogic_ver("ダミー");
				}else{
				bn.setLogic_name(rs.getString(2));
				bn.setLogic_writer(rs.getString(3));
				bn.setLogic_ver(rs.getString(4));
				bn.setPFlogic_name("ダミー");
				bn.setPFlogic_writer("ダミー");
				bn.setPFlogic_ver("ダミー");
				}
				bn.setResult(rs.getString(5));
				String[] battleDaySpl = rs.getString(6).split("-");
				bn.setYear(battleDaySpl[0]);
				bn.setMonth(battleDaySpl[1]);
				bn.setDay(battleDaySpl[2]);
				bn.setTurn(rs.getInt(7));
				list.add(bn);
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
		return list;
	}
}
