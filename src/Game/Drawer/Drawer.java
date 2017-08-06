package Game.Drawer;

import javax.swing.*;

/**
 * Created by bakanaouji on 2017/07/24.
 * 描画を行うDrawerクラス．
 */
public class Drawer {
	/**
	 * コンストラクタ．
	 * 描画が有効なときのみ，描画を行う．
	 * 学習時は時間削減のため描画が無効．
	 *
	 * @param aIsDraw 描画が有効かどうか
	 */
	public Drawer(final boolean aIsDraw) {
		mIsDraw = aIsDraw;
		// 描画が有効なときのみ，描画用のウィンドウを作成．
		if (aIsDraw) {
			mFrame = new JFrame();
			// フレームサイズの決定
			mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			mFrame.setTitle("My Game");
			mFrame.setBounds(0, 0, 1000, 1000);
			mFrame.setResizable(false);
			mFrame.setVisible(true);

			mPaintPanel = new PaintPanel();
			JPanel panel = new JPanel();
			panel.add(mPaintPanel);
			mFrame.add(panel);
			mFrame.setVisible(true);
			mFrame.requestFocusInWindow();
		} else {
			mFrame = null;
			mPaintPanel = null;
		}
	}

	/**
	 * 描画用のパネルを取得．
	 *
	 * @return ペイントパネル
	 */
	public PaintPanel paintPanel() {
		return mPaintPanel;
	}

	/**
	 * 描画を行うメソッド．
	 */
	public void draw() {
		// 描画が無効な場合，何もしない
		if (!mIsDraw) {
			return;
		}
		// 描画．
		mFrame.repaint();
		// 描画後は数ミリ秒待機
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// フレーム（ウィンドウ）
	private final JFrame mFrame;
	// ペイントパネル（パネルに配置）
	// このパネルに描画を行う．
	private final PaintPanel mPaintPanel;
	// 描画が有効かどうか
	private final boolean mIsDraw;
}
