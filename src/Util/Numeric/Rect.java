package Util.Numeric;

/**
 * Created by bakanaouji on 2017/07/23.
 * 四角形を表すクラス．
 */
public class Rect {
	/**
	 * コンストラクタ．
	 *
	 * @param aLeft   左幅
	 * @param aRight  右幅
	 * @param aTop    上幅
	 * @param aBottom 下幅
	 */
	public Rect(final double aLeft, final double aRight, final double aTop, final double aBottom) {
		mLeft = aLeft;
		mRight = aRight;
		mTop = aTop;
		mBottom = aBottom;
	}

	/**
	 * 左幅を取得するメソッド．
	 * @return 左幅
	 */
	public double left() {
		return mLeft;
	}

	/**
	 * 右幅を取得するメソッド．
	 * @return 右幅
	 */
	public double right() {
		return mRight;
	}

	/**
	 * 上幅を取得するメソッド．
	 * @return 上幅
	 */
	public double top() {
		return mTop;
	}

	/**
	 * 下幅を取得するメソッド．
	 * @return 下幅
	 */
	public double bottom() {
		return mBottom;
	}

	// 中心からの左，右，上，下の幅
	private double mLeft;
	private double mRight;
	private double mTop;
	private double mBottom;
}
