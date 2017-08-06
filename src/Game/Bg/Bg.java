package Game.Bg;

import java.awt.*;

/**
 * Created by bakanaouji on 2017/07/30.
 * 背景を表すクラス．
 */
public class Bg {
	/**
	 * コンストラクタ．
	 *
	 * @param aComp コンポーネント
	 */
	public Bg(final Game.Component aComp) {
		// コンポーネント
		mComp = aComp;
		// 画像読み込み
		if (mComp.viewer().paintPanel() != null) {
			mImage = mComp.viewer().paintPanel().getToolkit().getImage("../resource/Image/bg.png");
		} else {
			mImage = null;
		}
	}

	/**
	 * 描画を行うメソッド．
	 */
	public void draw() {
		final Graphics2D g2 = mComp.viewer().paintPanel().create();
		g2.drawImage(mImage, 0, 0, mComp.viewer().paintPanel());
	}

	// コンポーネント
	private final Game.Component mComp;
	// 画像ハンドル
	private final Image mImage;
}
