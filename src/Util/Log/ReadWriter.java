package Util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by bakanaouji on 2017/08/06.
 * 読み書きを簡単に行うためのインターフェース．
 */
public interface ReadWriter {
	/**
	 * 書き込みメソッド．
	 *
	 * @param aPw PrintWriter
	 */
	void writeTo(final PrintWriter aPw);

	/**
	 * 読み込みメソッド．
	 *
	 * @param aBr BufferedReader
	 * @throws IOException IOException
	 */
	void readFrom(final BufferedReader aBr) throws IOException;
}
