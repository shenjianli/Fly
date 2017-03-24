package com.shenjianli.fly.app.engine.view;


import com.shenjianli.fly.app.engine.MvpView;
import com.shenjianli.fly.model.Test;

public interface DataView extends MvpView {

    void loadData(Test data);

}
