package SAPGA;

import Agent.Agent;
import Game.MyGame;
import EBP.Policy;
import Util.Log.Logger;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/20.
 * SAPGAによる学習を実行するクラス．
 */
public class MainSAPGA {
	public MainSAPGA() {
		// 乱数シードを記録
		Logger.writeTo("../log/seed.csv", false, mSeed);
		// 初期化
		mRandom = new Random(mSeed);
		mAgent = new Agent();
		mGame = new MyGame(mRandom, 1, mSpearMaxNum, false);
		mSapga = new SAPGA(mRandom, mGame, mAgent, mPopulationSize, mNumOffspring, mMinSize, mMaxSize, mSelectionR,
						mMutationR);
		mTotalBestPolicy = new Policy();
		Logger.writeTo("../log/eval.csv", false, ",generation,cntEval,bestEval,meanEval");

		// 打ち切りまで探索を実行
		while (mSapga.evalCount() < mAbortCondition) {
			// 集団の平均評価位t
			final double meanEval = mSapga.meanEvaluationValue();
			// 集団内の最良の政策
			final Policy bestPolicy = mSapga.bestPolicy();
			// ログを標準出力
			System.out.println("generation, " + mSapga.generation() +
							", cntEval, " + mSapga.evalCount() +
							", bestEval, " + bestPolicy.evaluationValue() +
							", meanEval, " + meanEval);
			Logger.writeTo("../log/eval.csv", true, mSapga.generation() + "," + mSapga.generation() + "," + mSapga.evalCount() +
							"," + bestPolicy.evaluationValue() + "," + meanEval);
			// 数世代ごとに最良の政策を記録
			if (mSapga.generation() % 10 == 0) {
				Logger.writeTo("../log/policy_gen" + mSapga.generation() + ".csv", false, bestPolicy);
			}
			// 打ち切り評価値を超えた政策がサンプルされたら，探索を終了
			if (bestPolicy.evaluationValue() >= mAbortEvalValue) {
				break;
			}
			// 一反復分探索を実行
			mSapga.doOneIteration();
			// 探索を通して最良の政策を更新
			if (Double.isNaN(mTotalBestPolicy.evaluationValue()) || mTotalBestPolicy.evaluationValue() < bestPolicy
							.evaluationValue()) {
				mTotalBestPolicy = bestPolicy;
			}
		}
		// 探索を通して最良の政策のログを書き込み
		Logger.writeTo("../log/policy.csv", false, mTotalBestPolicy);
	}

	public static void main(final String[] aArgs) {
		new MainSAPGA();
	}

	// SAP-GAのパラメータ
	private final int mPopulationSize = 20; // 集団サイズ
	private final int mNumOffspring = 10;  // サンプルサイズ
	private final int mMinSize = 30;  // 最小事例数
	private final int mMaxSize = mMinSize;  // 最大事例数
	private final double mSelectionR = 0.5;  // 事例継承確率
	private final double mMutationR = 0.5;  // 事例突然変異確率
	// 探索終了関係のパラメータ
	private final int mAbortCondition = 50000;  // 終了評価回数
	private final double mAbortEvalValue = 4999.0;  // 打ち切り評価値（成功条件）
	// ゲームのパラメータ
	private final int mSpearMaxNum = 5;  // ヤリの最大数
	// 乱数シード
	//	private final long mSeed = new Random().nextLong();
	private final long mSeed = -5456963796592331010L;
	// 乱数生成器
	private final Random mRandom;
	// エージェント
	private final Agent mAgent;
	// ゲーム
	private final MyGame mGame;
	// SAP-GA
	private final SAPGA mSapga;
	// 探索を通して最も評価値が良かった政策
	private Policy mTotalBestPolicy;
}