package Util.Numeric;

/**
 * Created by bakanaouji on 2017/07/23.
 * 当たり判定を司るクラス．
 */
public class Entity {
	/**
	 * コンストラクタ．
	 *
	 * @param aCenter        中心座標
	 * @param aRotate        回転角
	 * @param aCollisionType 当たり判定の形の種類
	 * @param aRadius        円の当たり判定の場合の，半径
	 * @param aEdge          四角形の当たり判定の場合の，四角形の四隅
	 */
	public Entity(final Vector aCenter, final Rotate aRotate, final CollisionType aCollisionType, final double aRadius,
								final Rect aEdge) {
		mCenter = aCenter;
		mRotate = aRotate;
		mCollisionType = aCollisionType;
		mRadius = aRadius;
		mEdge = aEdge;
		mCorners = new Vector[4];
		mRotatedBoxReady = false;
	}

	/**
	 * 当たり判定の形の種類
	 */
	public enum CollisionType {
		Circle,      // 円
		RotatedBox  // 四角形
	}

	/**
	 * 更新．
	 */
	public void update() {
		mRotatedBoxReady = false;
	}

	/**
	 * 他のエンティティとの当たり判定を行うメソッド．
	 *
	 * @param aEnt             当たり判定を行う対象のエンティティ
	 * @param aCollisionVector 衝突している方向ベクトル．衝突していた場合のみセットされる．
	 * @return 衝突しているかどうか
	 */
	public boolean collidesWith(final Entity aEnt, final Vector aCollisionVector) {
		// 両方のエンティティがCircle衝突である場合
		if (mCollisionType == CollisionType.Circle && aEnt.mCollisionType == CollisionType.Circle) {
			return collideCircle(aEnt, aCollisionVector);
		}
		// どちらのエンティティもCircle衝突を使用しない場合
		if (mCollisionType != CollisionType.Circle && aEnt.mCollisionType != CollisionType.Circle) {
			return collideRotatedBox(aEnt, aCollisionVector);
		} else {
			if (mCollisionType == CollisionType.Circle) {
				return aEnt.collideRotatedBoxCircle(this, aCollisionVector);
			} else {
				return collideRotatedBoxCircle(aEnt, aCollisionVector);
			}
		}
	}

	/**
	 * 円のエンティティ同士の当たり判定を行うメソッド．
	 *
	 * @param aEnt             当たり判定を行う対象のエンティティ
	 * @param aCollisionVector 衝突している方向ベクトル．衝突していた場合のみセットされる．
	 * @return 衝突しているかどうか
	 */
	private boolean collideCircle(final Entity aEnt, final Vector aCollisionVector) {
		// 中心と中心の間の差
		final Vector distSquared = Vector.sub(mCenter, aEnt.mCenter);
		distSquared.value(0, distSquared.value(0) * distSquared.value(0));
		distSquared.value(1, distSquared.value(1) * distSquared.value(1));

		// 半径の合計を計算
		double sumRadSquared = mRadius + aEnt.mRadius;
		sumRadSquared *= sumRadSquared;

		// エンティティが衝突している場合
		if (distSquared.value(0) + distSquared.value(1) <= sumRadSquared) {
			// 衝突ベクトルを設定
			aCollisionVector.copyFrom(Vector.sub(aEnt.mCenter, mCenter));
			return true;
		}
		// 衝突していない場合
		return false;
	}

