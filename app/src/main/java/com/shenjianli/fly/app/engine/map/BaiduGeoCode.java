package com.shenjianli.fly.app.engine.map;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.shenjianli.shenlib.util.LogUtils;

public class BaiduGeoCode {
	
	private  final String TAG = getClass().getSimpleName();
	
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private GeoCodeResultListener mGeoCodeResultListener;
	
	public BaiduGeoCode(GeoCodeResultListener resultListener){
		this.mGeoCodeResultListener = resultListener;
		initSearchByAddress();
	}
	
	/**
	 * 初始化搜索模块，地理位置转化为经纬度
	 */
	private void initSearchByAddress() {
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					
					if(null != mGeoCodeResultListener){
						LogUtils.i( TAG + " 获取地理位置失败");
						mGeoCodeResultListener.updateReverseGeoCodeResult("",false);
					}
					LogUtils.i( TAG + " 抱歉，未能找到该地理位置！"
							+ result.error);
					return;
				}
				if(null != mGeoCodeResultListener){
					mGeoCodeResultListener.updateReverseGeoCodeResult(result.getAddress(),true);
				}
				if(null != mGeoCodeResultListener){
					mGeoCodeResultListener.updateReverseGeoCodeResult(result,true);
				}
			}
			
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				// TODO Auto-generated method stub
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					if(null != mGeoCodeResultListener){
						LogUtils.i( TAG + " 获取经度纬度失败");
						mGeoCodeResultListener.updateGeoCodeResult(0,0,false);
					}
					LogUtils.i( TAG + " 抱歉，未能找到经纬度！"
							+ result.error);
					return;
				}
				String strInfo = String.format("纬度：%f 经度：%f",
						result.getLocation().latitude,
						result.getLocation().longitude);
				double mLat = result.getLocation().latitude;
				double mLog = result.getLocation().longitude;
				if(null != mGeoCodeResultListener){
					LogUtils.i( TAG + "获取经度纬度成功：" + strInfo);
					mGeoCodeResultListener.updateGeoCodeResult(mLat,mLog,true);
				}
			}
		});
	}
	
	/**
	 * 开始搜索地理  通过地理位置得经纬度
	 */
	public void startSearchGeoCode(String city, String address) {
		if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(address)) {
			// Geo搜索
			mSearch.geocode(new GeoCodeOption().city(city).address(address));
			LogUtils.i( TAG + "开始搜索地址的经纬度:" + city + ":"
					+ address);

		}
		else{
			LogUtils.i(TAG + " 城市或是地址为空不能查询");
		}
	}

	/**
	 * 开始搜索地理  通过经纬度得到地理位置
	 */
	public void startSearchReverseGeoCode(double lat, double log) {
		
		LatLng ptCenter = new LatLng(lat,log);
		if(null != mSearch){
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
			.location(ptCenter));
			LogUtils.i( TAG + "开始反编码");
		}
	}

	
	public interface GeoCodeResultListener{
		
		public void updateGeoCodeResult(double lat, double log, boolean isSuccess);
		
		public void updateReverseGeoCodeResult(String address, boolean isSuccess);
		
		public void updateReverseGeoCodeResult(ReverseGeoCodeResult result, boolean isSuccess);
		
	}


	public void destory() {
		// TODO Auto-generated method stub
		if(null != mSearch){
			mSearch.destroy();
		}
	}
}
