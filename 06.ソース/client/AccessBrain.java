package client;

import brain.Brain;
import brain.BrainBean;

/**
 * Brainにアクセスするクラスです.
 * @author hatsugai
 * @version 0.1
 */

public class AccessBrain{
	private Brain bra = null;

	/**
	 * Brainをインスタンス化します.AccessBrain使用時に実行してください.
	 */
	public void createBrain(){
		if(bra == null){
			bra = new Brain();
		}
	}
	/**
	 * ロジックの情報を取得するためにBrainにアクセスするメソッドです.
	 * @return ロジック情報が格納されたBeanです
	 */
	public BrainBean getLogicInfo(){
		BrainBean bb = bra.logicInfo();
		return bb;
	}

	/**
	 * 指し手情報を取得します.
	 * @param loc 盤面情報です。3*3を想定しています
	 * @return 指し手情報が格納されたBeanです
	 */

	public BattleInfoBean getLocation(String[][] loc){
		//指し手情報を保管するための変数を宣言
		String loca = bra.getLocation(loc);
		//BattleInfoBeanに指し手情報を格納
		BattleInfoBean bib = convertFromString(loca);
		return bib;
	}

	/**
	 * Brainから送られたString型の指し手情報をBattleInfoBeanに変換します.
	 * @param location Brainから返された指し手情報です。
	 * @return 指し手情報の入ったBattleInfoBeanです。
	 */
	private BattleInfoBean convertFromString(String location){
		BattleInfoBean bib = new BattleInfoBean();
		bib.setxAxis(Integer.parseInt(location.substring(0,1)));
		bib.setyAxis(Integer.parseInt(location.substring(1)));
		return bib;
	}

}