	/**
	 * 四角形のエンティティ同士の当たり判定を行うメソッド．
	 *
	 * @param aEnt             当たり判定を行う対象のエンティティ
	 * @param aCollisionVector 衝突している方向ベクトル．衝突していた場合のみセットされる．
	 * @return 衝突しているかどうか
	 */
	private boolean collideRotatedBox(final Entity aEnt, final Vector aCollisionVector) {
		double overlap01, overlap03;
		// 回転するボックスを準備
		computeRotatedBox();
		aEnt.computeRotatedBox();
		if (projectionsOverlap(aEnt) && aEnt.projectionsOverlap(this)) {
			// ここに到達した場合、エンティティは衝突している。
			// 最小の重複部分のエッジが衝突が発生しているエッジ。
			// 衝突ベクトルは衝突が発生したエッジに垂直に生成される。
			// 投影エッジは01と03。
			//
			//                    entA01min
			//                   /     aEnt
			//                  /     / entA01max
			//                 /     / /  entB01max
			//                /     / /  /
			//            0--------------------1
			// entB03min..|          ____
			// entA03min..|    _____|_ B |
			//            |   | A   | |  |
			// entA03max..|   |_____|_|  |
			// entB03max..|         |____|
			//            |
			//            |
			//            3
			//
			if (mEntA01min < mEntB01min)   // AのほうがBより左
			{
				overlap01 = mEntA01max - mEntB01min;
				aCollisionVector.copyFrom(Vector.sub(mCorners[1], mCorners[0]));
			} else    // AのほうがBより右
			{
				overlap01 = mEntB01max - mEntA01min;
				aCollisionVector.copyFrom(Vector.sub(mCorners[0], mCorners[1]));
			}
			if (mEntA03min < mEntB03min)   // AのほうがBより上
			{
				overlap03 = mEntA03max - mEntB03min;
				if (overlap03 < overlap01)
					aCollisionVector.copyFrom(Vector.sub(mCorners[3], mCorners[0]));
			} else    // AのほうがBより下
			{
				overlap03 = mEntB03max - mEntA03min;
				if (overlap03 < overlap01)
					aCollisionVector.copyFrom(Vector.sub(mCorners[0], mCorners[3]));
			}
			return true;
		}
		return false;
	}

	/**
	 * 四角形のエンティティと円のエンティティの当たり判定を行うメソッド．
	 *
	 * @param aEnt             当たり判定を行う対象のエンティティ
	 * @param aCollisionVector 衝突している方向ベクトル．衝突していた場合のみセットされる．
	 * @return 衝突しているかどうか
	 */
	private boolean collideRotatedBoxCircle(final Entity aEnt, final Vector aCollisionVector) {
		double center01, center03, overlap01, overlap03;

		computeRotatedBox();                    // 回転するボックスを準備

		// 円の中心をedge01に投影
		center01 = Vector.times(mEdge01, aEnt.mCenter);
		// 最小値と最大値は中心からの半径
		mEntB01min = center01 - aEnt.mRadius;
		mEntB01max = center01 + aEnt.mRadius;
		if (mEntB01min > mEntA01max || mEntB01max < mEntA01min) // 投影が重ならない場合
			return false;                       // 衝突の可能性なし

		// 円の中心をedge03に投影
		center03 = Vector.times(mEdge03, aEnt.mCenter);
		// 最小値と最大値は中心からの半径
		mEntB03min = center03 - aEnt.mRadius;
		mEntB03max = center03 + aEnt.mRadius;
		if (mEntB03min > mEntA03max || mEntB03max < mEntA03min) // 投影が重ならない場合
			return false;                       // 衝突の可能性なし

		// 円の投影がボックスの投影に重なる場合
		// 円が衝突ボックスのボロノイ領域にあるかどうかをチェック

		// 中心がVoronoi0にある場合
		if (center01 < mEntA01min && center03 < mEntA03min)
			return collideCornerCircle(mCorners[0], aEnt, aCollisionVector);
		// 中心がVoronoi1にある場合
		if (center01 > mEntA01max && center03 < mEntA03min)
			return collideCornerCircle(mCorners[1], aEnt, aCollisionVector);
		// 中心がVoronoi2にある場合
		if (center01 > mEntA01max && center03 > mEntA03max)
			return collideCornerCircle(mCorners[2], aEnt, aCollisionVector);
		// 中心がVoronoi3にある場合
		if (center01 < mEntA01min && center03 > mEntA03max)
			return collideCornerCircle(mCorners[3], aEnt, aCollisionVector);

		// 円が衝突ボックスのボロノイ領域にないので、ボックスのエッジと衝突。
		// 最小の重複部分のエッジが衝突が発生しているエッジ。
		// 衝突ベクトルは衝突が発生したエッジに垂直に生成される。
		// 投影エッジは01と03。
		//
		//                    entA01min
		//                   /   aEnt
		//                  /   /    aEnt
		//                 /   /    /  entA01max
		//                /   /    /  /
		//            0--------------------1
		// aEnt..|        ___
		// entA03min..|    ___/ B \__
		// aEnt..|   |   \___/  |
		//            |   | A        |
		// entA03max..|   |__________|
		//            |
		//            |
		//            |
		//            3
		//
		if (mEntA01min < mEntB01min)   // AのほうがBより左
		{
			overlap01 = mEntA01max - mEntB01min;
			aCollisionVector.copyFrom(Vector.sub(mCorners[1], mCorners[0]));
		} else    // AのほうがBより右
		{
			overlap01 = mEntB01max - mEntA01min;
			aCollisionVector.copyFrom(Vector.sub(mCorners[0], mCorners[1]));
		}
		if (mEntA03min < mEntB03min)   // AのほうがBより上
		{
			overlap03 = mEntA03max - mEntB03min;
			if (overlap03 < overlap01)
				aCollisionVector.copyFrom(Vector.sub(mCorners[3], mCorners[0]));
		} else    // AのほうがBより下
		{
			overlap03 = mEntB03max - mEntA03min;
			if (overlap03 < overlap01)
				aCollisionVector.copyFrom(Vector.sub(mCorners[0], mCorners[3]));
		}
		return true;
	}

