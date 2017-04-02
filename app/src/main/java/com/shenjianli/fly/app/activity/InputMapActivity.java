package com.shenjianli.fly.app.activity;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.Constants;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.engine.map.MapResultData;
import com.shenjianli.fly.app.engine.map.ShowMapData;
import com.shenjianli.fly.app.engine.map.UpdateMapResultListener;
import com.shenjianli.fly.app.engine.map.drag.ConsigneeAddress;
import com.shenjianli.fly.app.engine.map.drag.ContentAdapter;
import com.shenjianli.fly.app.engine.map.drag.DragActionInterface;
import com.shenjianli.fly.app.engine.map.drag.DragBaiduMapAction;
import com.shenjianli.fly.core.ACache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 演示覆盖物的用法
 */
public class InputMapActivity extends BaseActivity implements
        UpdateMapResultListener {

    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.map_location_write_btn)
    Button mapLocationWriteBtn;
    @Bind(R.id.map_location_tv)
    TextView mapLocationTv;
    @Bind(R.id.map_location_refresh_btn)
    Button mapLocationRefreshBtn;
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private ImageView mBackImg;
    private TextView mTitleTv;

    private ListView mContentListView;
    private ContentAdapter adapter;

    private final int mDelayTime = 800;
    private final int mShowHotelLabelReqCode = 1000;
    private final int BAI_DU_RESULT_CODE = 1548;


    private double currentLat;
    private double currentLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParamData();
        initMap();
        initReceiver();
    }

    private String mHotelStreet = "";
    private String mCityStr = "";
    private String mAreaStr = "";
    private String mHotelName = "";
    private String mChoice = "";
    private ShowMapData mMapData;

    private void initParamData() {

        Intent intent = getIntent();

        mCityStr = intent.getStringExtra("city");
        mHotelStreet = intent.getStringExtra("street");
        mAreaStr = intent.getStringExtra("area");
        mHotelName = intent.getStringExtra("name");
        mChoice = intent.getStringExtra("choice");

        mMapData = new ShowMapData();
        mMapData.setHotelAddrCity(mCityStr);
        mMapData.setHotelAddrCounty(mAreaStr);
        mMapData.setHotelAddrStreet(mHotelStreet);
        mMapData.setHotelName(mHotelName);
        mMapData.setMapLevel(16.0f);

    }

    /**
     * 初始化地图相关组件
     */
    private void initMap() {

        setContentView(R.layout.activity_map_address);
        ButterKnife.bind(this);
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

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

        mContentListView = (ListView) findViewById(R.id.content_listview);
        mContentListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (null != mAddressComponent) {
                    ConsigneeAddress address = new ConsigneeAddress();
                    address.setConsigneeProvince(mAddressComponent.province);
                    address.setConsigneeCity(mAddressComponent.city);
                    address.setConsigneeCounty(mAddressComponent.district);

                    PoiInfo poiInfo = mContent.get(position);

                    adapter.setSelectPos(position);

                    currentLog = poiInfo.location.longitude;
                    currentLat = poiInfo.location.latitude;

                    LogUtils.i("点击的地址的经度：" + poiInfo.location.longitude + "纬度：" + poiInfo.location.latitude);

                    String detailAddress = poiInfo.address;
                    if (!TextUtils.isEmpty(detailAddress)) {

                        detailAddress = detailAddress.replace(
                                mAddressComponent.province, "");
                        detailAddress = detailAddress.replace(
                                mAddressComponent.city, "");
                        detailAddress = detailAddress.replace(
                                mAddressComponent.district, "");

                        if (!TextUtils.isEmpty(mAddressComponent.street)) {
                            if (!detailAddress.contains("路") && !detailAddress.contains("街") && !detailAddress.contains("道") && !detailAddress.contains(mAddressComponent.street)) {
                                detailAddress = mAddressComponent.street
                                        + detailAddress;
                            }
                        }
                        if (!TextUtils.isEmpty(poiInfo.name)) {
                            if (!detailAddress.contains(poiInfo.name)) {
                                detailAddress = detailAddress + poiInfo.name;
                            }
                        }
                        address.setConsigneeDetailAddress(detailAddress);
                        try {
                            //String result = JSONObject.toJSONString(address);
                            String result = new Gson().toJson(address);
                            if (!TextUtils.isEmpty(result)) {
                                LogUtils.i("点击的地址为：" + result);
//                                Intent data = new Intent();
//                                data.putExtra("result", result);
//                                setResult(BAI_DU_RESULT_CODE, data);
//                                finish();
                            }

                        } catch (Exception e) {
                            LogUtils.i("出现异常");
                        }
                    } else {
                        LogUtils.i("详细地址为空");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        adapter = new ContentAdapter(this, mContent);

        TextView textView = (TextView) findViewById(R.id.no_data_map_layout);
        mContentListView.setEmptyView(textView);
        mContentListView.setAdapter(adapter);

        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                LogUtils.i("onMapStatusChangeStart 经度：" + arg0.target.longitude + "纬度"
                        + arg0.target.latitude);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                LogUtils.i("onMapStatusChangeFinish 经度：" + arg0.target.longitude + "纬度"
                        + arg0.target.latitude);

                currentLat = arg0.target.latitude;
                currentLog = arg0.target.longitude;


                LatLng latLng = new LatLng(40.053275, 116.294587);
                double distance = DistanceUtil.getDistance(latLng, arg0.target);
                LogUtils.i("到定位的距离：" + distance + "米");
                if (null != mDragMapActionInterface) {
                    isLocationByAddress = false;
                    mDragMapActionInterface.searchNearBy(arg0.target.latitude,
                            arg0.target.longitude);
                }
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

        mDragMapActionInterface = new DragBaiduMapAction(mMapView, this, this);
        if (null != mDragMapActionInterface) {
            mDragMapActionInterface.startInitMap();
            showSiginCircleInMap();
        }
    }

    private void showSiginCircleInMap() {
        String sign_lat = ACache.get(this).getAsString("sign_lat");
        String sign_log = ACache.get(this).getAsString("sign_log");
        if (!TextUtils.isEmpty(sign_lat) && !TextUtils.isEmpty(sign_log)) {
            currentLat = Double.parseDouble(sign_lat);
            currentLog = Double.parseDouble(sign_log);
            mDragMapActionInterface.showCircleByLatAndLog(currentLat, currentLog, Constants.CIRCL_RADIUS);
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

    @OnClick({R.id.map_location_write_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_location_write_btn:

                ACache.get(this).put("sign_lat", String.valueOf(currentLat));
                ACache.get(this).put("sign_log", String.valueOf(currentLog));
                mDragMapActionInterface.showCircleByLatAndLog(currentLat, currentLog, Constants.CIRCL_RADIUS);

                break;
        }
    }

    @OnClick(R.id.map_location_refresh_btn)
    public void onClick() {
        mDragMapActionInterface.refreshMap();
        showSiginCircleInMap();
        mDragMapActionInterface.showMapLabelByLocation();
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
                Toast.makeText(InputMapActivity.this, "key 验证出错! ",
                        Toast.LENGTH_SHORT).show();
                LogUtils.d(TAG + "key 验证出错! ");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                LogUtils.d(TAG + "网络出错");
                Toast.makeText(InputMapActivity.this, "网络出错", Toast.LENGTH_SHORT)
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

    // 是否显示了酒店位置标签
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

    private boolean isLocationByAddress = false;
    private DragActionInterface mDragMapActionInterface;
    private DragSearchHandler mSearchHandler = new DragSearchHandler(this);

    private static class DragSearchHandler extends Handler {

        WeakReference<InputMapActivity> mActivityReference;

        DragSearchHandler(InputMapActivity acitivity) {
            mActivityReference = new WeakReference<InputMapActivity>(acitivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final InputMapActivity activity = mActivityReference.get();
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


    @Override
    public void updateMapResult(MapResultData data) {

        if (null != data) {
            boolean success = data.isSuccess();
            if (success) {
                switch (data.getWhat()) {
                    case RESULT_RECOMMAND_DATA:
                        updateReverseGeoCodeResult((ReverseGeoCodeResult) data
                                .getResult());
                        break;
                    default:
                        break;
                }
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

    @Override
    public void updateLocationResult(BDLocation location) {

    }


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
                            isLocationByAddress = false;
                            mDragMapActionInterface.showMapLabelByLocation();
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private List<PoiInfo> mContent = new ArrayList<PoiInfo>();
    private AddressComponent mAddressComponent;

    public void updateReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (null != result) {
            mContent.clear();
            adapter.setSelectPos(0);
            List<PoiInfo> content = result.getPoiList();
            mAddressComponent = result.getAddressDetail();
            LogUtils.i("-------------------"
                    + mAddressComponent.province + ":" + mAddressComponent.city
                    + ":" + mAddressComponent.district + ":"
                    + mAddressComponent.street);
            LogUtils.i("地址为：" + result.getAddress());
            if (null != content && content.size() > 0) {
                mContent.addAll(result.getPoiList());

            } else {
                if (mContent.size() <= 0) {
                    PoiInfo poiInfo = new PoiInfo();
                    if (null != mAddressComponent) {
//						if(isLocationByAddress){
//							poiInfo.address = mCityStr + mAreaStr;
//							poiInfo.name = mHotelStreet;
//							isLocationByAddress = false;
//						}
//						else 
                        {
                            String address = getFirstDetailAddress(mAddressComponent);
                            poiInfo.address = address;
                            if (!TextUtils.isEmpty(mAddressComponent.street)) {
                                poiInfo.name = mAddressComponent.street;
                                if (!TextUtils
                                        .isEmpty(mAddressComponent.streetNumber)) {
                                    poiInfo.name += mAddressComponent.streetNumber;
                                }
                            }
                        }
                        mContent.add(poiInfo);
                    }
                }
                LogUtils.i("附近没有数据");
            }
            adapter.notifyDataSetChanged();
        }
    }

    private String getFirstDetailAddress(AddressComponent mAddressComponent) {

        StringBuffer result = new StringBuffer();
        if (!TextUtils.isEmpty(mAddressComponent.province)) {
            result.append(mAddressComponent.province);
        }
        if (!TextUtils.isEmpty(mAddressComponent.city)
                && !result.toString().contains(mAddressComponent.city)) {
            result.append(mAddressComponent.city);
        }
        if (!TextUtils.isEmpty(mAddressComponent.district)) {
            result.append(mAddressComponent.district);
        }
        return result.toString();
    }
}
