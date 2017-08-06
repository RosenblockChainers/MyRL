package EBP;

import Util.Numeric.Vector;
import Util.Log.ReadWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by bakanaouji on 2017/07/20.
 * 状態ベクトルを表すクラス．
 */
public class State implements ReadWriter, Cloneable {

	/**
	 * コンストラクタ．
	 * 状態の次元数を0として初期化する．
	 */
	public State() {
		mData = new Vector(0);
	}

	/**
	 * コピーコンストラクタ．
	 *
	 * @param aSrc コピー先
	 */
	public State(final State aSrc) {
		mData = new Vector(aSrc.mData);
	}

	/**
	 * 自身のディープコピーを返すメソッド．
	 *
	 * @return 自身のディープコピー
	 */
	@Override
	public State clone() {
		return new State(this);
	}

	/**
	 * 自身の情報を書き込むメソッド．
	 *
	 * @param aPw PrintWriter
	 */
	@Override
	public void writeTo(final PrintWriter aPw) {
		mData.writeTo(aPw);
	}

	/**
	 * ファイルから情報を読み込むメソッド．
	 *
	 * @param aBr BufferedReader
	 * @throws IOException IOException
	 */
	@Override
	public void readFrom(final BufferedReader aBr) throws IOException {
		mData.readFrom(aBr);
	}

	/**
	 * 指定の次元の要素を返すメソッド．
	 *
	 * @param aIndex 取得する要素の次元
	 * @return 指定した次元の要素
	 */
	public double value(final int aIndex) {
		return mData.value(aIndex);
	}

	/**
	 * 指定の次元の要素の値を設定するメソッド．
	 *
	 * @param aIndex 設定する要素の次元
	 * @param aX     設定する値
	 */
	public void value(final int aIndex, final double aX) {
		mData.value(aIndex, aX);
	}

	/**
	 * 状態の次元数を返すメソッド．
	 *
	 * @return 状態の次元数
	 */
	public int dimension() {
		return mData.dimension();
	}

	/**
	 * 状態の次元数を設定するメソッド．
	 *
	 * @param aDimension 状態の次元数
	 */
	public void dimension(final int aDimension) {
		mData.dimension(aDimension);
	}

	/**
	 * 二つのインスタンスが等しいかどうかを比較するメソッド．
	 * 比較対象となるインスタンスを受け取り，自身のインスタンスと比較する．
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
		if (getClass() != aObj.getClass()) {
			return false;
		}
		// 状態の要素を比較
		final State other = (State) aObj;
		// 状態の次元数が異なればインスタンスは等しくない
		final int dimension = mData.dimension();
		if (other.mData.dimension() != dimension) {
			return false;
		}
		// 状態のすべての次元が等しければインスタンスは等しい
		for (int i = 0; i < dimension; ++i) {
			if (1.0e-9 < Math.abs(mData.value(i) - other.mData.value(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ベクトルを取得．
	 *
	 * @return ベクトル
	 */
	public Vector vector() {
		return mData;
	}

	// 状態の要素の配列
	private final Vector mData;
}
