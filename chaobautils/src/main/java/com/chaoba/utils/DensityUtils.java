package com.chaoba.utils;

import android.content.Context;
import android.util.TypedValue;

public class DensityUtils {
	private static TypedValue mTmpValue = new TypedValue();

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
	 */
	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 获取xml里定义的尺寸
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static int getXmlDef(Context context, int id) {

		synchronized (mTmpValue) {

			TypedValue value = mTmpValue;

			context.getResources().getValue(id, value, true);

			return (int) TypedValue.complexToFloat(value.data);

		}

	}
}
