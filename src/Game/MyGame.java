package Game;

import Agent.Agent;
import Game.Hero.Hero;
import Util.Numeric.Vector;
import EBP.*;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/23.
 * ゲームのメインクラス．
 */
public class MyGame {
	/**
	 * コンストラクタ．
	 *
	 * @param aRandom      乱数生成器
	 * @param aNum         参照する近傍のヤリの数
	 * @param aSpearMaxNum 同時に配置するヤリの最大数
	 * @param aIsDraw      描画が有効かどうか
	 */
	public MyGame(final Random aRandom, final int aNum, final int aSpearMaxNum, final boolean aIsDraw) {
		mComp = new Component(aRandom, aSpearMaxNum, aIsDraw);
		mRandom = aRandom;
		mState = new State();
		mState.dimension(aNum * 4 + 2);
		mActionSize = 5;
		mStateSize = aNum * 4 + 2;
		mTimer = 0;
		mNum = aNum;
		mIsDraw = aIsDraw;
	}

	/**
	 * 描画を行うメソッド．
	 * コンポーネントの情報を描画．
	 */
	public void draw() {
		// まず背景色をクリア
		mComp.viewer().paintPanel().clear();
		// 背景描画
		mComp.bg().draw();
		// プレイヤー情報描画
		mComp.heroManager().draw();
		// ヤリ情報描画
		mComp.spearManager().draw();
		// 描画を反映
		mComp.viewer().draw();
	}

	/**
	 * 政策を評価するメソッド．
	 *
	 * @param aPol   政策
	 * @param aAgent エージェント
	 * @return 政策の評価値
	 */
	public double evaluate(final Policy aPol, final Agent aAgent) {
		// 評価値
		double eval = 0.0;
		// 一回の評価でのプレイ回数
		final int loop = 20;
		for (int i = 0; i < loop; ++i) {
			eval += playGame(aPol, aAgent);
		}
		// 複数回プレイした時のスコアの平均値を評価値として返す
		return eval / loop;
	}

	/**
	 * 政策を使ってゲームを一回プレイするメソッド．
	 *
	 * @param aPol   政策
	 * @param aAgent エージェント
	 * @return スコア
	 */
	public double playGame(final Policy aPol, final Agent aAgent) {
		// ゲーム内の状態をリセット
		reset();
		// ゲーム終了となるまでステップを実行
		while (!isFinished()) {
			update(aPol, aAgent);
		}
		// スコアを返す
		return mTotalReward;
	}

	/**
	 * 政策の集団を初期化するメソッド．
	 * SAP-GAの初期集団を生成するために用いる．
	 *
	 * @param aPop     集団
	 * @param aPopSize 集団中の政策の数
	 * @param aMinSize 政策が持つ事例の最小数
	 * @param aMaxSize 政策が持つ事例の最大数
	 */
	public void initializePopulation(final Population aPop, final int aPopSize, final int aMinSize, final int aMaxSize) {
		// 集団のサイズを決定
		aPop.size(aPopSize);
		// 集団サイズ分だけ政策生成
		for (int i = 0; i < aPopSize; ++i) {
			final Policy policy = aPop.policy(i);
			// 各政策の事例数，事例タイプを決定
			final int numExemplars = mRandom.nextInt(aMaxSize - aMinSize + 1) + aMinSize;
			policy.size(numExemplars);
			for (int j = 0; j < numExemplars; ++j) {
				final Exemplar exemplar = policy.exemplar(j);
				// 各政策の各事例の状態の次元数を決定
				final State state = exemplar.state();
				// 各政策の各事例の状態の次元数を決定
				state.dimension(mStateSize);
				for (int k = 0; k < mStateSize; ++k) {
					// 状態の各要素をランダムに初期化
					state.value(k, mRandom.nextDouble());
				}
				// 行動をランダムに初期化
				final int action = mRandom.nextInt(mActionSize);
				exemplar.action(action);
			}
		}
	}

	/**
	 * ゲームを1フレーム分更新するメソッド．
	 * ①行動を受け取る
	 * ②行動を元にゲームの状態を遷移
	 * ③描画
	 *
	 * @param aPol   政策
	 * @param aAgent エージェント
	 */
	private void update(final Policy aPol,final  Agent aAgent) {
		// 1ステップごとの処理
		// 行動を選択
		final int actionInt = aAgent.chooseAction(aPol, mState);
		final Hero.Action action;
		if (actionInt == 0) {
			action = Hero.Action.None;
		} else if (actionInt == 1) {
			action = Hero.Action.Right;
		} else if (actionInt == 2) {
			action = Hero.Action.Left;
		} else if (actionInt == 3) {
			action = Hero.Action.Up;
		} else {
			action = Hero.Action.Down;
		}
		// 行動をもとにゲームを更新
		final double reward = doAction(action);
		mTotalReward += reward;
		// ゲームが終了状態の場合，描画しない
		if (isFinished()) {
			return;
		}
		// 描画
		// 高速化する場合はここを廃止
		// デバッグ用にいったん残す
		if (mIsDraw) {
			draw();
		}
	}

