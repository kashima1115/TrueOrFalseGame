package server;

import net.sf.json.JSONObject;

/**
 * 試合判定クラス
 * @author kanayama
 *
 */
public class JudgeMatch {
	private LocationInfoBean lifb;
	/**
	 * ルール判定（盤面と指し手の被りを確認）
	 * @param turn ターン数
	 * @param location 盤面
	 * @param gameInfo 受信した指し手情報
	 * @param battleId 試合ID
	 * @param playStart 処理開始時間
	 * @param playEnd 処理終了時間
	 * @param logicId ロジックID
	 * @return 盤面と指し手に被りがある場合false、ない場合trueを返す
	 */
	public boolean ruleJudge(int turn,String[][] location,JSONObject gameInfo,int battleId,
			String playStart,String playEnd,int logicId){

		JSONObject locationInfo=gameInfo.getJSONObject("gameInfo");

		//指し手情報を取得
		int locationX=locationInfo.getInt("xAxis");
		int locationY=locationInfo.getInt("yAxis");

		//被り判定
		if(!location[locationX][locationY].equals("_")){
			return false;
		}

		//指し手情報クラスに格納
		this.lifb=new LocationInfoBean();

		lifb.setBattleId(battleId);
		lifb.setLocationX(locationX);
		lifb.setLocationY(locationY);
		lifb.setLogicId(logicId);
		lifb.setPlayEnd(playEnd);
		lifb.setPlayStart(playStart);
		lifb.setTurn(turn);

		return true;
	}

	/**
	 * @return 指し手情報Beanクラス
	 */
	public LocationInfoBean getLocationInfo(){
		return this.lifb;
	}

	/**
	 * 勝敗判定
	 * @param location 最新の盤面
	 * @return 判定が勝利の場合"win"、引き分けの場合"draw"、継戦の場合"continue"を返す
	 */
	public String battleJudge(String[][] location){

		//勝利条件充足カウンター
		int trueLineX0=0;
		int trueLineX1=0;
		int trueLineX2=0;
		int falseLineX0=0;
		int falseLineX1=0;
		int falseLineX2=0;
		int trueLineY0=0;
		int trueLineY1=0;
		int trueLineY2=0;
		int falseLineY0=0;
		int falseLineY1=0;
		int falseLineY2=0;
		int trueSameNumLine=0;
		int falseSameNumLine=0;
		int trueSum2Line=0;
		int falseSum2Line=0;

		//勝利条件判別用変数
		final int LINE_X0=0;
		final int LINE_X1=1;
		final int LINE_X2=2;
		final int LINE_Y0=0;
		final int LINE_Y1=1;
		final int LINE_Y2=2;
		final int SUM_XY=2;

		//勝利条件必要要素数
		final int WIN_ELEMENTS=3;

		//プレイヤー判別
		final String FIRST_PLAYER="○";
		final String SECOND_PLAYER="×";
		final String NOT_FILL="_";

		//返すオブジェクト
		String judgeBattle="draw";

		//勝敗条件要素カウント
		for(int y=0;y<location[LINE_X0].length;y++){
			if(location[LINE_X0][y].equals(FIRST_PLAYER)){
				trueLineX0++;
			}else if(location[LINE_X0][y].equals(SECOND_PLAYER)){
				falseLineX0++;
			}
		}

		for(int y=0;y<location[LINE_X1].length;y++){

			if(location[LINE_X1][y].equals(FIRST_PLAYER)){
				trueLineX1++;
			}else if(location[LINE_X1][y].equals(SECOND_PLAYER)){
				falseLineX1++;
			}

		}

		for(int y=0;y<location[LINE_X2].length;y++){

			if(location[LINE_X2][y].equals(FIRST_PLAYER)){
				trueLineX2++;
			}else if(location[LINE_X2][y].equals(SECOND_PLAYER)){
				falseLineX2++;
			}

		}

		for(int x=0;x<location.length;x++){

			if(location[x][LINE_Y0].equals(FIRST_PLAYER)){
				trueLineY0++;
			}else if(location[x][LINE_Y0].equals(SECOND_PLAYER)){
				falseLineY0++;
			}

			if(location[x][LINE_Y1].equals(FIRST_PLAYER)){
				trueLineY1++;
			}else if(location[x][LINE_Y1].equals(SECOND_PLAYER)){
				falseLineY1++;
			}

			if(location[x][LINE_Y2].equals(FIRST_PLAYER)){
				trueLineY2++;
			}else if(location[x][LINE_Y2].equals(SECOND_PLAYER)){
				falseLineY2++;
			}
		}

		for(int x=0;x<location.length;x++){

			for(int y=0;y<location[x].length;y++){

				if(location[x][y].equals(FIRST_PLAYER) && x+y==SUM_XY){
					trueSum2Line++;
				}else if(location[x][y].equals(SECOND_PLAYER) && x+y==SUM_XY){
					falseSum2Line++;
				}

				if(location[x][y].equals(FIRST_PLAYER) && x==y){
					trueSameNumLine++;
				}else if(location[x][y].equals(SECOND_PLAYER) && x==y){
					falseSameNumLine++;
				}

				//盤面に空きがあるならば、"continue"を返す
				if(location[x][y].equals(NOT_FILL)){
					judgeBattle="continue";
				}
			}
		}

		/*いずれかの勝利条件を満たせば、"win"を返す
		 *
		 */
		if(trueLineX0==WIN_ELEMENTS||trueLineX1==WIN_ELEMENTS||trueLineX2==WIN_ELEMENTS
				||falseLineX0==WIN_ELEMENTS||falseLineX1==WIN_ELEMENTS||falseLineX2==WIN_ELEMENTS
				||trueLineY0==WIN_ELEMENTS||trueLineY1==WIN_ELEMENTS||trueLineY2==WIN_ELEMENTS
				||falseLineY0==WIN_ELEMENTS||falseLineY1==WIN_ELEMENTS||falseLineY2==WIN_ELEMENTS
				||trueSameNumLine==WIN_ELEMENTS||falseSameNumLine==WIN_ELEMENTS
				||trueSum2Line==WIN_ELEMENTS||falseSum2Line==WIN_ELEMENTS){

			judgeBattle="win";
		}


		//勝利条件・継戦条件が満たされなければ、"draw"を返す
		return judgeBattle;

	}
}
