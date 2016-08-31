package com.shenjianli.fly.test;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by edianzu on 2016/8/31.
 */
public interface TestApi {
    @GET("shenjianli/test")
    Call<Test> getWeather(@Query("Id") String Id);
}
