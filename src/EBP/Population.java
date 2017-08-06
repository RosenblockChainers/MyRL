package EBP;

import java.util.ArrayList;

/**
 * Created by bakanaouji on 2017/07/20.
 * 政策の集団を表すクラス．
 */
public class Population implements Cloneable {
	/**
	 * コンストラクタ．
	 */
	public Population() {
		mArray = new ArrayList<>();
	}

	/**
	 * コピーコンストラクタ．
	 *
	 * @param aSrc コピー先
	 */
	public Population(final Population aSrc) {
		mArray = new ArrayList<>(aSrc.mArray);
	}

	/**
	 * 自身のディープコピーを返すメソッド．
	 *
	 * @return 自身のディープコピー
	 */
	@Override
	public Population clone() {
		return new Population(this);
	}

	/**
	 * 集団の政策数（政策数）を取得するメソッド．
	 *
	 * @return 政策数
	 */
	public int size() {
		return mArray.size();
	}

	/**
	 * 集団の政策数（政策数）をセットするメソッド．
	 *
	 * @param aSize 政策数
	 */
	public void size(final int aSize) {
		if (mArray.size() != aSize) {
			mArray.clear();
			mArray.ensureCapacity(aSize);
			for (int i = 0; i < aSize; ++i) {
				mArray.add(new Policy());
			}
		}
	}

	/**
	 * 指定のインデックスの政策を返すメソッド．
	 *
	 * @param aIndex インデックス
	 * @return 指定したインデックスの政策
	 */
	public Policy policy(final int aIndex) {
		return mArray.get(aIndex);
	}

	/**
	 * 政策の配列に指定の政策を追加するメソッド．
	 *
	 * @param aPolicy 政策
	 */
	public void add(final Policy aPolicy) {
		mArray.add(aPolicy);
	}

	/**
	 * 指定のインデックスの政策を配列から消去するメソッド．
	 * swapしてindexが一番後方に来るようにしてからremoveした方が早いと思われる．
	 *
	 * @param aIndex 配列のインデックス
	 */
	public void remove(final int aIndex) {
		mArray.remove(aIndex);
	}

	/**
	 * 指定の政策を配列から消去するメソッド．
	 * swapして削除する政策が一番後方に来るようにしてからremoveした方が早いと思われる．
	 *
	 * @param aPolicy 政策
	 */
	public void remove(final Policy aPolicy) {
		for (int i = 0; i < mArray.size(); ++i) {
			if (mArray.get(i).equals(aPolicy)) {
				mArray.remove(i);
				return;
			}
		}
	}

	/**
	 * 二つの指定のインデックスの政策を入れ替えるメソッド．
	 *
	 * @param aIndex1 入れ替える政策のインデックス
	 * @param aIndex2 入れ替える政策のインデックス
	 */
	public void swap(final int aIndex1, final int aIndex2) {
		mArray.set(aIndex1, mArray.set(aIndex2, mArray.get(aIndex1)));
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
		if (!(aObj instanceof Population))
			return false;
		// 集団サイズが等しくなければインスタンスは等しくない
		final Population other = (Population) aObj;
		int size = mArray.size();
		if (size != other.mArray.size())
			return false;
		// それぞれの政策が等しくなければインスタンスは等しくない
		for (int i = 0; i < size; ++i) {
			if (!mArray.get(i).equals(other.policy(i))) {
				return false;
			}
		}
		return true;
	}

	// 政策の配列
	private final ArrayList<Policy> mArray;

}
