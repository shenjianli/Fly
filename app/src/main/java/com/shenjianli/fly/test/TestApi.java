package com.shenjianli.fly.test;

import com.shenjianli.shenlib.net.HttpResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by edianzu on 2016/8/31.
 */
public interface TestApi {
    @GET("shenjianli/test")
    Call<Test> getWeather(@Query("id") String id);

    @GET("shenjianli/test")
    Observable<HttpResult<TestData>> getTestData(@Query("id") String id);
}
