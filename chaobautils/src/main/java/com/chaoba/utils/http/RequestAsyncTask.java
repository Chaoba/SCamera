package com.chaoba.utils.http;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.chaoba.utils.Logger;
import com.chaoba.utils.http.HttpRequestProxy.ResponseWrapper;

/**
 * The task that send http request in background
 * 
 * @author Liyanshun
 * 
 */
public class RequestAsyncTask extends AsyncTask<Object, Integer, Integer> {
	private static final String TAG = "RequestAsyncTask";
	private Dialog dialog;
	private HttpClient client;
	private AsynRequestCallback callback;
	private ResponseWrapper wrapper;
	private HttpRequestProxy proxy;
	private int mRequestCode;

	public RequestAsyncTask(int requsetCode, Dialog dialog, HttpClient client,
			AsynRequestCallback callback, HttpRequestProxy proxy) {
		this.mRequestCode = requsetCode;
		this.dialog = dialog;
		this.client = client;
		this.callback = callback;
		this.proxy = proxy;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (IllegalArgumentException e) {
			}
		}
		Logger.d("on post");
		if (callback != null && result != HttpStatus.SC_UNAUTHORIZED) {
			Logger.d("on callback");
			callback.onCallback(mRequestCode, result, wrapper);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (dialog != null) {
			dialog.show();
		}

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected Integer doInBackground(Object... params) {
		int resCode = 0;
		HttpUriRequest request = (HttpUriRequest) params[0];
		Log.d(TAG, request.getURI().toString());

		resCode = sendRequest(resCode, request);

		return resCode;
	}

	private int sendRequest(int resCode, HttpUriRequest request) {
		try {
			Logger.i(request.toString());
			
			HttpResponse response = client.execute(request);
			resCode = response.getStatusLine().getStatusCode();
			String json ="";// EntityUtils.toString(response.getEntity());
			Log.d(TAG, "result:" + resCode);
			if (resCode == HttpStatus.SC_OK || resCode == HttpStatus.SC_CREATED) {
//				if (proxy != null) {
//					Log.d(TAG, resCode + ":" + json.toString());
//					wrapper = proxy.convertJsonToObject(json);
//				}
			} else {
				Log.d(TAG, json);
				json = "[{\"message\":\"Netwrok error!\"}]";
				wrapper = proxy.convertErrorJsonToObject(json);
			}

		} catch (SocketTimeoutException e) {
			resCode = HttpStatus.SC_REQUEST_TIMEOUT;
			String json = "[{\"message\":\"Netwrok error!\"}]";
			wrapper = proxy.convertErrorJsonToObject(json);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			resCode = HttpStatus.SC_REQUEST_TIMEOUT;
			String json = "[{\"message\":\"Netwrok error!\"}]";
			wrapper = proxy.convertErrorJsonToObject(json);
			e.printStackTrace();
		} catch (IOException e) {
			resCode = HttpStatus.SC_REQUEST_TIMEOUT;
			String json = "[{\"message\":\"Netwrok error!\"}]";
			wrapper = proxy.convertErrorJsonToObject(json);
			e.printStackTrace();
		} catch (Exception e) {
			resCode = HttpStatus.SC_REQUEST_TIMEOUT;
			String json = "[{\"message\":\"Netwrok error!\"}]";
			wrapper = proxy.convertErrorJsonToObject(json);
			e.printStackTrace();
		}
		return resCode;
	}

}
