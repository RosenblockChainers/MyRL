package Game.Hero;

import Game.Component;

/**
 * Created by bakanaouji on 2017/07/23.
 * プレイヤーを管理するマネージャー．
 */
public class HeroManager {
	/**
	 * コンストラクタ．
	 *
	 * @param aComp コンポーネント
	 */
	public HeroManager(final Component aComp) {
		// 画面中央にプレイヤーを初期化
		mHero = new Hero(aComp, 500.0, 500.0, 0.0);
	}

	/**
	 * プレイヤーを取得するメソッド．
	 *
	 * @return プレイヤー
	 */
	public Hero hero() {
		return mHero;
	}

	/**
	 * プレイヤーの更新を行うメソッド．
	 *
	 * @param aAction 行動
	 * @return プレイヤーが死亡しているかどうか
	 */
	public boolean update(final Hero.Action aAction) {
		return mHero.update(aAction);
	}

	/**
	 * プレイヤーの描画を行うメソッド．
	 */
	public void draw() {
		mHero.draw();
	}

	/**
	 * プレイヤーをリセットするメソッド．
	 * ゲームをプレイする度に最初に呼び出される．
	 */
	public void reset() {
		mHero.pos().value(0, 500.0);
		mHero.pos().value(1, 500.0);
		mHero.arrive();
	}

	// プレイヤー
	private final Hero mHero;
}
