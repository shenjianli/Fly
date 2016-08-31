package com.shenjianli.fly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.shenjianli.fly.test.Test;
import com.shenjianli.fly.test.TestApi;
import com.shenjianli.fly.test.TestData;
import com.shenjianli.shenlib.net.NetClient;
import com.shenjianli.shenlib.net.RetrofitCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fly_text)
    TextView flyText;
    @Bind(R.id.fly_btn)
    Button flyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fly_btn)
    public void onClick() {
        TestApi testApi = NetClient.retrofit().create(TestApi.class);
        Call<Test> weather = testApi.getWeather("001");
        weather.enqueue(new RetrofitCallback<Test>() {
            @Override
            public void onSuccess(Test test) {
                if(null != test){
                    TestData weatherinfo = test.getWeatherinfo();
                    if(null != weatherinfo){
                        flyText.setText(weatherinfo.getCity());
                    }
                }
            }

            @Override
            public void onFail(String errorMsg) {

            }
        });
    }
}
