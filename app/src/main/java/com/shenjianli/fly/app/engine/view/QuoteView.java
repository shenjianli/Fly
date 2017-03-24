package com.shenjianli.fly.app.engine.view;


import com.shenjianli.fly.app.engine.MvpView;
import com.shenjianli.fly.model.Quote;

public interface QuoteView extends MvpView {

    void updateQuoteView(Quote data);
}
