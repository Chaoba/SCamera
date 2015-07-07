package com.chaoba.utils.http;

import com.chaoba.utils.http.HttpRequestProxy.ResponseWrapper;


public interface AsynRequestCallback {

    void onCallback(int requestCode,int ackCode, ResponseWrapper wrapper);
}
