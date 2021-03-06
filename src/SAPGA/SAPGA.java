package SAPGA;

import Agent.Agent;
import Game.MyGame;
import EBP.Policy;
import EBP.Population;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by bakanaouji on 2016/07/20.
 * SAPGAを表すクラス．
 */
public class SAPGA {
	/**
	 * コンストラクタ．
	 *
	 * @param aRandom         乱数生成器
	 * @param aGames          ゲーム
	 * @param aAgents         エージェント
	 * @param aPopulationSize 集団サイズ
	 * @param aNumOffspring   子の政策の生成数
	 * @param aMinSize        政策の事例の最小数
	 * @param aMaxSize        政策の事例の最大数
	 * @param aSelectionR     親の政策の事例の継承確率
	 * @param aMutationR      事例の突然変異確率
	 */
	public SAPGA(final Random aRandom, final MyGame[] aGames, final Agent[] aAgents, final int aPopulationSize,
							 final int aNumOffspring, final int aMinSize, final int aMaxSize, final double aSelectionR,
							 final double aMutationR) {
		mRandom = aRandom;
		mCrossing = new CrossingOperator(aRandom);
		mRefresh = new RefreshOperator(aRandom);
		mGames = aGames;
		mAgents = aAgents;
		mGeneration = 0;
		mCntEval = 0;
		// パラメータ
		mPopulationSize = aPopulationSize;
		mNumOffspring = aNumOffspring;
		mMinSize = aMinSize;
		mMaxSize = aMaxSize;
		mSelectionR = aSelectionR;
		mMutationR = aMutationR;
		// 初期集団の生成と評価
		mPopulation = new Population();
		MyGame.initializePopulation(mRandom, mPopulation, aPopulationSize, aMinSize, aMaxSize);
		// 評価は並列化
		HashMap<Policy, MyGame> mapGame = new HashMap<>();
		HashMap<Policy, Agent> mapAgent = new HashMap<>();
		Policy[] policies = new Policy[aPopulationSize];
		for (int i = 0; i < mPopulationSize; ++i) {
			policies[i] = mPopulation.policy(i);
			mapGame.put(policies[i], mGames[i]);
			mapAgent.put(policies[i], mAgents[i]);
		}
		Stream.of(policies).parallel().forEach(policy -> policy.evaluationValue(mapGame.get(policy).evaluate(policy,
						mapAgent.get(policy))));
		mCntEval += aPopulationSize;
	}

