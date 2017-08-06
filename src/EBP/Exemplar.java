package EBP;

import Util.Log.ReadWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by bakanaouji on 2017/07/20.
 * 事例を表すクラス．
 */
public class Exemplar implements ReadWriter, Cloneable {
	/**
	 * コンストラクタ．
	 */
	public Exemplar() {
		mState = new State();
		mAction = 0;
	}

	/**
	 * コピーコンストラクタ．
	 *
	 * @param aSrc コピー先
	 */
	public Exemplar(final Exemplar aSrc) {
		mState = new State(aSrc.mState);
		mAction = aSrc.mAction;
		mActiveCount = aSrc.mActiveCount;
	}

	/**
	 * 自身のディープコピーを返すメソッド．
	 *
	 * @return 自身のディープコピー
	 */
	@Override
	public Exemplar clone() {
		return new Exemplar(this);
	}

	/**
	 * 自身の情報を書き込むメソッド．
	 *
	 * @param aPw PrintWriter
	 */
	@Override
	public void writeTo(final PrintWriter aPw) {
		mState.writeTo(aPw);
		aPw.println(mAction);
		aPw.println(mActiveCount);
	}

	/**
	 * ファイルの情報を読み込むメソッド．
	 *
	 * @param aBr BufferedReader
	 * @throws IOException IOException
	 */
	@Override
	public void readFrom(final BufferedReader aBr) throws IOException {
		mState.readFrom(aBr);
		String line = aBr.readLine();
		mAction = Integer.parseInt(line);
		line = aBr.readLine();
		mActiveCount = Integer.parseInt(line);
	}

	/**
	 * 状態ベクトルを取得するメソッド．
	 *
	 * @return 状態ベクトル
	 */
	public State state() {
		return mState;
	}

	/**
	 * 行動値を取得するメソッド．
	 *
	 * @return 行動値
	 */
	public int action() {
		return mAction;
	}

	/**
	 * 行動値をセットするメソッド．
	 *
	 * @param aAct 行動値
	 */
	public void action(int aAct) {
		mAction = aAct;
	}

	/**
	 * 事例が参照された回数を取得するメソッド．
	 *
	 * @return 参照された回数
	 */
	public int activeCount() {
		return mActiveCount;
	}

	/**
	 * 事例が参照された回数をセットするメソッド．
	 *
	 * @param aActiveCount 参照された回数
	 */
	public void activeCount(final int aActiveCount) {
		this.mActiveCount = aActiveCount;
	}

	/**
	 * 二つのインスタンスが等しいかどうかを比較するメソッド．
	 * 比較対象となるインスタンスを受け取り，自身のインスタンスと比較する．
	 * 事例が参照された回数（mActiveCount）は無視して判定する．
	 *
	 * @param aObj 比較対象となるインスタンス
	 * @return インスタンスが等しいかどうか
	 */
	@Override
	public boolean equals(final Object aObj) {
		// アドレスが等しければインスタンスも等しい
		if (this == aObj)
			return true;
		// インスタンスがnullならインスタンスは等しくない
		if (aObj == null)
			return false;
		// インスタンスのクラスが等しくなければインスタンスは等しくない
		if (!(aObj instanceof Exemplar))
			return false;
		// 状態と，対となるインスタンスが等しければインスタンスは等しい
		final Exemplar other = (Exemplar) aObj;
		return mState.equals(other.mState) && mAction == other.mAction;
	}

	// 状態ベクトル
	private final State mState;
	// 行動値
	private int mAction;
	// 参照された回数
	private int mActiveCount;
}
