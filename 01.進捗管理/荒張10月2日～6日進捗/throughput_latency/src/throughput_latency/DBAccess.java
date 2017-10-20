package throughput_latency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DBAccess {
	int id;
	Connection con;
	PreparedStatement stmt;
	Statement stm;
	ResultSet rs;
	final static String insert = "insert into logic (logic_name, logic_writer, logic_ver)"
			+ " values ('レイテンシ', 'スループット', '10')";
	final static String select = "select max(logic_id) from logic";
	final static String delete = "delete from logic order by logic_id desc limit 1";

	protected int access(int loop) throws Exception {
		// プロファイル(パラメータ)読込み定義
		// プロパティファイルバンドル
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		int i = 1;
		try {
			// パラメータ取得
			String driver = bundle.getString("driverClassName");
			String url = bundle.getString("url");
			String user = bundle.getString("username");
			String password = bundle.getString("password");
			// JDBCドライバロード
			Class.forName(driver);
			// データベース接続
			con = DriverManager.getConnection(url, user, password);
			con.setAutoCommit(false);
			for (i = 0; i <= loop; i++) {
				// ステートメント生成
				stm = con.createStatement();
				// SQL実行
				stm.executeUpdate(insert);
				stm.close();
				// ステートメント生成
				stmt = con.prepareStatement(select);
				// SQL実行
				rs = stmt.executeQuery();
				while (rs.next()) {
					id = rs.getInt(1);
				}
				rs.close();
				stmt.close();
				// ステートメント生成
				stm = con.createStatement();
				// SQL実行
				stm.executeUpdate(delete);
			}
			con.commit();
		} catch (Exception e) {
			// 例外をthrow
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		}

		return id;
	}
}
