package com.shenjianli.fly.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.shenjianli.fly.R;
import com.shenjianli.fly.core.HttpMethods;
import com.shenjianli.fly.model.TestData;
import com.shenjianli.shenlib.receiver.NetBroadcastReceiver;
import com.shenjianli.shenlib.util.CustomToast;
import com.shenjianli.shenlib.util.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

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

        HttpMethods.getInstance().getTestData(new Subscriber<TestData>() {
            @Override
            public void onCompleted() {
                LogUtils.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("onError:" + e.getStackTrace());
            }

            @Override
            public void onNext(TestData testData) {
                if(null != testData){
                    flyText.setText(testData.getCity());
                }
            }
        },"001");
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
