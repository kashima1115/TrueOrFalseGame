package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**DBアクセスクラス
 * @author kanayama
 *
 */
class DbOperation {
	// プロファイル(パラメータ)読込み定義
	// プロパティファイルバンドル
	private final ResourceBundle bundle=ResourceBundle.getBundle("config");

	// パラメータ取得
	private final String driver=this.bundle.getString("driverClassName");
	private final String url=this.bundle.getString("url");
	private final String user=this.bundle.getString("username");
	private final String password= this.bundle.getString("password");

	//プリペアドステートメント関連の変数
	private  Connection con=null;
	private PreparedStatement logicPstate=null;
	private PreparedStatement logicPstateLook=null;
	private PreparedStatement locationPstate=null;
	private PreparedStatement resultPstate=null;
	private PreparedStatement logicIdPstate=null;
	private PreparedStatement formerIdPstate=null;

	//sql文
	//DBにクライアントのロジック情報が登録されているか否かを検索
	private final static String logicLookSql="select count(*) from logic "+
			"where logic_name=? and logic_writer=? and logic_ver=?";

	//DBにロジック情報を登録
	private final static String logicInsertSql="insert into logic "+
			"(logic_name,logic_writer,logic_ver) "+
			"values (?,?,?)";

	//DBに指し手情報を登録
	private final static String insertLocationSql="insert into location "+
			"(battle_id,logic_id,location_x,location_y,turn,play_start,play_end) value "+
			"(?,?,?,?,?,?,?)";

	//DBに試合結果を登録
	private final static String resultInsertSql="insert into battle_result value "+
			"(?,?,?,?,?,?,?)";

	//各ロジックのIDを取得
	private final static String logicIdGetSql="select logic_id from logic "+
			"where logic_name=? and logic_writer=? and logic_ver=?";

	//直近試合IDを検索
	private final static String formerIdGetSql="select max(battle_id) from battle_result";



	/**
	 *  ロジック情報DB登録メソッド
	 *  @param logicList ロジック情報リスト
	 *  @throws SQLException
	 *  @return なし
	 */
	void insertLogic(LogicInfoBean lib) throws SQLException {
		// データベースへの検索処理*****************************************************/
		ResultSet rset=null;

		//DBにレコードの存在なし
		final int NOT_HAVE=0;

		try{

			//オートコミット解除
			this.con.setAutoCommit(false);

			//1度もプリコンパイルされていないならば、プリコンパイルを行う
			if(this.logicPstateLook==null){
				this.logicPstateLook=this.con.prepareStatement(DbOperation.logicLookSql);
			}

			//プリペアドステートメントの？の値をセット
			this.logicPstateLook.setString(1, lib.getLogicName());
			this.logicPstateLook.setString(2, lib.getWriter());
			this.logicPstateLook.setString(3, lib.getVersion());

			//sql文実行
			rset=this.logicPstateLook.executeQuery();

			//レコードに登録されているか確認
			int have=0;
			while(rset.next()){
				have=rset.getInt("count(*)");
			}

			//レコードに登録されていれば、以下の登録処理を行わない
			if(have!=NOT_HAVE){
				return;
			}

			//1度もプリコンパイルされてなければ、プリコンパイルを行う
			if(this.logicPstate==null){
				this.logicPstate=this.con.prepareStatement(DbOperation.logicInsertSql);
			}

			//プリペアドステートメントの？の値をセット
			this.logicPstate.setString(1, lib.getLogicName());
			this.logicPstate.setString(2, lib.getWriter());
			this.logicPstate.setString(3, lib.getVersion());

			//sql実行
			this.logicPstate.executeUpdate();

			//コミット
			this.con.commit();

		}catch(SQLException e){
			e.printStackTrace();

			//例外発生時ロールバック
			if(this.con!=null){
				this.con.rollback();
			}
			throw e;
		}finally{
			//クローズ処理
			if(rset!=null){
				rset.close();
			}
		}
	}

	/**指し手情報登録メソッド
	 * @param lcib 指し手情報のBean
	 * @throws SQLException
	 */
	 void insertLocation(LocationInfoBean lcib) throws SQLException{
		// データベースへの検索処理*****************************************************/
		try{

			//オートコミット解除
			this.con.setAutoCommit(false);


			//1度もプリコンパイルされていなければ、プリコンパイル
			if(this.locationPstate==null){
				this.locationPstate=this.con.prepareStatement(DbOperation.insertLocationSql);
			}

			//登録値の入力
			this.locationPstate.setInt(1, lcib.getBattleId());
			this.locationPstate.setInt(2, lcib.getLogicId());
			this.locationPstate.setInt(3, lcib.getLocationX());
			this.locationPstate.setInt(4, lcib.getLocationY());
			this.locationPstate.setInt(5, lcib.getTurn());
			this.locationPstate.setString(6, lcib.getPlayStart());
			this.locationPstate.setString(7, lcib.getPlayEnd());

			//sql実行
			this.locationPstate.executeUpdate();

			//コミット
			this.con.commit();


		}catch(Exception e){
			e.printStackTrace();

			//例外発生時ロールバック
			if(this.con!=null){
				this.con.rollback();
			}
			throw e;
		}
	}

