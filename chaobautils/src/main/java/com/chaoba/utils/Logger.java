package com.chaoba.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

/**
 * A class to control log show and save it to sdcard.
 * @author Liyanshun
 *
 */
public class Logger {
	private static final String TAG = "Logger";
	/** whether show log in logcat.*/
	private static boolean mShowLogInLogCat = true;
	/** whether save log to sdcard.*/
	private static boolean mSaveLogToFile = true;
	private static PrintWriter mOutStream = null;
	private static Calendar mCalendar = Calendar.getInstance();
	/** the dirctory to save log.*/
	public static final String DIR = "Log";
	private static String FILENAME;
	private static String CLASS_NAME =null;;

	private static String getFunctionName() {
		if(CLASS_NAME==null){
			CLASS_NAME=Logger.class.getName();
		}
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(CLASS_NAME)) {
				continue;
			}
			return st.getFileName() + "[Line: " + st.getLineNumber() + "] ";
		}
		return null;
	}

	public static void openLogFile() {
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		//set the log file name according current time
		FILENAME = TAG + (mCalendar.get(Calendar.MONTH) + 1) + "-"
				+ mCalendar.get(Calendar.DAY_OF_MONTH) + "-"
				+ mCalendar.get(Calendar.HOUR_OF_DAY) + "-"
				+ mCalendar.get(Calendar.MINUTE) + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + DIR + File.separator + FILENAME);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			try {
				if (mOutStream != null) {
					write("end of this log");
					mOutStream.close();
				}
				mOutStream = new PrintWriter(file, "utf-8");
				write("Begin print log");
			} catch (FileNotFoundException e) {
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

	}

	public static void i(String tag, Object message) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.i(tag, message.toString());
			} else {
				Log.i(tag, name + message.toString());
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, message);
			}
		}
	}

	public static void i(Object message) {
		i(TAG, message);
	}

	public static void d(String tag, Object message) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.d(tag, message.toString());
			} else {
				Log.d(tag, name + message.toString());
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, message);
			}
		}
	}

	public static void d(Object message) {
		d(TAG, message);
	}

	public static void v(String tag, Object message) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.v(tag, message.toString());
			} else {
				Log.v(tag, name + message.toString());
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, message);
			}
		}
	}

	public static void v(Object message) {
		v(TAG, message);
	}

	public static void w(String tag, Object message) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.w(tag, message.toString());
			} else {
				Log.w(tag, name + message.toString());
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, message);
			}
		}
	}

	public static void w(Object message) {
		w(TAG, message);
	}

	public static void e(String tag, Object message) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.e(tag, message.toString());
			} else {
				Log.e(tag, name + message);
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, message);
			}
		}
	}

	public static void e(Object message) {
		e(TAG, message);
	}

	public static void e(String tag, Exception e) {
		if (mShowLogInLogCat) {
			String name = getFunctionName();
			if (name == null) {
				Log.e(tag, e.getMessage());
			} else {
				Log.e(tag, name + e.getMessage());
			}
			if (mSaveLogToFile) {
				writeMsgToFile(name, e.getMessage());
			}
		}
	}

	public static void e(Exception e) {
		e(TAG, e);
	}

	private static void writeMsgToFile(String name, Object message) {
		if (mOutStream == null) {
			openLogFile();
		}
		if (mOutStream != null && message != null) {
			mCalendar.setTimeInMillis(System.currentTimeMillis());
			StringBuffer buffer = new StringBuffer();
			buffer.append("[time=");
			buffer.append(mCalendar.get(Calendar.HOUR_OF_DAY));
			buffer.append(":");
			buffer.append(mCalendar.get(Calendar.MINUTE));
			buffer.append(":");
			buffer.append(mCalendar.get(Calendar.SECOND));
			buffer.append("]  ");
			if (name != null) {
				buffer.append(name);
				if (name.length() < 50) {
					for (int i = 0; i < 50 - name.length(); i++) {
						buffer.append(" ");
					}
				}
			}
			buffer.append(message.toString());
			write(buffer.toString());
		}
	}

	static private void write(String s) {
		mOutStream.println(s);
		if (mOutStream.checkError()) {
			Log.e(TAG, "write error");
		}
	}

	public static void close() {
		if (mOutStream != null) {
			write("end log");
			mOutStream.close();
			mOutStream = null;
		}
	}
}
