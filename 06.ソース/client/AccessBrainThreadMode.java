package client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import brain.Brain;
import brain.BrainBean;

/**
 * Brainにアクセスするクラスです。Brainのタイムアウトに対応します.
 * @author hatsugai
 *
 */

public class AccessBrainThreadMode{
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
	 * ロジック情報を取得します.
	 * @return Beanで返します
	 */
	public BrainBean getLogicInfo(){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Future<BrainBean> future = exec.submit(new LogicInfo());
		try{
			BrainBean bb;
			//ここでタイムアウトの時間を設定しています。
			bb = future.get(60, TimeUnit.SECONDS);
			return bb;
		}catch(TimeoutException e){
			System.out.println("時間切れです。");
			return null;
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
	 * 指し手情報を取得します.
	 * @param loc 盤面情報です。
	 * @return 指し手情報です。beanで返します
	 */
	public BattleInfoBean getLocation(String[][] loc){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Future<BattleInfoBean> future = exec.submit(new Location(loc));
		try{
			BattleInfoBean bib;
			//ここでタイムアウトの時間を設定しています。
			bib= future.get(60, TimeUnit.SECONDS);
			return bib;
		}catch(TimeoutException e){
			System.out.println("時間切れです");
			return null;
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

class LogicInfo implements Callable<BrainBean>{
	@Override
	public BrainBean call() throws Exception{
		Brain bra = new Brain();
		BrainBean bb = bra.logicInfo();
		return bb;
	}
}

class Location implements Callable<BattleInfoBean>{
	String[][] loc;
	public Location(String[][] loc){
		this.loc = loc;
	}
	@Override
	public BattleInfoBean call() throws Exception{
		Brain bra = new Brain();
		//指し手情報を保管するための変数を宣言
		String loca = bra.getLocation(loc);
		//BattleInfoBeanに指し手情報を格納
		BattleInfoBean bib = new BattleInfoBean();
		bib.setxAxis(Integer.parseInt(loca.substring(0,1)));
		bib.setyAxis(Integer.parseInt(loca.substring(1)));
		return bib;
	}
}