	/**
	 * 相手のエンティティの四角形を，このエンティティのedge01およびedge03に投影し，
	 * 投影が重なっているかどうかを返すメソッド．
	 * collideRotateBoxによって呼び出される．
	 *
	 * @param aEnt 当たり判定を行う対象のエンティティ
	 * @return 投影が重なっているかどうか
	 */
	private boolean projectionsOverlap(final Entity aEnt) {
		double projection;

		// 相手のボックスをedge01に投影
		projection = Vector.times(mEdge01, aEnt.mCorners[0]);
		// 頂点0を投影
		mEntB01min = projection;
		mEntB01max = projection;
		// 残りの頂点それぞれを処理
		for (int c = 1; c < 4; c++) {
			// 頂点をedge01に投影
			projection = Vector.times(mEdge01, aEnt.mCorners[c]);
			if (projection < mEntB01min) {
				mEntB01min = projection;
			} else if (projection > mEntB01max) {
				mEntB01max = projection;
			}
		}
		// 投影が重ならない場合
		if (mEntB01min > mEntA01max || mEntB01max < mEntA01min) {
			return false;                       // 衝突の可能性なし
		}
		// 相手のボックスをedge03に投影
		projection = Vector.times(mEdge03, aEnt.mCorners[0]);
		// 頂点0を投影
		mEntB03min = projection;
		mEntB03max = projection;
		// 残りの頂点それぞれを処理
		for (int c = 1; c < 4; c++) {
			// 頂点をedge03に投影
			projection = Vector.times(mEdge03, aEnt.mCorners[c]);
			if (projection < mEntB03min) {
				mEntB03min = projection;
			} else if (projection > mEntB03max) {
				mEntB03max = projection;
			}
		}
		// 投影が重ならない場合
		return !(mEntB03min > mEntA03max || mEntB03max < mEntA03min);
	}

