package Util.Log;

import java.io.*;
import java.util.HashMap;

/**
 * Created by bakanaouji on 2017/08/06.
 * 時系列のログを書き込む用の便利クラス．
 * AutoCloseableによって自動でクローズされるまで，ファイルはクローズされない．
 */
public class LongLogger implements AutoCloseable {
	/**
	 * コンストラクタ．
	 */
	public LongLogger() {
		mPwMap = new HashMap<>();
	}

	/**
	 * 文字列を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aLog      書き込む内容
	 */
	public void writeTo(final String aFileName, final boolean aAppend, final String aLog) {
		if (!mPwMap.containsKey(aFileName)) {
			try {
				final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend));
				mPwMap.put(aFileName, pw);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mPwMap.get(aFileName).println(aLog);
	}


	// PrintWriterのハッシュマップ．
	private HashMap<String, PrintWriter> mPwMap;

	/**
	 * ファイルをクローズする．
	 */
	@Override
	public void close() throws Exception {
		for (PrintWriter pw : mPwMap.values()) {
			pw.close();
		}
	}
}
