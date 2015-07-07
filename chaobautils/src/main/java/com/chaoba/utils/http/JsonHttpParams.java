package com.chaoba.utils.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

/**
 * This class used to convert params to json
 * 
 */
public class JsonHttpParams {
	public static final String JSON_ARRAY_SPLIT = "@*@";
	private static final String TAG = "JsonHttpParams";
	private Bundle params;
	private List<String> keys;

	public JsonHttpParams() {
		params = new Bundle();
		keys = new ArrayList<String>();
	}

	public void putParam(String key, String value) {
		if (!params.containsKey(key)) {
			keys.add(key);
			params.putString(key, value);
		}
	}

	public List<String> getParamKeys() {
		return keys;
	}

	public String getParamKey(int index) {
		String result = null;
		if (keys.size() > 0 && index < keys.size()) {
			result = keys.get(index);
		}
		return result;
	}

	public String getParamValue(String key) {
		return params.getString(key);
	}

	public String getUrlParam() {
		String result = "";
		String key = null;
		for (int index = 0; index < keys.size(); index++) {
			key = getParamKey(index);
			if (key != null) {
				if (index == 0) {
					result += "?" + key + "=" + getParamValue(key);
				} else {
					result += "&" + key + "=" + getParamValue(key);
				}
			}
		}
		Log.d(TAG, result);
		return result;
	}

	public StringEntity convertParamsToEntity(boolean isJson) {
		JSONObject sjson = new JSONObject();
		StringEntity se = null;
		String key = null;
		String value = null;
		if (isJson) {
			try {
				for (int index = 0; index < keys.size(); index++) {
					key = getParamKey(index);
					value = getParamValue(key);
					if (value != null) {
						if (value.contains(JSON_ARRAY_SPLIT)) {
							String[] items = value.split(JSON_ARRAY_SPLIT);
							JSONArray jsonArray = new JSONArray();
							JSONObject subJson;
							for (int i = 0; i < items.length; i++) {
								subJson = new JSONObject();
								subJson.put(key, items[i]);
								jsonArray.put(subJson);
							}
							sjson.put("items", jsonArray);
						} else {
							sjson.put(key, value);
						}

					} else {
						sjson.put(key, value);
					}
				}
				Log.i(TAG, "post param:" + sjson.toString());
				se = new StringEntity(sjson.toString(), "utf-8");
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.toString());
			}
		} else {
			String result = "";
			for (int index = 0; index < keys.size(); index++) {
				key = getParamKey(index);
				if (key != null) {
					if (index == 0) {
						result += key + "=" + getParamValue(key);
					} else {
						result += "\n" + key + "=" + getParamValue(key);
					}
				}
			}
			Log.i(TAG, result);
			try {
				se = new StringEntity(result);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.toString());
			}
		}
		return se;
	}

}
