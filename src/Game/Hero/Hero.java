package Game.Hero;

import Util.Numeric.Entity;
import Util.Numeric.Rect;
import Util.Numeric.Rotate;
import Util.Numeric.Vector;

import java.awt.*;

/**
 * Created by bakanaouji on 2017/07/23.
 * プレイヤーを表すクラス．
 */
public class Hero {
	/**
	 * プレイヤーが選択できる行動
	 */
	public enum Action {
		None,    // 移動しない
		Right,  // 右に移動
		Left,    // 左に移動
		Up,      // 上に移動
		Down    // 下に移動
	}

	/**
	 * コンストラクタ．
	 *
	 * @param aComp コンポーネント
	 * @param aX    初期x座標
	 * @param aY    初期y座標
	 * @param aRot  初期回転角
	 */
	public Hero(final Game.Component aComp, final double aX, final double aY, final double aRot) {
		// コンポーネント
		mComp = aComp;
		// 初期座標決定
		mPos = new Vector(aX, aY);
		// 初期速度と加速度は0
		mVelocity = new Vector(2);
		// 初期回転角決定
		mRotate = new Rotate(aRot);
		// 画像読み込み
		if (mComp.viewer().paintPanel() != null) {
			mImage = mComp.viewer().paintPanel().getToolkit().getImage("resource/Image/hero.png");
		} else {
			mImage = null;
		}
		// エンティティは円
		final Rect rect = new Rect(20.0, -20.0, 20.0, -20.0);
		mEntity = new Entity(mPos, mRotate, Entity.CollisionType.Circle, 20.0, rect);
		mIsDead = false;
	}

	/**
	 * 更新を行うメソッド．
	 * 行動を入力として受け取り，プレイヤーの座標を更新する．
	 * 死亡判定も行い，死亡しているかどうかを返す．
	 *
	 * @param aAction 行動
	 * @return 死亡しているか
	 */
	public boolean update(final Action aAction) {
		// 死亡していたら更新しない
		if (mIsDead) {
			return true;
		}
		// 速度更新
		mVelocity.value(0, 0.0);
		mVelocity.value(1, 0.0);
		switch (aAction) {
			case Right:
				mVelocity.value(0, 3.0);
				break;
			case Left:
				mVelocity.value(0, -3.0);
				break;
			case Up:
				mVelocity.value(1, -3.0);
				break;
			case Down:
				mVelocity.value(1, 3.0);
				break;
		}
		// 座標更新
		mPos.value(0, mPos.value(0) + mVelocity.value(0));
		mPos.value(1, mPos.value(1) + mVelocity.value(1));
		// ヤリとの当たり判定
		// 当たっている場合，死亡
		final Vector collisionVec = new Vector(2);
		for (int i = 0; i < mComp.spearManager().count(); ++i) {
			boolean hit = mEntity.collidesWith(mComp.spearManager().spear(i).entity(), collisionVec);
			if (hit) {
				dead();
				break;
			}
		}
		// エンティティ更新
		mEntity.update();
		return false;
	}

	/**
	 * 描画を行うメソッド．
	 */
	public void draw() {
		// 死亡している場合，描画しない
		if (mIsDead) {
			return;
		}
		// 描画
		mComp.viewer().paintPanel().graphics().drawImage(mImage, (int) mPos.value(0) - 20, (int) mPos.value(1)
						- 20, mComp.viewer().paintPanel());
	}

	/**
	 * 死亡させるメソッド．
	 */
	public void dead() {
		mIsDead = true;
	}

	/**
	 * 死亡しているかどうかを取得するメソッド．
	 *
	 * @return 死亡しているかどうか
	 */
	public boolean isDead() {
		return mIsDead;
	}

	/**
	 * 座標を取得するメソッド．
	 *
	 * @return 座標
	 */
	public Vector pos() {
		return mPos;
	}

	/**
	 * 復活させるメソッド．
	 */
	public void arrive() {
		mIsDead = false;
	}

	// コンポーネント
	private final Game.Component mComp;
	// 座標
	private final Vector mPos;
	// 速度
	private final Vector mVelocity;
	// 回転角
	private final Rotate mRotate;
	// 画像ハンドル
	private final Image mImage;
	// エンティティ
	private final Entity mEntity;
	// 死亡状態か
	private boolean mIsDead;
}
