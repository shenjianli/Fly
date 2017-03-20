package com.shenjianli.fly.api;

import com.shenjianli.fly.model.HttpResult;
import com.shenjianli.fly.model.Test;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by shenjianlis on 2016/8/31.
 * 测试网络请求的接口
 */
public interface TestApi {

    //请求的url地址
    @GET("shenjianli/test")
    //id为请求的参数，可以根据参数不同返回不同的返回结果
    Call<Test> getTestData(@Query("id") String id);


    //请求的url地址
    @GET("shenjianli/rxandroid")
    //id为请求的参数，可以根据参数不同返回不同的返回结果
    Observable<HttpResult<Test>> getTestDataByRxAndroid(@Query("id") String id);

}
