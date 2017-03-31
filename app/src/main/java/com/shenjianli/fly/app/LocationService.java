package com.shenjianli.fly.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.app.engine.map.BaiduLocation;
import com.shenjianli.fly.app.util.CustomToast;
import com.shenjianli.fly.core.ACache;

/**
 * Created by edianzu on 2017/3/31.
 */
public class LocationService extends Service implements BaiduLocation.LocationResultListener {

    private BaiduLocation baiduLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.v("ServiceDemo onCreate");
        baiduLocation = new BaiduLocation(this,this);
        baiduLocation.startLocation();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.v("ServiceDemo onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateLocationResult(double lat, double log, String address, boolean isSuccess) {
        LogUtils.i("定位成功  经度：" + log  + "   纬度：" + lat);
    }

    private boolean isHasReminded = false;
    @Override
    public void updateLocationResult(BDLocation location) {
        if (null != location) {
            LogUtils.i("定位成功  经度：" + location.getLongitude()  + "   纬度：" + location.getLongitude() + " 地址：" + location.getAddrStr());

            String sign_lat = ACache.get(this).getAsString("sign_lat");
            String sign_log = ACache.get(this).getAsString("sign_log");

            if (!TextUtils.isEmpty(sign_lat) && !TextUtils.isEmpty(sign_log)) {
                double v = Double.parseDouble(sign_lat);
                double v1 = Double.parseDouble(sign_log);
                LatLng latLng = new LatLng(v, v1);
                LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(latLng, latLng1);
                if (0 <= distance && distance <= Constants.CIRCL_RADIUS && !isHasReminded) {
                    isHasReminded = true;
                    CustomToast.show(this, "已经进入签到区，请您签到！");
                }
            }
        }
    }
}
