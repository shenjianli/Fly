package com.shenjianli.fly.app.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shenjianli.shenlib.util.LogUtils;

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
		option.setScanSpan(1000);
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
			if(null != mLocationResultListener){
				LogUtils.i( TAG + " 定位成功！");
				if(null != mLocClient){
					mLocClient.stop();
				}
				LogUtils.i( "定位到的地址为：" + addStr + location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet());
				mLocationResultListener.updateLocationResult(mLat,mLog, addStr,true);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	public interface LocationResultListener{
		
		public void updateLocationResult(double lat, double log, String address, boolean isSuccess);
		
	}

	public void destory() {
		// TODO Auto-generated method stub
		if(null != mLocClient){
			mLocClient.stop();
			mLocClient.unRegisterLocationListener(myListener);
		}
	}
}
