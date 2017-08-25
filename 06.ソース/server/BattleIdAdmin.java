package server;

/**
 * 試合ID採番・取得
 * @author kanayama
 *
 */
public class BattleIdAdmin {
	private int battleId;
/**
 *
 * @return 採番した試合ID
 * @throws Exception
 */
	public int getBattleID() throws Exception{

		try{
			//DBアクセスクラスをインスタンス化
			DbInsert dbi=new DbInsert();

			//直近試合IDを取得
			this.battleId=dbi.getFormerId();

			//採番
			this.battleId++;

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

		return battleId;
	}
}
