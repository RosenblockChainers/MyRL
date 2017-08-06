package Game.Drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by bakanaouji on 2017/07/23.
 * ペイントパネル．
 * 描画用のキャンパスのようなもの．
 */
public class PaintPanel extends JPanel {
	/**
	 * コンストラクタ．
	 */
	public PaintPanel() {
		// パネルの大きさを指定して初期化
		mImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		mGraphics = mImage.createGraphics();
		setPreferredSize(new Dimension(1000, 1000));
	}

	/**
	 * パネルをクリアするメソッド．
	 */
	public void clear() {
		// 背景色は黒
		mGraphics.setBackground(Color.black);
		mGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());
	}

	/**
	 * 基本的な描画用のグラフィックスを取得するメソッド．
	 *
	 * @return グラフィックス
	 */
	public Graphics2D graphics() {
		return mGraphics;
	}

	/**
	 * 新しい描画用のグラフィックスを生成し取得するメソッド．
	 *
	 * @return グラフィックス
	 */
	public Graphics2D create() {
		return mImage.createGraphics();
	}

	/**
	 * バッファイメージを描画するメソッド．
	 *
	 * @param aGraphics 描画先のグラフィックス
	 */
	@Override
	public void paintComponent(final Graphics aGraphics) {
		// JPanelのpaintComponentを呼び出し．
		super.paintComponent(aGraphics);
		// バッファイメージの描画
		aGraphics.drawImage(mImage, 0, 0, this);
	}

	// バッファイメージ
	private final BufferedImage mImage;
	// グラフィックス
	private final Graphics2D mGraphics;
}
