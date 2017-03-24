package com.shenjianli.fly.app.engine.presenter;

import android.content.Context;

import com.shen.netclient.NetClient;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.api.QuoteApi;
import com.shenjianli.fly.app.engine.HttpResultFunc;
import com.shenjianli.fly.app.engine.view.QuoteView;
import com.shenjianli.fly.model.Quote;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by edianzu on 2016/9/12.
 */
public class QuotePresenter extends BasePresenter<QuoteView> {

    Context context;

    public QuotePresenter(Context context){
        this.context = context;
    }

    @Override
    public void attachView(QuoteView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        if(null != quoteSubscriber){
            quoteSubscriber.unsubscribe();
        }
        super.detachView();
    }

    private Subscriber<Quote> quoteSubscriber;

    public void loadQuoteInfo() {

        quoteSubscriber = new Subscriber<Quote>() {
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
            public void onNext(Quote data) {
                LogUtils.i("获取数据成功");
                getMvpView().updateQuoteView(data);
                getMvpView().hideLoading();
            }
        };

        QuoteApi quoteApi = NetClient.retrofit().create(QuoteApi.class);
        quoteApi.queryQuoteInfo().map(new HttpResultFunc<Quote>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quoteSubscriber);
    }
}
