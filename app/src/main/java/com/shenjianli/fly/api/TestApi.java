package com.shenjianli.fly.api;

import com.shenjianli.fly.model.TestData;
import com.shenjianli.shenlib.net.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by edianzu on 2016/8/31.
 */
public interface TestApi {

    @GET("shenjianli/test")
    Observable<HttpResult<TestData>> getTestData(@Query("id") String id);
}
