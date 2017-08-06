package Game;

import Game.Bg.Bg;
import Game.Drawer.Drawer;
import Game.Hero.HeroManager;
import Game.Spear.SpearManager;

import java.util.Random;


/**
 * Created by bakanaouji on 2017/07/23.
 * コンポーネント．
 * ゲームに必要なオブジェクトを持つ．
 * 各オブジェクトはコンポーネント経由で他のオブジェクトの情報を取得する．
 */
public class Component {
	/**
	 * コンストラクタ．
	 *
	 * @param aRandom      乱数生成器
	 * @param aSpearMaxNum ヤリを同時に配置する最大数
	 * @param aIsDraw      描画を行うかどうか
	 */
	public Component(final Random aRandom, final int aSpearMaxNum, final boolean aIsDraw) {
		fMDrawer = new Drawer(aIsDraw);
		mHeroManager = new HeroManager(this);
		mSpearManager = new SpearManager(this, aSpearMaxNum, aRandom);
		mBg = new Bg(this);
	}

	/**
	 * プレイヤーのマネージャーを取得するメソッド．
	 *
	 * @return プレイヤーのマネージャー
	 */
	public HeroManager heroManager() {
		return mHeroManager;
	}

	/**
	 * ヤリのマネージャーを取得するメソッド．
	 *
	 * @return ヤリのマネージャー
	 */
	public SpearManager spearManager() {
		return mSpearManager;
	}

	/**
	 * 背景オブジェクトを取得するメソッド．
	 *
	 * @return 背景オブジェクト
	 */
	public Bg bg() {
		return mBg;
	}

	/**
	 * Drawerオブジェクトを取得するメソッド．
	 *
	 * @return Drawerオブジェクト
	 */
	public Drawer viewer() {
		return fMDrawer;
	}

	/**
	 * ゲーム状態をリセットするメソッド．
	 */
	public void reset() {
		mHeroManager.reset();
		mSpearManager.reset();
	}

	// プレイヤーのマネージャー
	private final HeroManager mHeroManager;
	// ヤリのマネージャー
	private final SpearManager mSpearManager;
	// 背景オブジェクト
	private final Bg mBg;
	// Drawerオブジェクト
	private final Drawer fMDrawer;
}
