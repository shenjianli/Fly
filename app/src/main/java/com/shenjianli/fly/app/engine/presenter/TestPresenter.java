package com.shenjianli.fly.app.engine.presenter;

import android.content.Context;

import com.shen.netclient.NetClient;
import com.shen.netclient.engine.RetrofitCallback;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.api.TestApi;
import com.shenjianli.fly.app.engine.DataView;
import com.shenjianli.fly.app.engine.HttpResultFunc;
import com.shenjianli.fly.model.Test;

import retrofit2.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by edianzu on 2016/9/12.
 */
public class TestPresenter extends BasePresenter<DataView> {

    Context context;

    public TestPresenter(Context context){
        this.context = context;
    }

    @Override
    public void attachView(DataView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void loadData(){
        //在这里写网络请求的代码，从Server中请求到JavaBena UsBoxEntity
        //调用MvpView中的loadData方法，将请求到的数据传回View层，让View层更新数据；
        getMvpView().startLoading();
        TestApi api = NetClient.retrofit().create(TestApi.class);
        Call<Test> data = api.getTestData("001");
        data.enqueue(new RetrofitCallback<Test>() {
            @Override
            public void onSuccess(Test data) {
                getMvpView().loadData(data);
                getMvpView().hideLoading();
            }

            @Override
            public void onFail(String errorMsg) {
                getMvpView().showError(errorMsg);
                getMvpView().hideLoading();
            }
        });

    }

    private Subscriber<Test> testSubscriber;
    public void loadDataByRxAndroid() {

        testSubscriber = new Subscriber<Test>() {
            @Override
            public void onCompleted() {
                LogUtils.i("表求完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i(e.getStackTrace().toString());
                getMvpView().showError(e.getMessage());
                getMvpView().hideLoading();
            }

            @Override
            public void onNext(Test data) {
                LogUtils.i("获取数据成功");
                getMvpView().loadData(data);
                getMvpView().hideLoading();
            }
        };

        getMvpView().startLoading();
        TestApi testApi = NetClient.retrofit().create(TestApi.class);
        testApi.getTestDataByRxAndroid("001").map(new HttpResultFunc<Test>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testSubscriber);
    }
}
