package com.shenjianli.fly.app.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.Constants;
import com.shenjianli.fly.app.FlyApp;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.db.LocationEntityDao;
import com.shenjianli.fly.app.engine.map.MapResultData;
import com.shenjianli.fly.app.engine.map.UpdateMapResultListener;
import com.shenjianli.fly.app.engine.map.drag.DragActionInterface;
import com.shenjianli.fly.app.engine.map.drag.DragBaiduMapAction;
import com.shenjianli.fly.app.util.CustomToast;
import com.shenjianli.fly.core.ACache;
import com.shenjianli.fly.model.LocationEntity;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 演示覆盖物的用法
 */
public class LocationActivity extends BaseActivity implements
        UpdateMapResultListener {

    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.map_location_sinin_btn)
    Button mapLocationSininBtn;

    @Bind(R.id.map_location_refresh_btn)
    Button mapLocationRefreshBtn;
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private ImageView mBackImg;
    private TextView mTitleTv;

    private final int mDelayTime = 800;
    private final int mShowHotelLabelReqCode = 1000;


    private double currentLat;
    private double currentLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParamData();
        initData();
        initMap();
        initReceiver();
    }

    private void initData() {
        locEntityDao = FlyApp.getAppInstance().getDaoSession().getLocationEntityDao();
    }

    /**
     * 初始化参数
     */
    private void initParamData() {

    }

    /**
     * 初始化地图相关组件
     */
    private void initMap() {

        setContentView(R.layout.activity_map_location);
        ButterKnife.bind(this);
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.location_map);

//		mBackImg = (ImageView) findViewById(R.id.map_title_back_img);
//		mBackImg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});

