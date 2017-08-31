package server;

/**盤面保持・更新クラス
 *
 * @author kanayama
 *
 */
public class LocationAdmin {
	private String location[][];
	private final int MAX_X;
	private final int MAX_Y;
	private final String FIRST_PLAYER;
	private final String SECOND_PLAYER;
	private final String NOT_FILL;

	/**
	 * コンストラクタ
	 * 盤面を初期化
	 * @return なし
	 */
	LocationAdmin(){
		MAX_X=3;
		MAX_Y=3;
		FIRST_PLAYER="○";
		SECOND_PLAYER="×";
		NOT_FILL="_";

		location=new String[MAX_X][MAX_Y];

		for(int i=0;i<MAX_X;i++){
			for(int j=0;j<MAX_Y;j++){
				location[i][j]=NOT_FILL;
			}
		}
	}

	/**盤面を作成して引き渡す
	 *
	 * @return 初期状態の盤面
	 */
	public String[][] createLocation(){
		return this.location;
	}

	/**盤面を更新して引き渡す
	 *
	 * @param lifb 指し手情報を格納したBean
	 * @return 最新の状態に更新された盤面
	 */
	public String[][] updateLocation(LocationInfoBean lifb){

		if(lifb.getTurn()%2==1){
			location[lifb.getLocationX()][lifb.getLocationY()]=FIRST_PLAYER;
		}else if(lifb.getTurn()%2==0){
			location[lifb.getLocationX()][lifb.getLocationY()]=SECOND_PLAYER;
		}
		return this.location;
	}
}
