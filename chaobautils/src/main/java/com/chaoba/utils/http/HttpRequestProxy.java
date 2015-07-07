package com.chaoba.utils.http;

import org.apache.http.Header;

import android.app.Dialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public abstract class HttpRequestProxy<Params> {
	private static final String TAG = "ObdHttpRequestProxy";

	public void compositeParams(String... params) {
		if (params != null) {
			for (String para : params) {
				Log.d(TAG, "compositeParams:" + para);
			}
		}
	}

	public abstract Header[] compositeHead();

	public abstract void request(AsynRequestCallback callback, int requsetCode,
			Header[] header, Dialog dialog);

	public final void startRequest(AsynRequestCallback callback,
			int requsetCode, Dialog dialog, String... params) {
		if (params != null) {
			for (String param : params) {
				if (param != null) {
					Log.d(TAG, param);
				}
			}
		}
		compositeParams(params);
		request(callback, requsetCode, compositeHead(), dialog);
	}

	public abstract ResponseWrapper convertJsonToObject(String json);

	public ResponseWrapper convertErrorJsonToObject(String json) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(json).getAsJsonArray();
		JsonElement obj = Jarray.get(0);
		ResponseWrapper errorMsg = gson.fromJson(obj, ResponseWrapper.class);
		return errorMsg;
	}

	public static class ResponseWrapper {
		public String message;

		public String getMessage() {
			return message;
		}

	}

}