//		mTitleTv = (TextView) findViewById(R.id.map_title_text);
//		mTitleTv.setText(R.string.receiving_address_title);

        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);

        mDragMapActionInterface = new DragBaiduMapAction(mMapView, this, this);

        if (null != mDragMapActionInterface) {
            mDragMapActionInterface.startInitMap();
            showSiginCircleInMap();
        }
    }

    /**
     * 显示签到圆形区域
     */
    private void showSiginCircleInMap() {

        Long inputIndex = (Long) ACache.get(this).getAsObject("input_index");
        if(null != inputIndex){
            List<LocationEntity> locationEntities = locEntityDao.loadAll();
            if(null != locationEntities && locationEntities.size() > 0 ){
                for (LocationEntity locationEntity:locationEntities) {
                    String info = locationEntity.getInfo();
                    if(TextUtils.isEmpty(info)){
                        info = "提醒区域";
                    }else {
                        info += "区域";
                    }
                    mDragMapActionInterface.showCircleByInfo(locationEntity.getLat(), locationEntity.getLog(), Constants.CIRCL_RADIUS,info);
                }
            }
        }
    }

    private SDKReceiver mReceiver;

    /**
     * 初始化广播接收
     */
    private void initReceiver() {
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    private boolean isHasSigin = false;

    @OnClick({R.id.map_location_sinin_btn, R.id.map_location_refresh_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_location_sinin_btn:
                signInAction();
                break;
            case R.id.map_location_refresh_btn:
                mDragMapActionInterface.refreshMap();
                showSiginCircleInMap();
                mDragMapActionInterface.showMapLabelByLocation();
                break;
        }
    }

    private void signInAction() {


        Long inputIndex = (Long) ACache.get(this).getAsObject("input_index");

        if(null != inputIndex){
            LocationEntity signLocEntity = null;
            double minSignDistance = -1;
            List<LocationEntity> locationEntities = locEntityDao.loadAll();
            if(null != locationEntities && locationEntities.size() > 0 ){
                LatLng currentLoc = new LatLng(currentLat, currentLog);
                for (LocationEntity locationEntity:locationEntities) {
                    LatLng iputLoc = new LatLng(locationEntity.getLat(), locationEntity.getLog());
                    double distance = DistanceUtil.getDistance(currentLoc, iputLoc);

                    if (distance > Constants.CIRCL_RADIUS) {
                        LogUtils.i("");
                    } else if (distance >= 0) {
                        if( -1 == minSignDistance || minSignDistance > distance){
                            minSignDistance = distance;
                            signLocEntity = locationEntity;
                        }
                    }

                }
            }

            if(null != signLocEntity){
                CustomToast.show(this, "进行提醒成功  " + signLocEntity.getAddress());
                isHasSigin = true;
            }
            else {
                CustomToast.show(this, "未到提醒地点，请到提醒地点进行操作");
            }
        }
        else {
            CustomToast.show(this, "请录入提醒地点");
        }
    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            LogUtils.d(TAG, "action: " + s);
            // TextView text = (TextView) findViewById(R.id.text_Info);
            // text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(LocationActivity.this, "网络出错",
                        Toast.LENGTH_SHORT).show();
                LogUtils.d(TAG + "key 验证出错! ");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                LogUtils.d(TAG + "网络出错");
                Toast.makeText(LocationActivity.this, "网络出错", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    // 是否显示位置标签
    private boolean hasShowLabel = false;

    @Override
    protected void onResume() {
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (false == hasShowLabel) {
            if (null != mSearchHandler) {
                mSearchHandler.sendEmptyMessageDelayed(mShowHotelLabelReqCode,
                        mDelayTime);
            }
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        if (null != mDragMapActionInterface) {
            mDragMapActionInterface.destoryMap();
        }
        mMapView.onDestroy();
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }


    private DragActionInterface mDragMapActionInterface;
    private DragSearchHandler mSearchHandler = new DragSearchHandler(this);

    private static class DragSearchHandler extends Handler {

        WeakReference<LocationActivity> mActivityReference;

        DragSearchHandler(LocationActivity acitivity) {
            mActivityReference = new WeakReference<LocationActivity>(acitivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final LocationActivity activity = mActivityReference.get();
            if (null != mActivityReference) {
                if (msg.what == activity.mShowHotelLabelReqCode) {
                    if (null != activity.mDragMapActionInterface) {
                        activity.mDragMapActionInterface
                                .showMapLabelByLocation();
                    } else {
                        LogUtils.i("mDragMapActionInterface 操作对象为空");
                    }
                }
            }
        }
    }


    /**
     * 更新地图结果数据
     *
     * @param data
     */
    @Override
    public void updateMapResult(MapResultData data) {

        if (null != data) {
            boolean success = data.isSuccess();
            if (success) {

            } else {
                switch (data.getWhat()) {
                    case RESULT_GET_LOT_LOG_DATA_FAIL:
                        showLocationByAddressFail((String) data.getResult());
                        break;
                    default:
                        break;
                }
                LogUtils.i("收到数据为异常");
            }
        } else {
            LogUtils.i("收到数据为空");
        }
    }

    LocationEntityDao locEntityDao;

    /**
     * 实时更新用户的定位信息
     *
     * @param location 百度地图返回的定位信息对象
     */
    @Override
    public void updateLocationResult(BDLocation location) {
        if (null != location) {
            LogUtils.i("定位成功" + "   纬度：" + location.getLatitude() + "  经度：" + location.getLongitude());
//            String sign_lat = ACache.get(this).getAsString("sign_lat");
//            String sign_log = ACache.get(this).getAsString("sign_log");
//            if (!TextUtils.isEmpty(sign_lat) && !TextUtils.isEmpty(sign_log)) {
//                double v = Double.parseDouble(sign_lat);
//                double v1 = Double.parseDouble(sign_log);
//                LatLng latLng = new LatLng(v, v1);
//                LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                double distance = DistanceUtil.getDistance(latLng, latLng1);
//                if (0 <= distance && distance <= Constants.CIRCL_RADIUS && !isHasSigin) {
//                    CustomToast.show(this, "已经进入签到区，请您签到！");
//                    showNotificationInfo();
//                }
//            }
            currentLat = location.getLatitude();
            currentLog = location.getLongitude();

            Long inputIndex = (Long) ACache.get(this).getAsObject("input_index");
            if (null != inputIndex) {
                List<LocationEntity> locationEntities = locEntityDao.loadAll();
                if (null != locationEntities && locationEntities.size() > 0) {
                    for (LocationEntity locationEntity : locationEntities) {
                        LatLng latLng = new LatLng(locationEntity.getLat(), locationEntity.getLog());
                        LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                        double distance = DistanceUtil.getDistance(latLng, latLng1);
                        if (0 <= distance && distance <= Constants.CIRCL_RADIUS && !isHasSigin) {
                            CustomToast.show(this, "已经进入提醒区，请注意啦！");
                            showNotificationInfo();
                        }
                    }
                }
            }
        }
    }

    /**
     * 到达签到区域，发送通知提示签到
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationInfo() {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
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
                .setVibrate(new long[]{0, 3000, 3500, 7000, 1000, 3000})//延迟0ms，然后振动3000ms，在延迟3500ms，接着在振动7000ms。
                .build();
        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;//用户单击通知后自动消失; //三色灯提醒，在使用三色灯提醒时候必须加该标志符,发起Notification后，铃声和震动均只执行一次;

        NotificationManager manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manger.notify(0, notification);
    }

    /**
     * 定位失败的提示信息
     *
     * @param result 提示信息内容
     */
    private void showLocationByAddressFail(String result) {
        String msgTitle = "";
        String msgContent = "";
        if (TextUtils.isEmpty(msgTitle)) {
            msgTitle = "温馨提示 ：";
        }
        if (TextUtils.isEmpty(result)) {
            msgContent = "地址百度定位失败";
        } else {
            msgContent = result;
        }
        Builder builder = new Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(msgTitle).setMessage(msgContent).setCancelable(false)
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("定位", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != mDragMapActionInterface) {
                            mDragMapActionInterface.showMapLabelByLocation();
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
