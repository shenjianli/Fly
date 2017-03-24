package com.shenjianli.fly.app.activity;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.engine.presenter.QuotePresenter;
import com.shenjianli.fly.app.engine.view.QuoteView;
import com.shenjianli.fly.core.ACache;
import com.shenjianli.fly.model.Quote;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuoteShowActivity extends BaseActivity implements CountTimer.CountDownTimerListener,QuoteView{

    @Bind(R.id.fly_motivational_quotes_enter_time_tv)
    TextView flyMotivationalQuotesEnterTimeTv;
    @Bind(R.id.fly_app_logo_info)
    TextView flyAppLogoInfo;
    @Bind(R.id.fly_motivational_quotes_title_tv)
    TextView flyMotivationalQuotesTitleTv;
    @Bind(R.id.fly_motivational_quotes_content_tv)
    TextView flyMotivationalQuotesContentTv;
    @Bind(R.id.fly_motivational_quotes_sum_layout)
    RelativeLayout flyMotivationalQuotesSumLayout;

    private CountTimer countTimer;
    private QuotePresenter quotePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_show);
        ButterKnife.bind(this);
        initView();

        quotePresenter = new QuotePresenter(this);
        quotePresenter.attachView(this);
        quotePresenter.loadQuoteInfo();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        Bitmap quote_bg_bitmap = ACache.get(this).getAsBitmap("quote_bg_bitmap");
        if (null != quote_bg_bitmap) {
            if (null != flyMotivationalQuotesSumLayout) {
                Drawable drawable = new BitmapDrawable(getResources(), quote_bg_bitmap);
                flyMotivationalQuotesSumLayout.setBackground(drawable);
            }
        }
        Quote quoteInfo = (Quote) ACache.get(this).getAsObject("quote_info_key");
        long countTime = 5;
        if(null != quoteInfo){
            String title = quoteInfo.getTitle();
            if(!TextUtils.isEmpty(title)){
                flyMotivationalQuotesTitleTv.setText(title);
            }
            String content = quoteInfo.getContent();
            if(!TextUtils.isEmpty(content)){
                flyMotivationalQuotesContentTv.setText(content);
            }

            String appName = quoteInfo.getAppName();
            if(!TextUtils.isEmpty(appName)){
                flyAppLogoInfo.setText(appName);
            }

            String titleColor = quoteInfo.getTitleColor();
            if(!TextUtils.isEmpty(titleColor)){
                flyMotivationalQuotesTitleTv.setTextColor(Color.parseColor(titleColor));
            }

            String contentColor = quoteInfo.getContentColor();
            if(!TextUtils.isEmpty(contentColor)){
                flyMotivationalQuotesContentTv.setTextColor(Color.parseColor(contentColor));
            }

//            String bgImgUrl = quoteInfo.getBgImgUrl();
//            if(!TextUtils.isEmpty(bgImgUrl)){
//                Drawable d = Drawable.createFromPath(bgImgUrl);
//                flyMotivationalQuotesSumLayout.setBackground(d);
//                Uri uri = Uri.parse(bgImgUrl);
//                downLoadImg(uri);
//            }

            if(countTime > 1){
                countTime = quoteInfo.getCountTime();
            }
        }
        countTimer = new CountTimer((countTime + 1) * 1000,1000);
        countTimer.setCountDownTimerListener(this);
        countTimer.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fly_motivational_quotes_enter_time_tv)
    public void onClick() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quotePresenter.detachView();
        quotePresenter = null;
        countTimer.setCountDownTimerListener(null);
        countTimer = null;

    }

    @Override
    public void updateCountDownTime(String day, String hour, String min, String second) {
        LogUtils.i("倒计时：" + day + ":" + hour + ":" + min + ":" + second);
        if(null != flyMotivationalQuotesEnterTimeTv){
            flyMotivationalQuotesEnterTimeTv.setText(second);
        }
    }

    @Override
    public void countDownTimerFinish() {
        LogUtils.i("倒计时结束" );
    }

    @Override
    public void updateQuoteView(Quote quoteInfo) {
        LogUtils.i("收到名言警句请求数据：" + quoteInfo.toString());
        ACache.get(this).put("quote_info_key",quoteInfo);
        String bgImgUrl = quoteInfo.getBgImgUrl();
        if(!TextUtils.isEmpty(bgImgUrl)){
            Uri uri = Uri.parse(bgImgUrl);
            downLoadImg(uri);
        }
    }

    @Override
    public void startLoading() {
        LogUtils.i("名言警句开始请求数据...");
    }

    @Override
    public void hideLoading() {
        LogUtils.i("名言警句结束请求数据...");
    }

    @Override
    public void showError(String msg) {
        LogUtils.i("名言警句显示错误信息" + msg);
    }


    private void downLoadImg(Uri uri) {

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                //bitmap即为下载所得图片
                if(null != bitmap){
//                    if(null != flyMotivationalQuotesSumLayout){
//                        Drawable drawable = new BitmapDrawable(QuoteShowActivity.this.getResources(),bitmap);
//                        flyMotivationalQuotesSumLayout.setBackground(drawable);
//                    }
                    ACache.get(QuoteShowActivity.this).put("quote_bg_bitmap",bitmap);

                }
                LogUtils.i("下载图片成功");
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                LogUtils.i("失败" + dataSource.getFailureCause());
            }
        }, CallerThreadExecutor.getInstance());
    }
}
