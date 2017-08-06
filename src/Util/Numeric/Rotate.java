package Util.Numeric;

/**
 * Created by bakanaouji on 2017/07/23.
 * 回転角を表すクラス．
 */
public class Rotate {
	/**
	 * コンストラクタ
	 *
	 * @param aRot 初期回転角
	 */
	public Rotate(final double aRot) {
		mRot = aRot;
	}

	/**
	 * 回転角を取得
	 *
	 * @return 回転角
	 */
	public double angle() {
		return mRot;
	}

	// 回転角（ラジアン）
	private double mRot;
}
