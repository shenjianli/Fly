package com.shenjianli.shenlib.net.interceptor;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;



public class QueryParameterInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request originalRequest = chain.request();
        Request request;
        String method = originalRequest.method();
        Headers headers = originalRequest.headers();
        HttpUrl modifiedUrl = originalRequest.httpUrl().newBuilder()
                // Provide your custom parameter here
                .addQueryParameter("platform", "android")
                .addQueryParameter("version", "1.0.0")              
                .build();
        request = originalRequest.newBuilder().url(modifiedUrl).build();
        return chain.proceed(request);
	}

}
