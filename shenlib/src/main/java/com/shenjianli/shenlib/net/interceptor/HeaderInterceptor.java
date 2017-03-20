package com.shenjianli.shenlib.net.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;




public class HeaderInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {

		Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("AppType", "Android")
				.header("Accept", "application/json")
//                .header("Content-Type", "application/json")
                .method(originalRequest.method(), originalRequest.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
	}

}
