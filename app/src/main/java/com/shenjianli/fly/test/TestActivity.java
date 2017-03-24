package com.shenjianli.fly.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.shen.netclient.NetClient;
import com.shen.netclient.engine.RetrofitCallback;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.api.TestApi;
import com.shenjianli.fly.app.engine.view.DataView;
import com.shenjianli.fly.app.engine.presenter.TestPresenter;
import com.shenjianli.fly.model.Test;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class TestActivity extends AppCompatActivity implements DataView {

    @Bind(R.id.city_tv)
    TextView mCityTv;
    @Bind(R.id.get_btn)
    Button mGetBtn;

    private TestPresenter testPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        testPresenter = new TestPresenter(this);
        testPresenter.attachView(this);
    }

    @OnClick(R.id.get_btn)
    public void onClick() {
        //loadTestData();
        loadDataByRxAndroid();
    }

    private void loadTestData() {
        //创建网络请求结口
        TestApi api = NetClient.retrofit().create(TestApi.class);
        //根据网络请求结口获取数据
        Call<Test> data = api.getTestData("001");
        //启动网络请求去请求数据
        data.enqueue(new RetrofitCallback<Test>() {
            @Override
            public void onSuccess(Test data) {
                //成功时返回测试数据
                if(null != data){
                    mCityTv.setText(data.getTestData().getCity());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                //失败时返回错误信息
                LogUtils.i(" 错误信息 " + errorMsg);
            }
        });
    }


    private void loadDataByRxAndroid(){
        testPresenter.loadDataByRxAndroid();
    }
    private void loadDataByMvp(){
        testPresenter.loadData();
    }

    @Override
    public void loadData(Test data) {
        //成功时返回测试数据
        if(null != data){
            mCityTv.setText(data.getTestData().getCity());
        }
    }

    @Override
    public void startLoading() {
        LogUtils.i("开始loading...");
    }

    @Override
    public void hideLoading() {
        LogUtils.i("结束loading...");
    }

    @Override
    public void showError(String msg) {
        //失败时返回错误信息
        LogUtils.i(" 错误信息 " + msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testPresenter.detachView();
    }
}
