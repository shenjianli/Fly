package com.shenjianli.fly.app.engine.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shen.netclient.util.LogUtils;

public class BaiduLocation {

	private final String TAG = getClass().getSimpleName();
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	private Context mContext;
	private LocationResultListener mLocationResultListener;
	//private BaiduMap mBaiduMap;
	
	public BaiduLocation(Context context,LocationResultListener resultListener){
		this.mContext = context;
		this.mLocationResultListener = resultListener;
		//this.mBaiduMap = baiduMap;
		initLocation();
	}
	
	/**
	 * 初始化定位相关设置
	 */
	private void initLocation(){
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
	}
	
	
	public void startLocation(){
		if(null != mLocClient){
			mLocClient.start();
		}
	}
	
	public void stopLocation(){
		if(null != mLocClient){
			mLocClient.stop();
		}
	}

	public void refresh() {
		mLocClient.stop();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {


		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null){
				if(null != mLocationResultListener){
					LogUtils.i( TAG + " 定位失败！");
					mLocationResultListener.updateLocationResult(0,0,"定位失败", false);
				}
				return;
			}
			
//			MyLocationData locData = new MyLocationData.Builder()
//					.accuracy(location.getRadius())
//					// 此处设置开发者获取到的方向信息，顺时针0-360
//					.direction(100).latitude(location.getLatitude())
//					.longitude(location.getLongitude()).build();
//			
//			
//			mBaiduMap.setMyLocationData(locData);
			
			double mLat = location.getLatitude();
			double mLog = location.getLongitude();
			String addStr = location.getAddrStr();
//			if (isFirstLoc) {
//				isFirstLoc = false;
//				LatLng ll = new LatLng(location.getLatitude(),
//						location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);
//			}
			LogUtils.i( TAG + " 定位成功！");
			if(null != mLocationResultListener){
//				if(null != mLocClient){
//					mLocClient.stop();
//				}
				mLocationResultListener.updateLocationResult(location);
				LogUtils.i( "定位到的地址为：" + addStr + location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet());
				mLocationResultListener.updateLocationResult(mLat,mLog, addStr,true);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	public interface LocationResultListener{
		
		public void updateLocationResult(double lat, double log, String address, boolean isSuccess);

		public void updateLocationResult(BDLocation location);
		
	}

	public void destory() {
		mLocationResultListener = null;
		if(null != mLocClient){
			mLocClient.unRegisterLocationListener(myListener);
			myListener = null;
			mLocClient.stop();
			mLocClient = null;
		}
	}
}