	/**対戦結果を登録するメソッド
	 * @param battleId 試合ID
	 * @param startTime 試合開始時刻
	 * @param endTime 試合終了時刻
	 * @param result 試合結果
	 * @param logicId ロジックID
	 * @param firstSecond 先攻・後攻ラベル
	 * @return なし
	 * @throws SQLException
	 *
	 */
	 void insertResult(int battleId,String startTime,String endTime,String result,
			int logicId,String startDate,String firstSecond) throws SQLException{
		// データベースへの検索処理*****************************************************/

		try{
			//オートコミット解除
			this.con.setAutoCommit(false);

			//1度もプリコンパイルされていないのなら、プリコンパイル
			if(this.resultPstate==null){
				this.resultPstate=this.con.prepareStatement(DbOperation.resultInsertSql);
			}

			//値の入力
			this.resultPstate.setInt(1,battleId);
			this.resultPstate.setInt(2, logicId);
			this.resultPstate.setString(3, result);
			this.resultPstate.setString(4, startDate);
			this.resultPstate.setString(5, startTime);
			this.resultPstate.setString(6,endTime);
			this.resultPstate.setString(7,firstSecond);

			//sql実行
			this.resultPstate.executeUpdate();

			//コミット
			this.con.commit();

		}catch(SQLException e){
			e.printStackTrace();

			//例外発生時ロールバック
			if(this.con!=null){
				this.con.rollback();
			}

			throw e;
		}
	}

	/**
	 *ロジックIDをDBから取得
	 *@param ib ロジック情報Bean
	 *@return ロジックID
	 *@throws SQLException
	 */
	int getLogicId(LogicInfoBean lb) throws SQLException{
		// データベースへの検索処理*****************************************************/
		ResultSet rset=null;

		//返り値用変数
		int logicId=0;

		try{

			//1度もプリコンパイルが為されていないのなら、プリコンパイル
			if(this.logicIdPstate==null){
				this.logicIdPstate=this.con.prepareStatement(DbOperation.logicIdGetSql);
			}

			//検索条件のセット
			this.logicIdPstate.setString(1, lb.getLogicName());
			this.logicIdPstate.setString(2, lb.getWriter());
			this.logicIdPstate.setString(3, lb.getVersion());

			//sql実行
			rset=this.logicIdPstate.executeQuery();



			while(rset.next()){
				logicId=rset.getInt("logic_id");
			}


		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}finally{
			//クローズ処理
			if(rset!=null){
				rset.close();
			}

		}
		return logicId;
	}

	/**
	 * 直近試合をDBから取得
	 * @return 直近試合ID
	 * @throws SQLException
	 */
	int getFormerId() throws SQLException{
		// データベースへの検索処理*****************************************************/
		ResultSet rset=null;
		int battleId=0;

		try{
			if(this.formerIdPstate==null){
				this.formerIdPstate=this.con.prepareStatement(DbOperation.formerIdGetSql);
			}


			//sql実行
			rset=this.formerIdPstate.executeQuery();

			//直近試合ID取得
			while(rset.next()){
				battleId=rset.getInt("max(battle_id)");
			}
		}catch(SQLException e){

			e.printStackTrace();
			throw e;

		}finally{

			//クローズ処理
			if(rset!=null){
				rset.close();
			}

		}
		return battleId;
	}

	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void  connect() throws SQLException,ClassNotFoundException{
		try{

			// JDBCドライバロード
			Class.forName(this.driver);

			// データベース接続
			this.con = DriverManager.getConnection(this.url, this.user, this.password);


		}catch(SQLException e){

			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw e;
		}

	}

	/**
	 *各種クローズ処理を行う
	 * @throws SQLException
	 */
	void disconnect() throws SQLException{
		try{
			if(this.con!=null){
				this.con.close();
			}

			if(this.logicPstate!=null){
				this.logicPstate.close();
			}

			if(this.logicPstate!=null){
				this.logicPstateLook.close();
			}

			if(this.locationPstate!=null){
				this.locationPstate.close();
			}

			if(this.resultPstate!=null){
				this.resultPstate.close();
			}

			if(this.logicIdPstate!=null){
				this.logicIdPstate.close();
			}

			if(this.formerIdPstate!=null){
				this.formerIdPstate.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}

	}


}
