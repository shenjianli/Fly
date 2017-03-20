package com.shenjianli.fly.app.engine;


import com.shenjianli.fly.model.Test;

public interface DataView extends MvpView {

    void loadData(Test data);
}
