package com.chaoba.utils;

import java.text.DecimalFormat;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.MeasureSpec;

public class Util {

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}

	public static void makeACall(Context context, String number) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + number));
		context.startActivity(intent);
	}

	public static String getVersion(Context c) {
		PackageManager manager = c.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(c.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null) {
			return info.versionName;
		} else {
			return "1.0";
		}
	}

	/**
	 * double去掉小数点后面的位数，显示整数
	 * 
	 * @param f
	 * @param scale
	 * @return
	 */
	public static String formartDecimal(double f, int scale) {
		DecimalFormat df = new DecimalFormat("#");
		return df.format(f);
	}

	/**
	 * double去掉小数点后面的1位数，如果后面一位为0，则只保留为整数
	 * 
	 * @param f
	 * @return
	 */
	public static String formartDecimalForABit(double f) {
		DecimalFormat df = new DecimalFormat("0.0");
		String formartStr = df.format(f);
		if (formartStr.endsWith("0")) {
			formartStr = formartStr.substring(0, formartStr.length() - 2);
		}
		return formartStr;
	}

	/**
	 * double去掉小数点后面的2位数，如果后面一位为0，则只保留为整数
	 * 
	 * @param f
	 * @return
	 */
	public static String formartDecimalFor2Bit(double f) {
		DecimalFormat df = new DecimalFormat("0.00");
		String formartStr = df.format(f);
		if (formartStr.endsWith("00")) {
			formartStr = formartStr.substring(0, formartStr.length() - 3);
		} else if (formartStr.endsWith("0")) {
			formartStr = formartStr.substring(0, formartStr.length() - 1);
		}
		return formartStr;
	}

	/**
	 * 判断app是否在后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean runInBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
