package SAPGA;

import EBP.Exemplar;
import EBP.Policy;
import EBP.State;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/20.
 * リフレッシュオペレータ．
 */
public class RefreshOperator {

	/**
	 * コンストラクタ．
	 *
	 * @param aRandom 乱数生成器
	 */
	public RefreshOperator(final Random aRandom) {
		mRandom = aRandom;
	}

	/**
	 * 政策のリフレッシュ（再初期化）を行うメソッド．
	 *
	 * @param aPolicy 政策
	 * @param aR      再初期化の確率
	 */
	public void refreshPolicy(final Policy aPolicy,final  double aR) {
		// 政策の事例の内，参照されていない事例を確率rで再初期化
		for (int i = 0; i < aPolicy.size(); i++) {
			if (aPolicy.exemplar(i).activeCount() < 1e-10
							&& mRandom.nextDouble() <= aR) {
				randomize(aPolicy.exemplar(i));
			}
		}
	}

	/**
	 * 事例を再初期化するメソッド．
	 *
	 * @param aExem 事例
	 */
	private void randomize(final Exemplar aExem) {
		final State state = aExem.state();
		for (int i = 0; i < state.dimension(); ++i) {
			state.value(i, mRandom.nextDouble());
		}
	}

	// 乱数生成器
	private final Random mRandom;
}
