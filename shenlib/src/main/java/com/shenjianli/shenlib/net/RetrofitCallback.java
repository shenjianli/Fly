package com.shenjianli.shenlib.net;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public abstract class RetrofitCallback<T> implements Callback<T> {


	@Override
	public void onResponse(Response<T> response, Retrofit retrofit) {
		T model = response.body();
		if(null == model){
			// 404 or the response cannot be converted to User.
			ResponseBody responseBody = response.errorBody();
			if (responseBody != null) {
				try {
					onFail(responseBody.string());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				onFail("responseBody = null");
			}
		}
		else {//200
			onSuccess(model);
		}
	}

	@Override
	public void onFailure(Throwable t) {
		onFail(t.getMessage());
	}
	
	public abstract void onSuccess(T t);
	
	public abstract void onFail(String errorMsg);
}
