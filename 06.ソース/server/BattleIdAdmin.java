package server;

import java.sql.SQLException;

/**
 * 試合ID採番・取得
 * @author kanayama
 *
 */
class BattleIdAdmin {

/**
 *	DBに存在する直近試合の試合IDを元に現在試合の試合IDを採番
 * @return 採番した試合ID
 * @throws SQLException
 */
	static int getBattleID(DbInsert dbi) throws SQLException{
		int battleId=0;
		try{

			//直近試合IDを取得
			battleId=dbi.getFormerId();

			//採番
			battleId++;

		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}

		return battleId;
	}
}
