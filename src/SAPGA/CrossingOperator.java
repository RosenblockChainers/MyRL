package SAPGA;

import EBP.Policy;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/20.
 * 交叉オペレータ．
 */
public class CrossingOperator {

	/**
	 * コンストラクタ．
	 *
	 * @param aRandom 乱数生成器
	 */
	public CrossingOperator(final Random aRandom) {
		mRandom = aRandom;
	}

	/**
	 * 子の政策を交叉によって生成するメソッド．
	 *
	 * @param aParents   親の政策
	 * @param aOffspring 子の政策
	 * @param aMinSize   政策の事例の最小数
	 * @param aMaxSize   政策の事例の最大数
	 * @param aR         継承確率
	 */
	public void makeOffspring(final Policy[] aParents, final Policy[] aOffspring,
														final int aMinSize, final int aMaxSize, final double aR) {
		// 二つの親政策から指定事例数分の事例を持つ子政策を生成
		for (int i = 0; i < aOffspring.length; i++) {
			// 子政策の事例数が指定数分になるまで，
			while (aOffspring[i].size() < aMinSize
							|| aOffspring[i].size() > aMaxSize) {
				// 子政策を空集合で初期化
				if (aOffspring[i].size() > aMaxSize) {
					aOffspring[i] = new Policy();
				}
				// 親1の事例を確率rで追加，既に含まれている事例ならば追加しない
				for (int j = 0; j < aParents[0].size(); j++) {
					if (mRandom.nextDouble() <= aR) {
						if (!aOffspring[i].hasExemplar(aParents[0].exemplar(j))) {
							aOffspring[i].add(aParents[0].exemplar(j).clone());
						}
					}
				}
				// 親2の事例を確率1-rで追加，既に含まれている事例ならば追加しない
				for (int j = 0; j < aParents[1].size(); j++) {
					if (mRandom.nextDouble() <= (1.0 - aR)) {
						if (!aOffspring[i].hasExemplar(aParents[1].exemplar(j))) {
							aOffspring[i].add(aParents[1].exemplar(j).clone());
						}
					}
				}
			}
		}
	}

	// 乱数生成器
	private final Random mRandom;
}
