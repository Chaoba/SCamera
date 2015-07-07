package com.chaoba.utils.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.util.Log;

public class HttpRequest {
	private static final String TAG = "HttpRequest";
	private final DefaultHttpClient httpClient;
	static ExecutorService executors = Executors.newCachedThreadPool();

	public HttpRequest() {
		httpClient = HttpClientFactory.getInstance();
	}

	/**
	 * send common post request
	 * @param url
	 * @param params
	 * @param dialog
	 * @param headers
	 * @param callback
	 * @param proxy
	 */
	public void asynPost(int requsetCode,String url, JsonHttpParams params, Dialog dialog,
			Header[] headers, AsynRequestCallback callback,
			HttpRequestProxy proxy) {
		Log.d(TAG, "asynPost--****" + url);
		boolean jsonMode = false;
		HttpPost post = new HttpPost(url);
		if (headers != null) {
			for (Header header : headers) {
				if (header.getValue().contains("json")) {
					jsonMode = true;
				}
				post.addHeader(header);
			}
		}
		Log.d(TAG, "jsonMode:" + jsonMode + "params:" + params);
		if (params != null) {
			StringEntity entity = null;
			entity = params.convertParamsToEntity(jsonMode);
			post.setEntity(entity);
		}
		new RequestAsyncTask(requsetCode,dialog, httpClient, callback, proxy)
				.executeOnExecutor(executors, post);
	}
	
	/**
	 * send post request, such as file
	 * 
	 * @param url
	 * @param params
	 * @param dialog
	 * @param header
	 * @param callback
	 * @param proxy
	 */
	public void asynPost(int requsetCode,String url, JsonHttpParams params, String filePath,
			Dialog dialog, Header[] headers, AsynRequestCallback callback,
			HttpRequestProxy proxy) {
		Log.d(TAG, "asynPostFile" + url);
		Log.d(TAG, filePath);
		HttpPost post = new HttpPost(url);
		MultipartEntity mpEntity = new MultipartEntity();
		File file = new File(filePath);
		try {
			FileBody fileBody = new FileBody(file);
			mpEntity.addPart("data", fileBody);
			if (params != null) {
				List<String> keys = params.getParamKeys();
				String key, value;
				for (int index = 0; index < keys.size(); index++) {
					key = params.getParamKey(index);
					if (key != null) {
						value = params.getParamValue(key);
						StringBody valueBody;
						valueBody = new StringBody(
								value,
								Charset.forName(org.apache.http.protocol.HTTP.UTF_8));
						Log.d(TAG,"add part key:" + key + " value:" + value);
						mpEntity.addPart(key, valueBody);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		post.setEntity(mpEntity);
		new RequestAsyncTask(requsetCode,dialog, httpClient, callback, proxy)
				.executeOnExecutor(executors, post);
	}

	public void asynGet(int requsetCode,String url, JsonHttpParams params, Dialog dialog,
			Header[] headers, AsynRequestCallback callback,
			HttpRequestProxy proxy) {
		Log.d(TAG, "asynGet" + url);
		if (params != null) {
			url += params.getUrlParam();
		}

		HttpGet get = new HttpGet(url);

		new RequestAsyncTask(requsetCode,dialog, httpClient, callback, proxy)
				.executeOnExecutor(executors, get);
	}

}