	/**
	 * エンティティの四角形の頂点と対象のエンティティの円との当たり判定を行うメソッド．
	 * collideRotatedBoxCircleによって呼び出される．
	 *
	 * @param aEnt             当たり判定を行う対象のエンティティ
	 * @param aCollisionVector 衝突している方向ベクトル．衝突していた場合のみセットされる．
	 * @return 衝突しているかどうか
	 */
	private boolean collideCornerCircle(final Vector aCorner, final Entity aEnt, final Vector aCollisionVector) {
		Vector mDistSquared = Vector.sub(aCorner, aEnt.mCenter);      // 頂点 - 円
		// 差を2乗
		mDistSquared.value(0, mDistSquared.value(0) * mDistSquared.value(0));   //
		mDistSquared.value(1, mDistSquared.value(1) * mDistSquared.value(1));   //

		// 半径の合計を計算してから、それを2乗
		double mSumRadiiSquared = aEnt.mRadius; // (0 + 円の半径)
		mSumRadiiSquared *= mSumRadiiSquared;                 // 2乗する

		// 頂点と円が衝突している場合
		if (mDistSquared.value(0) + mDistSquared.value(1) <= mSumRadiiSquared) {
			// 衝突ベクトルを設定
			aCollisionVector.copyFrom(Vector.sub(aEnt.mCenter, aCorner));
			return true;
		}
		return false;
	}

	/**
	 * 回転角におうじて，エンティティの四角形の頂点や投影線などを計算するメソッド．
	 */
	private void computeRotatedBox() {
		if (mRotatedBoxReady) {
			return;
		}
		double projection;

		final Vector rotatedX = new Vector(Math.cos(mRotate.angle()), Math.sin(mRotate.angle()));
		final Vector rotatedY = new Vector(-Math.sin(mRotate.angle()), Math.cos(mRotate.angle()));

		final Vector center = mCenter;
		mCorners[0] = Vector.add(center, Vector.add(Vector.times(rotatedX, mEdge.left()),
						Vector.times(rotatedY, mEdge.top())));
		mCorners[1] = Vector.add(center, Vector.add(Vector.times(rotatedX, mEdge.right()),
						Vector.times(rotatedY, mEdge.top())));
		mCorners[2] = Vector.add(center, Vector.add(Vector.times(rotatedX, mEdge.right()),
						Vector.times(rotatedY, mEdge.bottom())));
		mCorners[3] = Vector.add(center, Vector.add(Vector.times(rotatedX, mEdge.left()),
						Vector.times(rotatedY, mEdge.bottom())));

		// corners[0]を基点として使用
		// corners[0]に接する2辺を投影線として使用
		mEdge01 = new Vector(mCorners[1].value(0) - mCorners[0].value(0), mCorners[1].value(1) - mCorners[0].value(1));
		mEdge01.normalize();
		mEdge03 = new Vector(mCorners[3].value(0) - mCorners[0].value(0), mCorners[3].value(1) - mCorners[0].value(1));
		mEdge03.normalize();

		// このエンティティを投影線上に投影したときの最小値と最大値
		projection = Vector.times(mEdge01, mCorners[0]);
		mEntA01min = projection;
		mEntA01max = projection;
		// edge01への投影
		projection = Vector.times(mEdge01, mCorners[1]);
		if (projection < mEntA01min)
			mEntA01min = projection;
		else if (projection > mEntA01max)
			mEntA01max = projection;
		// edge03への投影
		projection = Vector.times(mEdge03, mCorners[0]);
		mEntA03min = projection;
		mEntA03max = projection;
		projection = Vector.times(mEdge03, mCorners[3]);
		if (projection < mEntA03min)
			mEntA03min = projection;
		else if (projection > mEntA03max)
			mEntA03max = projection;

		mRotatedBoxReady = true;
	}

	// 中心座標
	private final Vector mCenter;
	// 回転角
	private final Rotate mRotate;
	// 当たり判定の種類
	private final CollisionType mCollisionType;
	// 当たり判定（円）の半径
	private final double mRadius;
	// RotatedBoxの衝突判定用ボックス
	private final Rect mEdge;
	// RotatedBoxの衝突判定用
	private final Vector[] mCorners;
	// 投影用のエッジ
	private Vector mEdge01, mEdge03;
	// このエンティティのボックスをedge01とedge03に投影した場合の最大と最小の投影
	private double mEntA01min, mEntA01max, mEntA03min, mEntA03max;
	// 相手のボックスをedge01とedge03に投影した場合の最大と最小の投影
	private double mEntB01min, mEntB01max, mEntB03min, mEntB03max;
	// 回転した衝突判定用のボックスが準備できた場合、true
	private boolean mRotatedBoxReady;
}
