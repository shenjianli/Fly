package com.shenjianli.fly.app.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.engine.map.BaiduLocationAction;
import com.shenjianli.fly.app.engine.map.MapResultData;
import com.shenjianli.fly.app.engine.map.UpdateMapResultListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by edianzu on 2016/9/30.
 */
public class SignInOutActivity extends BaseActivity implements UpdateMapResultListener {

    @Bind(R.id.project_name)
    TextView projectName;
    @Bind(R.id.location_address)
    TextView locationAddress;
    @Bind(R.id.employee_work_time)
    TextView employeeWorkTime;

    private BaiduLocationAction baiduLocationAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_in_out);
        ButterKnife.bind(this);
        baiduLocationAction = new BaiduLocationAction(this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void updateMapResult(MapResultData data) {
        if(null != data){
            boolean success = data.isSuccess();
            if(success){
                String  address = (String) data.getResult();
                if(null != locationAddress){
                    locationAddress.setText(address);
                }
            }
        }
    }

    @Override
    public void updateLocationResult(BDLocation location) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != baiduLocationAction){
            baiduLocationAction.destory();
        }
    }
}
