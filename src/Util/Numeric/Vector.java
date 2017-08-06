package Util.Numeric;

import Util.Log.ReadWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by bakanaouji on 2017/07/23.
 * ベクトルを表すクラス．
 */
public class Vector implements ReadWriter {
	/**
	 * コンストラクタ．
	 *
	 * @param aDim 次元数
	 */
	public Vector(final int aDim) {
		// 全要素を0初期化
		mValue = new double[aDim];
		for (int i = 0; i < aDim; ++i) {
			mValue[i] = 0.0;
		}
	}

	/**
	 * ２次元のベクトル用のコンストラクタ．
	 * ゲームの座標を取り扱う場合などに使うと便利．
	 *
	 * @param aX x
	 * @param aY y
	 */
	public Vector(final double aX, final double aY) {
		mValue = new double[2];
		mValue[0] = aX;
		mValue[1] = aY;
	}

	/**
	 * コピーコンストラクタ．
	 *
	 * @param aVec コピー元
	 */
	public Vector(final Vector aVec) {
		// ディープコピー
		mValue = new double[aVec.mValue.length];
		System.arraycopy(aVec.mValue, 0, mValue, 0, aVec.mValue.length);
	}

	/**
	 * コピーメソッド．
	 *
	 * @param aVec コピー元
	 */
	public void copyFrom(final Vector aVec) {
		if (mValue.length != aVec.mValue.length) {
			mValue = new double[aVec.mValue.length];
		}
		System.arraycopy(aVec.mValue, 0, mValue, 0, aVec.mValue.length);
	}

	/**
	 * 指定の次元の要素を取得．
	 *
	 * @param aIndex 取得する要素の次元
	 * @return 要素の値
	 */
	public double value(final int aIndex) {
		return mValue[aIndex];
	}

	/**
	 * 指定の次元の要素の値をセット．
	 *
	 * @param aIndex セットする要素の次元
	 * @param aValue セットする値
	 */
	public void value(final int aIndex, final double aValue) {
		mValue[aIndex] = aValue;
	}

	/**
	 * 次元数を取得．
	 *
	 * @return 次元数
	 */
	public int dimension() {
		return mValue.length;
	}

	/**
	 * 次元数を設定するメソッド．
	 */
	public void dimension(final int aDimension) {
		if (mValue.length != aDimension) {
			mValue = new double[aDimension];
		}
	}

	/**
	 * ベクトルの足し算を行うメソッド．
	 * どちらのベクトルも上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec1 ベクトル1
	 * @param aVec2 ベクトル2
	 * @return 足し算したベクトル
	 */
	public static Vector add(final Vector aVec1, final Vector aVec2) {
		final Vector vec = new Vector(aVec1);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] += aVec2.mValue[i];
		}
		return vec;
	}

	/**
	 * ベクトルの各要素に指定値を足したベクトルを返すメソッド．
	 * ベクトルは上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec ベクトル
	 * @param aX   足し込む値
	 * @return 足し算したベクトル
	 */
	public static Vector add(final Vector aVec, final double aX) {
		final Vector vec = new Vector(aVec);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] += aX;
		}
		return vec;
	}

	/**
	 * ベクトルの足し算を行うメソッド．
	 * 呼び出した側のベクトルの値が上書きされる．
	 *
	 * @param aVec 足し込むベクトル
	 * @return 足し算したベクトル
	 */
	public Vector add(final Vector aVec) {
		for (int i = 0; i < mValue.length; ++i) {
			mValue[i] += aVec.mValue[i];
		}
		return this;
	}

	/**
	 * ベクトルの引き算を行うメソッド．
	 * どちらのベクトルも上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec1 ベクトル1
	 * @param aVec2 ベクトル2
	 * @return 引き算したベクトル
	 */
	public static Vector sub(final Vector aVec1, final Vector aVec2) {
		final Vector vec = new Vector(aVec1);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] -= aVec2.mValue[i];
		}
		return vec;
	}

	/**
	 * ベクトルの各要素から指定値を引いたベクトルを返すメソッド．
	 * ベクトルは上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec ベクトル
	 * @param aX   引く値
	 * @return 引き算したベクトル
	 */
	public static Vector sub(final Vector aVec, final double aX) {
		final Vector vec = new Vector(aVec);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] -= aX;
		}
		return vec;
	}

	/**
	 * ベクトルの引き算を行うメソッド．
	 * 呼び出した側のベクトルの値が上書きされる．
	 *
	 * @param aVec 引くベクトル
	 * @return 引き算したベクトル
	 */
	public Vector sub(final Vector aVec) {
		for (int i = 0; i < mValue.length; ++i) {
			mValue[i] -= aVec.mValue[i];
		}
		return this;
	}

	/**
	 * ベクトルの各要素に指定値を掛けたベクトルを返すメソッド．
	 * ベクトルは上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec ベクトル
	 * @param aX   掛ける値
	 * @return 掛け算したベクトル
	 */
	public static Vector times(final Vector aVec, final double aX) {
		final Vector vec = new Vector(aVec);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] *= aX;
		}
		return vec;
	}

	/**
	 * ベクトルの内積を返すメソッド．
	 *
	 * @param aVec1 ベクトル1
	 * @param aVec2 ベクトル2
	 * @return 内積
	 */
	public static double times(final Vector aVec1, final Vector aVec2) {
		double ip = 0.0;
		for (int i = 0; i < aVec1.mValue.length; ++i) {
			ip += aVec1.mValue[i] * aVec2.mValue[i];
		}
		return ip;
	}

	/**
	 * ベクトルの各要素を指定値で割ったベクトルを返すメソッド．
	 * ベクトルは上書きされず，新しいベクトルが返される．
	 *
	 * @param aVec ベクトル
	 * @param aX   割る値
	 * @return 割り算したベクトル
	 */
	public static Vector devide(final Vector aVec, final double aX) {
		final Vector vec = new Vector(aVec);
		for (int i = 0; i < vec.mValue.length; ++i) {
			vec.mValue[i] /= aX;
		}
		return vec;
	}

	/**
	 * L2ノルムを返すメソッド．
	 *
	 * @return L2ノルム
	 */
	public double norm() {
		return Math.sqrt(Vector.times(this, this));
	}

	/**
	 * ベクトルの正規化を行うメソッド．
	 */
	public void normalize() {
		final double norm = this.norm();
		for (int i = 0; i < mValue.length; ++i) {
			mValue[i] /= norm;
		}
	}

	/**
	 * 自身の情報を書き込むメソッド．
	 *
	 * @param aPw PrintWriter
	 */
	@Override
	public void writeTo(final PrintWriter aPw) {
		final int dimension = mValue.length;
		aPw.println(dimension);
		for (double value : mValue) {
			aPw.print(value + ",");
		}
		aPw.println();
	}

	/**
	 * ファイルから情報を読み込むメソッド．
	 *
	 * @param aBr BufferedReader
	 * @throws IOException IOException
	 */
	@Override
	public void readFrom(final BufferedReader aBr) throws IOException {
		String line;
		line = aBr.readLine();
		int dimension = Integer.parseInt(line);
		if (dimension != mValue.length) {
			mValue = new double[dimension];
		}
		line = aBr.readLine();
		String[] strArray = line.split(",");
		for (int i = 0; i < dimension; ++i) {
			mValue[i] = Double.parseDouble(strArray[i]);
		}
	}

	// 要素
	private double[] mValue;

}
