package com.chaoba.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * A Manager class to show toast easier.
 * @author Liyanshun
 *
 */
public class ToastManager {

	/** show string toast in short time */
	private static final int SHOW_STRING_SHORT = 0;
	/** show resource string toast in short time */
	private static final int SHOW_RESOURCE_STRING_SHORT = 1;
	/** show string toast in long time */
	private static final int SHOW_STRING_LONG = 2;
	/** show resource string toast in long time */
	private static final int SHOW_RESOURCE_STRING_LONG = 3;
	private static Context mContext;
	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_STRING_SHORT:
				show(mContext, (String) msg.obj, false);
				break;
			case SHOW_RESOURCE_STRING_SHORT:
				show(mContext, mContext.getString(msg.arg1), false);
				break;
			case SHOW_STRING_LONG:
				show(mContext, (String) msg.obj, true);
				break;
			case SHOW_RESOURCE_STRING_LONG:
				show(mContext, mContext.getString(msg.arg1), true);
				break;
			}

			super.handleMessage(msg);
		}

	};

	/**
	 * show string type of toast in short time
	 * 
	 * @param context
	 * @param msg
	 *            - string to show
	 */
	public static void showShort(Context context, String msg) {
		mContext = context;
		Message message = new Message();
		message.what = SHOW_STRING_SHORT;
		message.obj = msg;
		mHandler.sendMessage(message);
	}

	/**
	 * show resource string of toast in short time
	 * 
	 * @param context
	 * @param id
	 */
	public static void showShort(Context context, int id) {
		mContext = context;
		Message message = new Message();
		message.what = SHOW_RESOURCE_STRING_SHORT;
		message.arg1 = id;
		mHandler.sendMessage(message);
	}

	/**
	 * show string type of toast in long time
	 * 
	 * @param context
	 * @param msg
	 *            - string to show
	 */
	public static void showLong(Context context, String msg) {
		mContext = context;
		Message message = new Message();
		message.what = SHOW_STRING_LONG;
		message.obj = msg;
		mHandler.sendMessage(message);
	}

	/**
	 * show resource string of toast in long time
	 * 
	 * @param context
	 * @param id
	 */
	public static void showLong(Context context, int id) {
		mContext = context;
		Message message = new Message();
		message.what = SHOW_RESOURCE_STRING_LONG;
		message.arg1 = id;
		mHandler.sendMessage(message);
	}

	private static void show(Context context, String msg, boolean isLong) {
		if (isLong) {
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			toast.show();
		}
		// release the context in case of oom
		mContext = null;
	}

}