	/**
	 * 行動によってゲームの状態遷移を行うメソッド．
	 * 行動を受け取り，それに応じてゲーム状態を更新する．
	 * その状態遷移によって得た報酬を返す．
	 *
	 * @param aAction 行動
	 * @return 更新によって得た報酬
	 */
	private double doAction(final Hero.Action aAction) {
		// プレイヤー更新
		mComp.heroManager().update(aAction);
		// ヤリ更新
		mComp.spearManager().update();
		// 報酬決定
		double reward = 0.0;
		// プレイヤーが死亡していなければ報酬を1追加
		if (mComp.heroManager().hero().isDead()) {
			reward += 0.0;
		} else {
			reward += 1.0;
		}
		// プレイヤーが画面外に出ていれば，報酬を10000減少
		final Vector heroPos = mComp.heroManager().hero().pos();
		final Vector centerPos = new Vector(2);
		centerPos.value(0, 500.0);
		centerPos.value(1, 500.0);
		final double norm = Vector.sub(centerPos, heroPos).norm();
		if (norm > 500.0) {
			reward -= 10000.0;
			// プレイヤーを死亡状態に
			mComp.heroManager().hero().dead();
		}
		// プレイヤーとの距離に応じてソート
		mComp.spearManager().sort();
		// 状態ベクトルを更新
		updateState();
		// タイマー更新
		mTimer += 1;
		return reward;
	}


	/**
	 * ゲームの内部状態をリセットするメソッド．
	 */
	private void reset() {
		// コンポーネント内の各情報をリセット．
		mComp.reset();
		// 状態ベクトルを更新
		// プレイヤーとの距離に応じてソート
		mComp.spearManager().sort();
		// 状態ベクトルの更新
		updateState();
		mTotalReward = 0.0;
		mTimer = 0;
	}

	/**
	 * ゲームの状態を状態ベクトルに反映させるメソッド．
	 */
	private void updateState() {
		for (int i = 0; i < Math.min(mNum, mComp.spearManager().count()); ++i) {
			// 近傍のヤリの相対的な位置と、ヤリが進んでいる方向を格納
			final Vector sub = Vector.sub(mComp.spearManager().spear(i).pos(), mComp.heroManager().hero().pos());
			final Vector vel = new Vector(mComp.spearManager().spear(i).velocity());
			vel.normalize();
			mState.value(i * 4, (sub.value(0) + 500.0) / 1000.0);
			mState.value(i * 4 + 1, (sub.value(1) + 500.0) / 1000.0);
			mState.value(i * 4 + 2, (vel.value(0) + 1.0) / 2.0);
			mState.value(i * 4 + 3, (vel.value(1) + 1.0) / 2.0);
		}
		for (int i = Math.min(mNum, mComp.spearManager().count()); i < mNum; ++i) {
			// 近傍に指定数未満しかヤリがない場合，値を適当に格納
			mState.value(i * 4, 1.0);
			mState.value(i * 4 + 1, 1.0);
			mState.value(i * 4 + 2, 1.0);
			mState.value(i * 4 + 3, 1.0);
		}
		// 最後にプレイヤーの位置座標を格納
		final double dev = 500.0;
		mState.value(mNum * 4, (mComp.heroManager().hero().pos().value(0) - (1000.0 - dev) / 2.0) / dev);
		mState.value(mNum * 4 + 1, (mComp.heroManager().hero().pos().value(1) - (1000.0 - dev) / 2.0) / dev);
	}

	/**
	 * ゲームが終了状態かどうかを返すメソッド．
	 * プレイヤーが死亡状態か，一定以上時間が経過していたら終了とする．
	 *
	 * @return ゲームが終了状態かどうか
	 */
	private boolean isFinished() {
		return mComp.heroManager().hero().isDead() || mTimer > 5000;
	}

	// コンポーネント
	private final Component mComp;
	// 乱数生成器
	private final Random mRandom;
	// 状態ベクトル
	private final State mState;
	// タイマー
	private int mTimer;
	// 参照する近傍数
	private final int mNum;
	// スコア（合計報酬）
	private double mTotalReward;
	// 取りうる行動の数
	private final int mActionSize;
	// 状態ベクトルの次元数
	private final int mStateSize;
	// 描画するかどうか
	private final boolean mIsDraw;
}
