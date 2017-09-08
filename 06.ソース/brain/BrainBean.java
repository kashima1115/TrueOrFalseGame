package brain;
/**
 * Brainの情報を保管するBeanです.
 * @author hatsugai
 *
 */
public class BrainBean {
	/**
	 * ロジック名を保管します
	 */
	private String logicName;
	/**
	 * ロジックのバージョン情報を保管します。DBに入れるときに数字と半角のピリオドの文字列を想定しています
	 */
	private String logicVersion;
	/**
	 * ロジックの作成者の情報を保管します
	 */
	private String writer;
	public String getLogicName() {
		return logicName;
	}
	public void setLogicName(String logicName) {
		this.logicName = logicName;
	}
	public String getLogicVersion() {
		return logicVersion;
	}
	public void setLogicVersion(String logicVersion) {
		this.logicVersion = logicVersion;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
}
