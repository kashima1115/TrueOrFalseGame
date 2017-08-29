package client;

import java.util.ArrayList;
import java.util.List;

import brain.Brain;
import brain.BrainBean;

/**
 * Brainにアクセスするクラスです.
 * @author hatsugai
 *
 */

public class AccessBrain{
	/**
	 * ロジックの情報を取得するためにBrainにアクセスするメソッドです
	 * @return ロジック情報が格納されたBeanです
	 */
	public List<BrainBean> getLogicInfo(){
		Brain bra = new Brain();
		List<BrainBean> logList = bra.logicInfo();
		return logList;
	}

	/**
	 * @param loc 盤面情報です。3*3を想定しています
	 * @return 指し手情報が格納されたBeanです
	 */

	public List<BattleInfoBean> getLocation(String[][] loc){
		Brain bra = new Brain();
		//指し手情報を保管するための変数を宣言
		String loca = bra.getLocation(loc);
		//BattleInfoBeanに指し手情報を格納
		List<BattleInfoBean> bl = new ArrayList<BattleInfoBean>();
		BattleInfoBean bib = new BattleInfoBean();
		bib.setxAxis(Integer.parseInt(loca.substring(0,1)));
		bib.setyAxis(Integer.parseInt(loca.substring(1)));
		bl.add(bib);
		return bl;
	}

}
