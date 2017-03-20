package com.shenjianli.fly.app.engine.presenter;


import com.shenjianli.fly.app.engine.MvpView;

/**
 * Created by toryang on 16/4/27.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();

}
