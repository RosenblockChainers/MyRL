package Game.Spear;

import Util.Numeric.Entity;
import Util.Numeric.Rect;
import Util.Numeric.Rotate;
import Util.Numeric.Vector;

import java.awt.*;

/**
 * Created by bakanaouji on 2017/07/23.
 * ヤリを表すクラス．
 */
public class Spear {
	/**
	 * コンストラクタ．
	 *
	 * @param aComp コンポーネント
	 * @param aX    初期x座標
	 * @param aY    初期y座標
	 * @param aVel  初期速度（方向のみ）
	 */
	public Spear(final Game.Component aComp, final double aX, final double aY, final Vector aVel) {
		// コンポーネント
		mComp = aComp;
		// 初期座標決定
		mPos = new Vector(aX, aY);
		mVelocity = Vector.times(aVel, 3.0);
		// 初期加速度は0
		mAcceleration = new Vector(2);
		// 初期回転角を進む方向に合わせて初期化
		double rot;
		if (aVel.value(1) < 0) {
			rot = -Math.acos(aVel.value(0));
		} else {
			rot = Math.acos(aVel.value(0));
		}
		mRotate = new Rotate(rot);
		// 画像読み込み
		if (mComp.viewer().paintPanel() != null) {
			mImage = mComp.viewer().paintPanel().getToolkit().getImage("../resource/Image/spear.png");
		} else {
			mImage = null;
		}
		// エンティティは四角形
		final Rect rect = new Rect(15.0, -15.0, 7.5, -7.5);
		mEntity = new Entity(mPos, mRotate, Entity.CollisionType.RotatedBox, 20.0, rect);
	}

	/**
	 * 更新を行うメソッド．
	 */
	public void update() {
		// 死亡していたら更新しない
		if (mIsDead) {
			return;
		}
		// 速度更新
		mVelocity.add(mAcceleration);
		// 座標更新
		mPos.value(0, mPos.value(0) + mVelocity.value(0));
		mPos.value(1, mPos.value(1) + mVelocity.value(1));
		// エンティティ更新
		mEntity.update();
		// 終点座標まで到着していたら死亡
		final Vector posFromCenter = Vector.sub(mPos, new Vector(500.0, 500.0));
		if (posFromCenter.norm() > 510.0) {
			dead();
		}
	}

	/**
	 * 描画を行うメソッド．
	 */
	public void draw() {
		// 死亡していたら描画しない
		if (mIsDead) {
			return;
		}
		// 回転させるので，新しいグラフィックスにそれぞれ描画
		final Graphics2D g2 = mComp.viewer().paintPanel().create();
		g2.rotate(mRotate.angle(), mPos.value(0), mPos.value(1));
		g2.drawImage(mImage, (int) (mPos.value(0) - 15.0), (int) (mPos.value(1) - 7.5), mComp.viewer()
						.paintPanel());
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
	 * エンティティを取得するメソッド．
	 *
	 * @return エンティティ
	 */
	public Entity entity() {
		return mEntity;
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
	 * 速度を取得するメソッド．
	 *
	 * @return 速度
	 */
	public Vector velocity() {
		return mVelocity;
	}

	// コンポーネント
	private final Game.Component mComp;
	// 座標
	private final Vector mPos;
	// 速度
	private final Vector mVelocity;
	// 加速度
	private final Vector mAcceleration;
	// 回転角
	private final Rotate mRotate;
	// 画像ハンドル
	private final Image mImage;
	// エンティティ
	private final Entity mEntity;
	// 死亡状態か
	private boolean mIsDead;

}
