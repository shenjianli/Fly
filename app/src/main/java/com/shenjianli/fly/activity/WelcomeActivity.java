package com.shenjianli.fly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.shenjianli.fly.MainActivity;
import com.shenjianli.fly.R;
import com.shenjianli.fly.engine.guide.GuideActivity;
import com.shenjianli.shenlib.base.BaseActivity;
import com.shenjianli.shenlib.util.LogUtils;
import com.shenjianli.shenlib.util.SharedPreUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.iv_welcome_bg)
    ImageView ivWelcomeBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        showAnim();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void showAnim() {
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        ivWelcomeBg.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isShowGuidePage = SharedPreUtil.get(
                        WelcomeActivity.this, "showGuidePage", true);

                String fileName = SharedPreUtil.get(WelcomeActivity.this, "splash_file_name", "");

                if (isShowGuidePage) {
                    //跳转到引导页面，且显示广告页面
                    if (!TextUtils.isEmpty(fileName)) {
                        LogUtils.i("跳转到引导页面，且显示广告页面");
                        goToGuideActivity(true);
                    } else {
                        //跳转到引导页面，不显示广告页面
                        LogUtils.i("跳转到引导页面，不显示广告页面");
                        goToGuideActivity(false);
                    }
                } else {
                    //进入广告页面
                    if (!TextUtils.isEmpty(fileName)) {
                        LogUtils.i("进入广告页面");
                        goToAdActivity();
                    } else {
                        //进入主界面
                        LogUtils.i("进入主界面");
                        goToMainActivity();
                    }
                }
            }

        });
    }

    /**
     * 跳转到广告页面
     */
    private void goToAdActivity() {
        Intent intent = new Intent(WelcomeActivity.this, AdActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 跳转到主界面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 跳转到引导页面
     * @param showAd 表示是否显示广告页面
     */
    private void goToGuideActivity(boolean showAd) {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra("showAd", showAd);
        startActivity(intent);
        this.finish();
    }

}
