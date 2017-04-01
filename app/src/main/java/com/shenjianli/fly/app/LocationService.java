package com.shenjianli.fly.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.activity.MainActivity;
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
                if (0 <= distance && distance <= Constants.CIRCL_RADIUS /*&& !isHasReminded*/) {
                    isHasReminded = true;
                    CustomToast.show(this, "已经进入签到区，请您签到！");
                    showNotificationInfo();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationInfo(){

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification= new Notification.Builder(this)
                .setContentTitle("签到提醒")
                .setContentText("已经进入签到区，请您签到！")
                .setTicker("签到通知来啦") //通知首次出现在通知栏，带上升动画效果的
                //.setNumber(number) //设置通知集合的数量
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                //.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.icon_gold)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_gold))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {0,3000,3500,7000,1000,3000})//延迟0ms，然后振动3000ms，在延迟3500ms，接着在振动7000ms。
                .build();
        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE |Notification.FLAG_AUTO_CANCEL ;//用户单击通知后自动消失; //三色灯提醒，在使用三色灯提醒时候必须加该标志符,发起Notification后，铃声和震动均只执行一次;

        NotificationManager manger= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manger.notify(0, notification);
    }
}
