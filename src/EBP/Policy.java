package EBP;

import Util.Log.ReadWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by bakanaouji on 2017/07/20.
 * 政策を表すクラス．
 */
public class Policy implements ReadWriter, Cloneable {
	/**
	 * コンストラクタ．
	 */
	public Policy() {
		mExemplars = new ArrayList<>();
		mEvaluationValue = Double.NaN;
	}

	/**
	 * コピーコンストラクタ．
	 *
	 * @param aSrc コピー先
	 */
	public Policy(final Policy aSrc) {
		mExemplars = new ArrayList<>(aSrc.mExemplars.size());
		for (int i = 0; i < aSrc.mExemplars.size(); ++i) {
			mExemplars.add(aSrc.mExemplars.get(i).clone());
		}
		mEvaluationValue = aSrc.mEvaluationValue;
	}

	/**
	 * 自身のディープコピーを返すメソッド．
	 *
	 * @return 自身のディープコピー
	 */
	@Override
	public Policy clone() {
		return new Policy(this);
	}

	/**
	 * 自身の情報を書き込むメソッド．
	 *
	 * @param aPw PrintWriter
	 */
	@Override
	public void writeTo(final PrintWriter aPw) {
		final int size = mExemplars.size();
		aPw.println(size);
		for (Exemplar exemplar : mExemplars) {
			exemplar.writeTo(aPw);
		}
		aPw.println(mEvaluationValue);
	}

	/**
	 * ファイルから情報を読み込むメソッド．
	 *
	 * @param aBr BufferedReader
	 * @throws IOException IOException
	 */
	@Override
	public void readFrom(final BufferedReader aBr) throws IOException {
		String line = aBr.readLine();
		final int size = Integer.parseInt(line);
		// 読み込んだ事例のタイプに応じて事例を生成
		for (int i = 0; i < size; ++i) {
			mExemplars.add(new Exemplar());
		}
		for (int i = 0; i < size; ++i) {
			mExemplars.get(i).readFrom(aBr);
		}
		line = aBr.readLine();
		mEvaluationValue = Double.parseDouble(line);
	}

	/**
	 * 事例の数を取得するメソッド．
	 *
	 * @return 事例数
	 */
	public int size() {
		return mExemplars.size();
	}

	/**
	 * 事例の数をセットするメソッド．
	 *
	 * @param aSize 事例数
	 */
	public void size(final int aSize) {
		if (mExemplars.size() != aSize) {
			mExemplars.clear();
			mExemplars.ensureCapacity(aSize);
			for (int i = 0; i < aSize; ++i) {
				mExemplars.add(new Exemplar());
			}
		}
	}

	/**
	 * 指定のインデックスの事例を取得するメソッド．
	 *
	 * @param aIndex インデックス
	 * @return 指定したインデックスの事例
	 */
	public Exemplar exemplar(final int aIndex) {
		return mExemplars.get(aIndex);
	}

	/**
	 * 評価値を取得するメソッド．
	 *
	 * @return 評価値
	 */
	public double evaluationValue() {
		return mEvaluationValue;
	}

	/**
	 * 評価値をセットするメソッド．
	 *
	 * @param aEvaluationValue 評価値
	 */
	public void evaluationValue(final double aEvaluationValue) {
		this.mEvaluationValue = aEvaluationValue;
	}

	/**
	 * 政策が指定の事例を持つかどうかを返すメソッド．
	 *
	 * @param aExem 事例
	 * @return 事例を持っているかどうか
	 */
	public boolean hasExemplar(final Exemplar aExem) {
		for (Exemplar exemplar : mExemplars) {
			if (exemplar.equals(aExem)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 事例の配列に指定の事例を追加するメソッド．
	 *
	 * @param aExem 事例
	 */
	public void add(final Exemplar aExem) {
		mExemplars.add(aExem);
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
		if (!(aObj instanceof Policy))
			return false;
		// 事例数が等しくなければインスタンスは等しくない
		final Policy other = (Policy) aObj;
		final int size = mExemplars.size();
		if (size != other.mExemplars.size())
			return false;
		// それぞれの事例が等しくなければインスタンスは等しくない
		for (int i = 0; i < size; ++i) {
			if (!mExemplars.get(i).equals(other.exemplar(i))) {
				return false;
			}
		}
		// すべての事例と評価値が等しければインスタンスは等しい
		return (mEvaluationValue - other.mEvaluationValue < 1.0e-9 && mEvaluationValue
						- other.mEvaluationValue > -1.0e-9);
	}

	// 事例の配列
	private final ArrayList<Exemplar> mExemplars;
	// 評価値
	private double mEvaluationValue;
}
