package com.shenjianli.fly.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.shenjianli.fly.R;
import com.shenjianli.fly.test.StyleMainActivity;
import com.shenjianli.shenlib.receiver.NetBroadcastReceiver;
import com.shenjianli.shenlib.util.CustomToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements NetBroadcastReceiver.NetStateChangeListener{

    @Bind(R.id.fly_text)
    TextView flyText;
    @Bind(R.id.fly_btn)
    Button flyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NetBroadcastReceiver.addNetStateListener(this);
    }

    @OnClick(R.id.fly_btn)
    public void onClick() {
//        TestApi testApi = NetClient.retrofit().create(TestApi.class);
//        Call<Test> weather = testApi.getWeather("001");
//        weather.enqueue(new RetrofitCallback<Test>() {
//            @Override
//            public void onSuccess(Test test) {
//                if(null != test){
//                    TestData weatherinfo = test.getWeatherinfo();
//                    if(null != weatherinfo){
//                        flyText.setText(weatherinfo.getCity());
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String errorMsg) {
//
//            }
//        });

//        Intent intent = new Intent(this, WelcomeActivity.class);
//        startActivity(intent);
//         PreHomeDataManager.getPreHomeDataManager().startPreLoadDataOfHome();
        Intent intent = new Intent(this, StyleMainActivity.class);
//        intent = new Intent(this,SignInOutActivity.class);
        startActivity(intent);

        //OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Log信息拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        //设置 Debug Log 模式
//        builder.addInterceptor(loggingInterceptor);
//        final OkHttpClient okHttpClient = builder.build();
//
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                String url = "http://m.mall.icbc.com.cn/mobile/indexSlide.jhtml";
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//                try {
//                    Response response =  okHttpClient.newCall(request).execute();
//                    if(response.isSuccessful()){
//                        LogUtils.i(response.body().toString());
//                    }
//                    else {
//                        LogUtils.i("error:" + response.message());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    }

    @Override
    public void onNetChange(boolean connect) {
        if (connect) {
            CustomToast.show(this, "真好,网络正常啦！");
        } else {
            CustomToast.show(this, "糟糕，网络断开了！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // stopService(new Intent(MainActivity.this, BackgroundMonitorService.class));
    }
}