	/**
	 * SAP-GAの探索を1世代分を進める．
	 */
	public void doOneIteration() {
		// 集団からN_pop/2個の親のペアを作る．
		final Policy[][] parentsArray = new Policy[mPopulation.size() / 2][2];
		for (int i = 0; i < mPopulation.size(); ++i) {
			int rand = mRandom.nextInt(mPopulation.size() - i) + i;
			mPopulation.swap(i, rand);
		}
		for (int i = 0; i < mPopulation.size(); ++i) {
			parentsArray[i / 2][i % 2] = mPopulation.policy(i);
		}
		mPopulation.size(0);
		// 子の政策を生成
		final Policy[][] offspringArray = new Policy[parentsArray.length][mNumOffspring];
		for (int i = 0; i < offspringArray.length; i++) {
			for (int j = 0; j < offspringArray[i].length; j++) {
				offspringArray[i][j] = new Policy();
			}
		}
		for (int i = 0; i < parentsArray.length; i++) {
			mCrossing.makeOffspring(parentsArray[i], offspringArray[i], mMinSize,
							mMaxSize, mSelectionR);
		}
		// 親と子を合わせて家族とする
		final Policy[][] family = new Policy[parentsArray.length][2 + mNumOffspring];
		for (int i = 0; i < family.length; i++) {
			System.arraycopy(parentsArray[i], 0, family[i], 0, 2);
			System.arraycopy(offspringArray[i], 0, family[i], 2, 2 + mNumOffspring - 2);
		}
		// 家族の各政策の評価値を得る
		HashMap<Policy, MyGame> mapGame = new HashMap<>();
		HashMap<Policy, Agent> mapAgent = new HashMap<>();
		for (Policy[] policies : family) {
			for (Policy policy : policies) {
				// 貢献度リセット
				for (int k = 0; k < policy.size(); k++) {
					policy.exemplar(k).activeCount(0);
				}
			}
			mapGame.clear();
			mapAgent.clear();
			for (int i = 0; i < mNumOffspring + 2; ++i) {
				mapGame.put(policies[i], mGames[i]);
				mapAgent.put(policies[i], mAgents[i]);
			}
			Stream.of(policies).parallel().forEach(policy -> policy.evaluationValue(mapGame.get(policy).evaluate(policy,
							mapAgent.get(policy))));
		}
		for (int h = 0; h < family.length; h++) {
			// 評価値の順に家族内の政策をソート
			for (int i = 0; i < family[h].length; i++) {
				for (int j = family[h].length - 1; j > i; j--) {
					if (family[h][j - 1].evaluationValue() < family[h][j]
									.evaluationValue()) {
						final Policy tmp = family[h][j - 1];
						family[h][j - 1] = family[h][j];
						family[h][j] = tmp;
					}
				}
			}
		}
		// 家族内の最良政策を集団に追加
		int index = 0;
		Policy[] bestClones = new Policy[family.length];
		mapGame.clear();
		mapAgent.clear();
		for (Policy[] policies : family) {
			mPopulation.add(policies[0]);
			final Policy bestClone = policies[0].clone();
			mRefresh.refreshPolicy(bestClone, mMutationR, mGames[index].actionSize());
			mapGame.put(bestClone, mGames[index]);
			mapAgent.put(bestClone, mAgents[index]);
			mPopulation.add(bestClone);
			bestClones[index] = bestClone;
			++index;
		}
		// 再初期化を行った政策を並列化して評価
		Stream.of(bestClones).parallel().forEach(policy -> policy.evaluationValue(mapGame.get(policy).evaluate(policy,
						mapAgent.get(policy))));
		// 世代数を1増加
		++mGeneration;
		// 評価回数を加算
		mCntEval += (mNumOffspring * mPopulationSize / 2 + mPopulationSize);
	}

	/**
	 * 集団内の平均評価値を取得するメソッド．
	 * 念のために，集団を評価し直すが，評価回数には加算しない．
	 *
	 * @return 集団内の平均評価値
	 */
	public double meanEvaluationValue() {
		double meanEval = 0.0;
		for (int i = 0; i < mPopulationSize; ++i) {
			meanEval += mPopulation.policy(i).evaluationValue();
		}
		return meanEval / (double) mPopulationSize;
	}

	/**
	 * 集団内の最良の政策を取得するメソッド．
	 *
	 * @return 集団内の最良の政策
	 */
	public Policy bestPolicy() {
		Policy bestPol = mPopulation.policy(0);
		for (int i = 0; i < mPopulationSize; ++i) {
			if (mPopulation.policy(i).evaluationValue() > bestPol.evaluationValue()) {
				bestPol = mPopulation.policy(i).clone();
			}
		}
		return bestPol;
	}

	/**
	 * 世代数を取得するメソッド．
	 *
	 * @return 世代数
	 */
	public int generation() {
		return mGeneration;
	}

	/**
	 * 評価回数を取得するメソッド
	 *
	 * @return 評価回数
	 */
	public int evalCount() {
		return mCntEval;
	}

	// 乱数生成器
	private final Random mRandom;
	// 集団
	private final Population mPopulation;
	// 交叉オペレータ
	private final CrossingOperator mCrossing;
	// リフレッシュオペレータ
	private final RefreshOperator mRefresh;
	// ゲーム
	private final MyGame[] mGames;
	// エージェント
	private final Agent[] mAgents;
	// 世代数
	private int mGeneration;
	// 評価回数
	private int mCntEval;
	// パラメータ
	private final int mPopulationSize; // 集団サイズ
	private final int mNumOffspring;  // サンプルサイズ
	private final int mMinSize;  // 最小事例数
	private final int mMaxSize;  // 最大事例数
	private final double mSelectionR;  // 事例継承確率
	private final double mMutationR;  // 事例突然変異確率
}
