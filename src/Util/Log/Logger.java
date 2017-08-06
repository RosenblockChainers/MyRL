package Util.Log;

import java.io.*;

/**
 * Created by bakanaouji on 2017/08/06.
 * ログ書き込みの便利関数．
 */
public class Logger {
	/**
	 * 数値を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aLog      書き込む内容
	 */
	public static void writeTo(
					final String aFileName, final boolean aAppend, final long aLog) {
		try (final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend))) {
			pw.println(aLog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数値を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aLog      書き込む内容
	 */
	public static void writeTo(
					final String aFileName, final boolean aAppend, final double aLog) {
		try (final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend))) {
			pw.println(aLog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数値を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aLog      書き込む内容
	 */
	public static void writeTo(
					final String aFileName, final boolean aAppend, final int aLog) {
		try (final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend))) {
			pw.println(aLog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文字列を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aLog      書き込む内容
	 */
	public static void writeTo(
					final String aFileName, final boolean aAppend, final String aLog) {
		try (final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend))) {
			pw.println(aLog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * オブジェクトの情報を簡単に書き込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aAppend   追加で記録をするかどうか
	 * @param aObj      書き込むオブジェクト
	 */
	public static void writeTo(final String aFileName, final boolean aAppend, final ReadWriter aObj) {
		try (final PrintWriter pw = new PrintWriter(new FileWriter(aFileName, aAppend))) {
			aObj.writeTo(pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * オブジェクトの情報を簡単に読み込める便利メソッド．
	 *
	 * @param aFileName ファイルパス
	 * @param aObj      読み込み先
	 */
	public static void readFrom(final String aFileName, final ReadWriter aObj) {
		try (final BufferedReader br = new BufferedReader(new FileReader(new File(aFileName)))) {
			aObj.readFrom(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
