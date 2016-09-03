package com.shenjianli.fly.core;

import com.shenjianli.fly.api.TestApi;
import com.shenjianli.fly.model.TestData;
import com.shenjianli.shenlib.net.HttpResult;
import com.shenjianli.shenlib.net.NetClient;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by shenjianli on 16/7/30.
 */
public class HttpMethods {

    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
        retrofit = NetClient.retrofit();

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用来统一处理Http的errNo,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new ApiException(httpResult.getErrNo());
            }
            return httpResult.getData();
        }
    }


    public void getTestData(Subscriber<TestData> subscriber, String id){
        if(null == retrofit){
            retrofit = NetClient.retrofit();
        }
        TestApi testService = retrofit.create(TestApi.class);
        testService.getTestData(id)
                .map(new HttpResultFunc<TestData>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
