package server;

import java.sql.SQLException;

/**
 * 試合ID採番・取得
 * @author kanayama
 *
 */
public class BattleIdAdmin {

/**
 *
 * @return 採番した試合ID
 * @throws Exception
 */
	public static int getBattleID(DbInsert dbi) throws SQLException{
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
