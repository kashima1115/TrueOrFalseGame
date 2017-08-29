package client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import brain.Brain;
import brain.BrainBean;

/**
 * Brainにアクセスするクラスです。Brainのタイムアウトに対応します.
 * @author hatsugai
 *
 */

public class AccessBrainThreadMode{
	/**
	 * ロジック情報を取得
	 * @return Beanで返します
	 */
	public List<BrainBean> getLogicInfo(){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Future<List<BrainBean>> future = exec.submit(new LogicInfo());
		try{
			List<BrainBean> bb;
			//ここでタイムアウトの時間を設定しています。
			bb = future.get(60, TimeUnit.SECONDS);
			return bb;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			exec.shutdown();
			try{
				exec.awaitTermination(5, TimeUnit.SECONDS);
			}catch(InterruptedException e){
				exec.shutdownNow();
			}
		}
	}

	/**
	 * 指し手情報を取得
	 * @param loc 盤面情報です。
	 * @return 指し手情報です。beanで返します
	 */
	public List<BattleInfoBean> getLocation(String[][] loc){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Future<List<BattleInfoBean>> future = exec.submit(new Location(loc));
		try{
			List<BattleInfoBean> bib;
			//ここでタイムアウトの時間を設定しています。
			bib= future.get(60, TimeUnit.SECONDS);
			return bib;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			exec.shutdown();
			try{
				exec.awaitTermination(5, TimeUnit.SECONDS);
			}catch(InterruptedException e){
				exec.shutdownNow();
			}
		}
	}

}

class LogicInfo implements Callable<List<BrainBean>>{
	@Override
	public List<BrainBean> call() throws Exception{
		Brain bra = new Brain();
		List<BrainBean> logList = bra.logicInfo();
		return logList;
	}
}

class Location implements Callable<List<BattleInfoBean>>{
	String[][] loc;
	public Location(String[][] loc){
		this.loc = loc;
	}
	@Override
	public List<BattleInfoBean> call() throws Exception{
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
