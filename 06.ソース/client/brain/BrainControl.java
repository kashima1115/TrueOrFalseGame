package brain;

/**
 * Brainのインターフェースです.
 * @author hatsugai
 *
 */
public interface BrainControl {
	/**
	 * ロジックの情報（名前・版・作者）を取得します
	 * @return BrainBeanで返します
	 */
	public BrainBean logicInfo();
	/**
	 * 差し手を決めます
	 * @param location 盤面情報です
	 * @return Stringで座標が返されます
	 */
	public String getLocation(String[][] location);

}
