package com.chaoba.utils.http;


import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientFactory {

    private static DefaultHttpClient staticHttpClient = null;


    public static synchronized DefaultHttpClient getInstance() {
        if (staticHttpClient == null) {
            HttpParams params = new BasicHttpParams();
            ConnManagerParams.setMaxTotalConnections(params, 100);
            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRoute() {
                @Override
                public int getMaxForRoute(HttpRoute route) {
                    return 50;
                }});
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

            staticHttpClient = new DefaultHttpClient(cm, params);
            staticHttpClient.addRequestInterceptor(new HttpClientInterceptor().resquestInterceptor);
            staticHttpClient
                    .addResponseInterceptor(new HttpClientInterceptor().responseInterceptor);

        }

        return staticHttpClient;
    }

    public static synchronized DefaultHttpClient getNewInstance() {
        staticHttpClient = null;
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRoute() {
            @Override
            public int getMaxForRoute(HttpRoute route) {
                return 50;
            }});
        
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        HttpConnectionParams.setConnectionTimeout(params, 15000);
        HttpConnectionParams.setSoTimeout(params, 15000);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        staticHttpClient = new DefaultHttpClient(cm, params);
        staticHttpClient.addRequestInterceptor(new HttpClientInterceptor().resquestInterceptor);
        staticHttpClient.addResponseInterceptor(new HttpClientInterceptor().responseInterceptor);

        return staticHttpClient;
    }
}

