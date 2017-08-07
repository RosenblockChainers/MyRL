package SAPGA;

import Agent.Agent;
import EBP.Policy;
import Game.MyGame;
import Util.Log.LongLogger;
import Util.Log.ShortLogger;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/20.
 * SAPGAによる学習を実行するクラス．
 */
public class MainSAPGA {
	/**
	 * コンストラクタ
	 */
	public MainSAPGA() {
		// 乱数シードを記録
		System.out.println("seed : " + mSeed);
		ShortLogger.writeTo("../log/seed.csv", false, mSeed + "");
		// 初期化
		initialize();
		// 探索を実行
		run();
	}

	/**
	 * 初期化
	 */
	public void initialize() {
		for (int i = 0; i < Math.max(mNumOffspring + 2, mPopulationSize); ++i) {
			mAgents[i] = new Agent();
			mGames[i] = new MyGame(new Random(mRandom.nextLong()), mSpearMaxNum, false);
		}
		mSapga = new SAPGA(mRandom, mGames, mAgents, mPopulationSize, mNumOffspring, mMinSize, mMaxSize, mSelectionR,
						mMutationR);
	}

	/**
	 * 探索を実行
	 */
	public void run() {
		try (LongLogger longLogger = new LongLogger()) {
			// ログ書き込み
			longLogger.writeTo("../log/eval.csv", false, ",generation,cntEval,bestEval,meanEval");
			// 打ち切りまで探索を実行
			Policy bestPolicy = mSapga.bestPolicy(); // 集団内の最良の政策
			while (mSapga.evalCount() < mAbortCondition && bestPolicy.evaluationValue() < mAbortEvalValue) {
				// 集団の平均評価値
				final double meanEval = mSapga.meanEvaluationValue();
				// ログを標準出力
				System.out.println("generation : " + mSapga.generation() +
								", cntEval : " + mSapga.evalCount() +
								", bestEval :  " + bestPolicy.evaluationValue() +
								", meanEval : " + meanEval);
				longLogger.writeTo("../log/eval.csv", false, mSapga.generation() + "," + mSapga.generation() + "," + mSapga
								.evalCount() + "," + bestPolicy.evaluationValue() + "," + meanEval);
				// 数世代ごとに最良の政策を記録
				if (mSapga.generation() % 10 == 0) {
					ShortLogger.writeTo("../log/policy_gen" + mSapga.generation() + ".csv", false, bestPolicy);
				}
				// 一反復分探索を実行
				mSapga.doOneIteration();
				bestPolicy = mSapga.bestPolicy();
				// 探索を通して最良の政策を更新
				if (Double.isNaN(mTotalBestPolicy.evaluationValue()) || mTotalBestPolicy.evaluationValue() < bestPolicy
								.evaluationValue()) {
					mTotalBestPolicy = bestPolicy;
				}
			}
			// 探索を通して最良の政策のログを書き込み
			ShortLogger.writeTo("../log/policy.csv", false, mTotalBestPolicy);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	private final long mSeed = -829656541046703542L;
	// 乱数生成器
	private final Random mRandom = new Random(mSeed);
	// エージェント
	private final Agent[] mAgents = new Agent[Math.max(mNumOffspring + 2, mPopulationSize)];
	// ゲーム
	private final MyGame[] mGames = new MyGame[Math.max(mNumOffspring + 2, mPopulationSize)];
	// SAP-GA
	private SAPGA mSapga;
	// 探索を通して最も評価値が良かった政策
	private Policy mTotalBestPolicy = new Policy();
}