package Viewer;

import Agent.Agent;
import Game.MyGame;
import EBP.Policy;
import Util.Log.Logger;

import java.util.Random;

/**
 * Created by bakanaouji on 2017/07/24.
 * 学習済みの政策のプレイ動画を閲覧するビューワー．
 */
public class Viewer {
	public static void main(final String[] aArgs) {
		// 乱数生成器
//		final long seed = new Random().nextLong();
		final long seed = -6306892710638080131L;
		System.out.println("seed : " + seed);
		final Random random = new Random(seed);
		// エージェント
		final Agent agent = new Agent();
		// 政策
		final Policy policy = new Policy();
		// ファイル読み込み
		Logger.readFrom("log/policy.csv", policy);
		// ゲーム
		final MyGame game = new MyGame(random, 1, 5, true);
		System.out.println("eval : " + policy.evaluationValue());
		while (true) {
			final double score = game.playGame(policy, agent);
			System.out.println("score : " + score);
		}
	}

}
