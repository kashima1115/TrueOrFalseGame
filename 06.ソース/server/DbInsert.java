package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**DBアクセスクラス
 * @author kanayama
 *
 */
public class DbInsert {
	private final ResourceBundle bundle;
	private final String driver;
	private final String url;
	private final String user;
	private final String password;
	private Connection con;

	/**
	 * 設定ファイルから取得する値の初期化
	 * @return なし
	 */
	DbInsert(){
		// プロファイル(パラメータ)読込み定義
		// プロパティファイルバンドル
		this.bundle=ResourceBundle.getBundle("config");

		// パラメータ取得
		this.driver=this.bundle.getString("driverClassName");
		this.url=this.bundle.getString("url");
		this.user=this.bundle.getString("username");
		this.password= this.bundle.getString("password");
	}


	/**
	 *  ロジック情報DB登録メソッド
	 *  @param logicList ロジック情報リスト
	 *  @throws Exception
	 *  @return なし
	 */
	public void logicInsert(List<LogicInfoBean> logicList) throws Exception {
		// データベースへの検索処理*****************************************************/
		Connection con = this.con;
		PreparedStatement pStateLook=null;
		PreparedStatement pState = null;
		ResultSet rset=null;

		//DB内に受信値と同じレコードが存在するか否かを表す値を格納
		List<Integer> exist=new ArrayList<Integer>();
		//DBにレコードの存在なし
		final int NOT_HAVE=0;
		//DBにレコードが存在しないロジックの数
		int nhCount=0;
		//DBに登録する必要があるロジック数
		final int HAVE_RECORD=0;

		try{

			//オートコミット解除
			con.setAutoCommit(false);

			//sql文（DBに被りが無いかチェック）
			String sSql="select count(*) from logic "+
						"where logic_name=? and logic_writer=? and logic_ver=?";

			//プリコンパイル
			pStateLook=con.prepareStatement(sSql);

			//受信値取得・DB内探査
			for(LogicInfoBean lb:logicList){
				pStateLook.setString(1, lb.getLogicName());
				pStateLook.setString(2, lb.getCreator());
				pStateLook.setString(3, lb.getVersion());
				//sql文実行
				rset=pStateLook.executeQuery();

				//レコードに登録なしのロジックをカウント
				int have=0;
				while(rset.next()){
					have=rset.getInt("count(*)");
				}


				if(have==NOT_HAVE){
					nhCount++;
				}
				exist.add(have);
			}

			//受信したロジック情報が全て登録済みの場合、メソッドの処理終了
			if(nhCount==HAVE_RECORD){
				return;
			}



			//sql文(DBにロジック情報を登録）
			String iSql="insert into logic "+
						"(logic_name,logic_writer,logic_ver) "+
						"values (?,?,?)";

			//プリコンパイル
			pState=con.prepareStatement(iSql);

			//受信値取得・登録
			for(int i=0;i<logicList.size();i++){
				if(exist.get(i)!=NOT_HAVE){
					continue;
				}
				LogicInfoBean lib=logicList.get(i);
				pState.setString(1, lib.getLogicName());
				pState.setString(2, lib.getCreator());
				pState.setString(3, lib.getVersion());
				pState.executeUpdate();

				if(i<logicList.size()-1){
					if(lib.getLogicName().equals(logicList.get(i+1).getLogicName())){
						if(lib.getCreator().equals(logicList.get(i+1).getCreator())){
							if(lib.getVersion().equals(logicList.get(i+1).getVersion())){
								break;
							}
						}
					}
				}
			}

			//コミット
			con.commit();

		}catch(Exception e){
			e.printStackTrace();
			//例外発生時ロールバック
			if(con!=null){
				con.rollback();
			}
			throw e;
		}finally{
			//クローズ処理
			if(pState!=null){
			pState.close();
			}

			if(pStateLook!=null){
				pStateLook.close();
			}

			if(rset!=null){
				rset.close();
			}
		}
	}

