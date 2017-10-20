package server;

import net.sf.json.JSONObject;

/**
 * 試合判定クラス
 * @author kanayama
 *
 */
class JudgeMatch {
	/**
	 * ルール判定（盤面と指し手の被りを確認）
	 * @param turn ターン数
	 * @param location 盤面
	 * @param gameInfo 受信した指し手情報
	 * @return 盤面と指し手に被りがあるor存在しない盤面をしていた場合false、それ以外trueを返す
	 */
	static boolean ruleJudge(String[][] location,JSONObject gameInfo){
		if(gameInfo == null){
			return false;
		}

		//指し手情報を取得
		int locationX=gameInfo.getInt("xAxis");
		int locationY=gameInfo.getInt("yAxis");

		//適正値判定
		if(locationX<0 || locationX>2 || locationY<0 || locationY>2){
			return false;
		}

		//被り判定
		if(!location[locationX][locationY].equals("-")){
			return false;
		}else{
			return true;
		}
	}


	/**
	 * 勝敗判定
	 * @param location 最新の盤面
	 * @return 判定が勝利の場合"win"、引き分けの場合"draw"、継戦の場合"continue"を返す
	 */
	static String battleJudge(String[][] location){

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
		final String NOT_FILL="-";

		//返すオブジェクト
		String judgeBattle="draw";

		//勝敗条件要素カウント
		//勝利条件X=0
		for(int y=0;y<location[LINE_X0].length;y++){
			if(location[LINE_X0][y].equals(FIRST_PLAYER)){
				trueLineX0++;
			}else if(location[LINE_X0][y].equals(SECOND_PLAYER)){
				falseLineX0++;
			}
		}

		//勝利条件X=1
		for(int y=0;y<location[LINE_X1].length;y++){

			if(location[LINE_X1][y].equals(FIRST_PLAYER)){
				trueLineX1++;
			}else if(location[LINE_X1][y].equals(SECOND_PLAYER)){
				falseLineX1++;
			}

		}

		//勝利条件X=2
		for(int y=0;y<location[LINE_X2].length;y++){

			if(location[LINE_X2][y].equals(FIRST_PLAYER)){
				trueLineX2++;
			}else if(location[LINE_X2][y].equals(SECOND_PLAYER)){
				falseLineX2++;
			}

		}

		//勝利条件Y=0～2
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

		//勝利条件斜め
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

	/**
	 * 勝敗通知オブジェクト作成
	 * @return 勝敗通知オブジェクト
	 */
	static JSONObject informResult(String result){
		final String WIN="win";
		final String LOSE="lose";
		final String DRAW="draw";

		//JSONObject作成
		JSONObject gameInfo=new JSONObject();

		//勝敗結果別にイベント情報を生成・JSONObjectにセット
		if(result.equals(WIN)){
			gameInfo.accumulate("event", WIN);
		}else if(result.equals(DRAW)){
			gameInfo.accumulate("event", DRAW);
		}else if(result.equals(LOSE)){
			gameInfo.accumulate("event", LOSE);
		}

		return gameInfo;
	}
}
