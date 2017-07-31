package com.shenjianli.fly.app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.DividerDecoration;
import com.shenjianli.fly.app.LocationService;
import com.shenjianli.fly.app.adapter.RecylerViewAdapter;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.receiver.NetBroadcastReceiver;
import com.shenjianli.fly.app.util.CustomToast;
import com.shenjianli.fly.app.util.DeviceUtils;
import com.shenjianli.fly.app.util.NetUtils;
import com.shenjianli.fly.app.util.ScreenUtils;
import com.shenjianli.fly.model.DemoData;
import com.shenjianli.fly.test.PreHomeDataManager;
import com.shenjianli.fly.test.StyleMainActivity;
import com.shenjianli.fly.test.TestActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements NetBroadcastReceiver.NetStateChangeListener {

    @Bind(R.id.fly_text)
    TextView flyText;
    @Bind(R.id.fly_btn)
    Button flyBtn;
    @Bind(R.id.recyclerView)
    RecyclerView flyRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout flySwipeRefreshLayout;


    RecylerViewAdapter adapter;
    List<DemoData> mDemoDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        NetBroadcastReceiver.addNetStateListener(this);


        LogUtils.i(ScreenUtils.getScreenInfo(this).toString());
        LogUtils.i(NetUtils.getWifiDeviceInfo(this));
        LogUtils.i(DeviceUtils.getPhoneInfo(this));

        initData();

        initAdapter();

        initRecylerView();

        PreHomeDataManager.getPreHomeDataManager().startPreLoadDataOfHome();

    }

    private void initAdapter() {
        adapter = new RecylerViewAdapter(this,mDemoDatas);
        adapter.setOnDemoClickListener(new RecylerViewAdapter.OnDemoClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, StyleMainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this,TestActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this,SignInOutActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, QuoteShowActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, InputMapActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, LocationActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, GreenDaoActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {

        mDemoDatas = new ArrayList<>();

        DemoData demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("使用样式相关");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("Test");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("签到");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("励志广告");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("地图选址");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("定位");
        mDemoDatas.add(demodata);

        demodata = new DemoData();
        demodata.setImgId(R.drawable.me_plan);
        demodata.setName("GreenDao");
        mDemoDatas.add(demodata);

        for (int i = 0; i < 5; i++) {
            demodata = new DemoData();
            demodata.setImgId(R.drawable.me_plan);
            demodata.setName("demo" + i);
            mDemoDatas.add(demodata);
        }
    }

    private void initRecylerView() {
        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // 设置布局管理器
        flyRecyclerView.setLayoutManager(linearLayoutManager);
        DividerDecoration decoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_single);
        decoration.setDivider(drawable);

//        decoration.getItemOffsets();
        flyRecyclerView.addItemDecoration(decoration);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        // recyclerView.addItemDecoration(new DividerDecoration(this, DividerDecoration.VERTICAL_LIST));
        flyRecyclerView.setAdapter(adapter);

        // 模拟下拉刷新
        flySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flySwipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
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
        Intent intent = new Intent(this, LocationService.class);
//        intent = new Intent(this,SignInOutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

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
//
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