	/**指し手情報登録メソッド
	 * @param lcib 指し手情報のBean
	 * @throws Exception
	 */
	public void locationInsert(LocationInfoBean lcib) throws Exception{
		// データベースへの検索処理*****************************************************/
		Connection con = this.con;
		PreparedStatement pState = null;


		try{

			//オートコミット解除
			con.setAutoCommit(false);


			//sql文（指し手情報登録）
			String iSql="insert into location "+
						"(battle_id,logic_id,location_x,location_y,turn,play_start,play_end) value "+
						"(?,?,?,?,?,?,?)";

			//プリコンパイル
			pState=con.prepareStatement(iSql);

			//登録値の入力
			pState.setInt(1, lcib.getBattleId());
			pState.setInt(2, lcib.getLogicId());
			pState.setInt(3, lcib.getLocationX());
			pState.setInt(4, lcib.getLocationY());
			pState.setInt(5, lcib.getTurn());
			pState.setString(6, lcib.getPlayStart());
			pState.setString(7, lcib.getPlayEnd());

			//sql実行
			pState.executeUpdate();

			//コミット
			con.commit();


		}catch(Exception e){
			e.printStackTrace();

			//例外発生時ロールバック
			if(con!=null){
				con.rollback();
			}

			throw e;
		}finally{

			//クローズ処理
			if(pState!=null){
				pState.close();
			}
		}
	}

	/**対戦結果を登録するメソッド
	 * @param battleId 試合ID
	 * @param startTime 試合開始時刻
	 * @param endTime 試合終了時刻
	 * @param result 試合結果
	 * @param logicId ロジックID
	 * @return なし
	 * @throws Exception
	 *
	 */
	public void resultInsert(int battleId,String startTime,String endTime,String result,
			int logicId,String startDate) throws Exception{
		// データベースへの検索処理*****************************************************/
		Connection con = this.con;
		PreparedStatement pState = null;


		try{
			//オートコミット解除
			con.setAutoCommit(false);

			//sql文（対戦結果登録）
			String iSql="insert into battle_result value "+
						"(?,?,?,?,?,?)";

			//プリコンパイル
			pState=con.prepareStatement(iSql);

			//値の入力
			pState.setInt(1,battleId);
			pState.setInt(2, logicId);
			pState.setString(3, result);
			pState.setString(4, startDate);
			pState.setString(5, startTime);
			pState.setString(6,endTime);

			//sql実行
			pState.executeUpdate();

			//コミット
			con.commit();

		}catch(Exception e){
			e.printStackTrace();

			//例外発生時ロールバック
			if(con!=null){
				con.rollback();
			}

			throw e;
		}finally{
			//クローズ処理
			if(pState!=null){
				pState.close();
			}
		}

	}

	/**
	 *ロジックIDをDBから取得
	 *@return ロジックID
	 *@throws Exception
	 */
	public int getLogicId(LogicInfoBean lb) throws Exception{
		// データベースへの検索処理*****************************************************/
		Connection con = this.con;
		PreparedStatement pState = null;
		ResultSet rset=null;

		//返り値用変数
		int logicId=0;

		try{

			//sql文（ロジックIDの取得）
			String sSql="select logic_id from logic "+
						"where logic_name=? and logic_writer=? and logic_ver=?";

			//プリコンパイル
			pState=con.prepareStatement(sSql);

			//検索条件のセット
			pState.setString(1, lb.getLogicName());
			pState.setString(2, lb.getCreator());
			pState.setString(3, lb.getVersion());

			//sql実行
			rset=pState.executeQuery();



			while(rset.next()){
				logicId=rset.getInt("logic_id");
			}


		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			//クローズ処理
			if(pState!=null){
				pState.close();
			}

			if(rset!=null){
				rset.close();
			}

		}
		return logicId;
	}

	/**
	 * 直近試合をDBから取得
	 * @return 直近試合ID
	 * @throws Exception
	 */
	public int getFormerId() throws Exception{
		// データベースへの検索処理*****************************************************/
		Connection con = this.con;
		Statement state = null;
		ResultSet rset=null;
		int battleId=0;

		try{
			//sql文（直近試合ID取得)
			String sSql="select max(battle_id) from battle_result";

			//ステートメント生成
			state=con.createStatement();

			//sql実行
			rset=state.executeQuery(sSql);

			//直近試合ID取得
			while(rset.next()){
				battleId=rset.getInt("max(battle_id)");
			}
		}catch(Exception e){

			e.printStackTrace();
			throw e;

		}finally{

			//クローズ処理
			if(state!=null){
				state.close();
			}

			if(rset!=null){
				rset.close();
			}

		}
		return battleId;
	}

	/**
	 *
	 * @throws Exception
	 */
	public void  connect() throws Exception{
		Connection con = null;
		try{

			// JDBCドライバロード
			Class.forName(this.driver);

			// データベース接続
			con = DriverManager.getConnection(this.url, this.user, this.password);

			//フィールド変数にアクセス
			this.con=con;

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 *
	 * @throws Exception
	 */
	public void disconnect() throws Exception{
		try{
			if(this.con!=null){
				this.con.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}


}
