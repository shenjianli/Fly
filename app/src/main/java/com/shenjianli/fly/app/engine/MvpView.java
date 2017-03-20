package com.shenjianli.fly.app.engine;

public interface MvpView {
    void startLoading();
    void hideLoading();
    void showError(String msg);
}