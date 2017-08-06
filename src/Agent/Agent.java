package Agent;

import Util.Numeric.Vector;
import EBP.Exemplar;
import EBP.Policy;
import EBP.State;

/**
 * Created by bakanaouji on 2017/07/20.
 * エージェントを表すクラス．
 */
public class Agent {
	/**
	 * コンストラクタ．
	 */
	public Agent() {
	}

	/**
	 * 行動を決定するメソッド．
	 *
	 * @param aPolicy 行動選択に用いる政策
	 * @param aState  環境から観測した状態
	 * @return 行動
	 */
	public int chooseAction(final Policy aPolicy, final State aState) {
		final int exemSize = aPolicy.size();    // 事例数
		int nearestIndex = 0;   // 最近接の事例のインデックス
		double nearestDistance = distanceState(aState, aPolicy.exemplar(0)
						.state());  // 最近接の事例との距離
		for (int i = 0; i < exemSize; ++i) {
			final double distance = distanceState(aState, aPolicy.exemplar(i).state());
			if (distance < nearestDistance) {
				nearestIndex = i;
				nearestDistance = distance;
			}
		}
		final Exemplar nearestExem = aPolicy.exemplar(nearestIndex);
		nearestExem.activeCount(nearestExem
						.activeCount() + 1);
		return nearestExem.action();
	}

	/**
	 * 状態ベクトル同士の距離を計算するメソッド．
	 *
	 * @param aState1 状態ベクトル①
	 * @param aState2 状態ベクトル②
	 * @return 距離
	 */
	private double distanceState(final State aState1, final State aState2) {
		final Vector subVec = Vector.sub(aState1.vector(), aState2.vector());
		return subVec.norm();
	}
}
