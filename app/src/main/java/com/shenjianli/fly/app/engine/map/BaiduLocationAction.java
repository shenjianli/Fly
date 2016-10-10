package com.shenjianli.fly.app.engine.map;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by edianzu on 2016/9/30.
 */
public class BaiduLocationAction implements BaiduLocation.LocationResultListener {

    private BaiduLocation mBaiduLocation;
    private UpdateMapResultListener mUpdateMapResultListener;
    private Context mContext;

    @Override
    public void updateLocationResult(double lat, double log, String address, boolean isSuccess) {
        if(isSuccess){
            if(null != mUpdateMapResultListener){
                MapResultData mapResultData = new MapResultData(isSuccess);
                mapResultData.setResult(address);
                mUpdateMapResultListener.updateMapResult(mapResultData);
            }
        }
        else{
            Toast.makeText(mContext, "很报歉，没有找到定位相关信息", Toast.LENGTH_SHORT).show();
        }
    }

    public BaiduLocationAction(Context context, UpdateMapResultListener resultListener){
        this.mContext = context;
        this.mUpdateMapResultListener = resultListener;
        initBaidu();
    }

    private void initBaidu() {
        mBaiduLocation = new BaiduLocation(mContext,this);
        if(null != mBaiduLocation){
            mBaiduLocation.startLocation();
        }
    }


}
