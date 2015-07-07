package com.chaoba.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


public class NetManager {
	/**
	 * 获取基站id
	 * 
	 * @param context
	 * @return
	 */
	public static int getCellId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			try {
				GsmCellLocation location = (GsmCellLocation) tm
						.getCellLocation();
				if (location != null) {
					int cellid = location.getCid();

					return cellid;
				} else {
					return 0;
				}
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 获取区域号码
	 * 
	 * @param context
	 * @return
	 */
	public static int getLac(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			try {
				GsmCellLocation location = (GsmCellLocation) tm
						.getCellLocation();
				if (location != null) {
					int lac = location.getLac();
					return lac;
				} else {
					return 0;
				}
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (null != info) {
			return info.isAvailable();
		}
		return false;
	}

	/**
	 * 返回当前的网络环境
	 * 
	 * @param context
	 * @return 1（WIFI）；2(非WIFI)；3(2G)、4(2.5G)、5(3G)
	 */
	public static int getNetWorkType(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mWifi != null && mWifi.isConnected()) {
			return 1;
		} else if (mMobile != null && mMobile.isConnected()) {
			int type = mMobile.getType();
			// 1:联通2G;2:移动2G;4电信2G;5,6,12:电信3G;3,8:联通3G
			switch (type) {
			case 1:
			case 2:
			case 4:
				return 3;
			case 3:
			case 5:
			case 6:
			case 8:
			case 12:
				return 5;
			default:
				return 5;
			}
		} else {
			return 2;
		}

	}

	/**
	 * 判断GPS是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOpenGPS(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS是否打开
		return locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
	}

	/**
	 * 打开GPS
	 * 
	 * @param context
	 * @return
	 */
	public static void toggleGPS(Context context) {
		Intent myIntent = new Intent();
		myIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		myIntent.addCategory("android.intent.category.ALTERNATIVE");
		myIntent.setData(Uri.parse("3"));
		context.sendBroadcast(myIntent);
	}
}
