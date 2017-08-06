package Game.Spear;

import Game.Component;
import Util.Numeric.Vector;
import Game.Hero.Hero;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/23.
 * ヤリを管理するマネージャー．
 */
public class SpearManager {
	/**
	 * コンストラクタ．
	 *
	 * @param aComp        コンポーネント
	 * @param aSpearMaxNum ヤリを同時に配置する最大数
	 * @param aRandom      乱数生成器
	 */
	public SpearManager(final Component aComp, final int aSpearMaxNum, final Random aRandom) {
		mComp = aComp;
		mSpearArray = new ArrayList<>();
		mTimer = 0.0;
		mRandom = aRandom;
		mSpearMaxCount = aSpearMaxNum;
		mTimeSpan = 300.0 / mSpearMaxCount;
	}

	/**
	 * 指定のインデックスのヤリを取得するメソッド．
	 *
	 * @param aIndex インデックス
	 * @return ヤリ
	 */
	public Spear spear(final int aIndex) {
		return mSpearArray.get(aIndex);
	}

	/**
	 * 配置されているヤリの数を取得するメソッド．
	 *
	 * @return 配置されているヤリの数
	 */
	public int count() {
		return mSpearArray.size();
	}

	/**
	 * 更新を行うメソッド．
	 */
	public void update() {
		// 初めに死亡チェック
		for (int i = mSpearArray.size() - 1; i >= 0; --i) {
			if (mSpearArray.get(i).isDead()) {
				mSpearArray.remove(mSpearArray.get(i));
			}
		}
		// タイマー更新
		mTimer += 1.0;
		if (mTimer > mTimeSpan) {
			// 一定時間ごとに，ヤリ生成処理
			mTimer = 0.0;
			if (mSpearArray.size() < mSpearMaxCount) {
				// ヤリの数が最大数未満であれば，ヤリを１つ生成
				// 初期位置を中心から円状にランダムに決定
				final double angle = (mRandom.nextDouble() * 2.0 * Math.PI);
				final Vector initPos = new Vector((Math.cos(angle) * 500.0 + 500.0), (Math.sin(angle) * 500.0 +
								500.0));
				// 方向はプレイヤーの方へ
				final Vector vec = Vector.sub(mComp.heroManager().hero().pos(), initPos);
				vec.normalize();
				mSpearArray.add(new Spear(mComp, initPos.value(0), initPos.value(1), vec));
			}
		}
		// 各ヤリ更新
		for (Spear spear : mSpearArray) {
			spear.update();
		}
	}

	/**
	 * 各ヤリの描画を行うメソッド．
	 */
	public void draw() {
		for (Spear spear : mSpearArray) {
			spear.draw();
		}
	}

	/**
	 * ヤリをリセットするメソッド．
	 * ゲームをプレイする度に最初に呼び出される．
	 */
	public void reset() {
		mSpearArray.clear();
		mTimer = 0.0;
	}

	/**
	 * プレイヤーとの近さ順にヤリをソートするメソッド．
	 */
	public void sort() {
		final Hero hero = mComp.heroManager().hero();
		mSpearArray.sort((a, b) -> (int) (Vector.sub(a.pos(), hero.pos()).norm() - Vector.sub(b.pos(), hero.pos()).norm
						()));
	}

	// コンポーネント
	private final Component mComp;
	// ヤリ
	private final ArrayList<Spear> mSpearArray;
	// ヤリを配置するマックス数
	private final int mSpearMaxCount;
	// ヤリを投入する時間間隔
	private final double mTimeSpan;
	// タイマー
	private double mTimer;
	// 乱数生成器
	private final Random mRandom;
}
