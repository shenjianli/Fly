package com.shenjianli.fly.app.map;

import android.text.TextUtils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.shenjianli.shenlib.util.LogUtils;

import java.util.List;

public class BaiduPoiSearch {
	
	private  final String TAG = getClass().getSimpleName();
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	PoiSearch mPoiSearch  = null; // 搜索模块，也可去掉地图模块独立使用

	private SearchNearByResultListener mSearchNearByResultListener;
	
	public BaiduPoiSearch(SearchNearByResultListener resultListener,MapView mapView){
		this.mMapView = mapView;
		this.mSearchNearByResultListener = resultListener;
		initPoiSearch();
	}
	
	
	private List<PoiInfo> mResultInfos; 
	/**
	 * 初始化搜索模块，地理位置转化为经纬度
	 */
	private void initPoiSearch() {
		
		mBaiduMap = mMapView.getMap();
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
			
		    public void onGetPoiResult(PoiResult result){  
		    	//获取POI检索结果  
		    	if (result.error != SearchResult.ERRORNO.NO_ERROR) {
		    		LogUtils.i( TAG + " 获取POI检索结果失败");
		    		if(null != mSearchNearByResultListener){
			    		mSearchNearByResultListener.updateSearchNearByResult(result, false);
			    	}
					return;
		    	}
		    	else{
		    		LogUtils.i( TAG + " 获取POI检索结果成功  ");
			    	mResultInfos = result.getAllPoi();
			    	if(null != mSearchNearByResultListener){
			    		mSearchNearByResultListener.updateSearchNearByResult(result, true);
			    	}
					PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
					mBaiduMap.setOnMarkerClickListener(overlay);
					overlay.setData(result);
					overlay.addToMap();
					overlay.zoomToSpan();
					
					return ;
		    	}
		    	
		    }  
		    public void onGetPoiDetailResult(PoiDetailResult result){  
				// 获取Place详情页检索结果
				if (result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 详情检索失败
					// result.error请参考SearchResult.ERRORNO
					LogUtils.i( TAG + " 详情检索失败  ");
				} else {
					// 检索成功
					LogUtils.i( TAG + " 详情检索成功 ");
					String address = result.getAddress();
					String name = result.getName();
					String phone = result.getTelephone();
					LogUtils.i( TAG + " 名称：" + name + " 电话：" + phone + " 地址：" + address);
				}

		    }  
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}
	
	/**
	 * 开始搜索地理  通过地理位置得经纬度
	 */
	public void startSearchNearBy(double lat,double log,String keyword) {
			if(!TextUtils.isEmpty(keyword)){
				LatLng latLng = new LatLng(lat,log);
				PoiNearbySearchOption option = new PoiNearbySearchOption();
				option.location(latLng).radius(10000).keyword(keyword);
				mPoiSearch.searchNearby(option);
				LogUtils.i( TAG + " 开始在 经度：" + log + "纬度：" + lat + "关键字：" + keyword);
			}
			else{
				LogUtils.i( TAG + " 搜索的关键字为空");
			}
	}


	public void searchDetailInfo(String uid){
		//uid是POI检索中获取的POI ID信息
		mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(uid));
	}
	
	public interface SearchNearByResultListener{
		
		public void updateSearchNearByResult(PoiResult result, boolean isSuccess);
		
	}


	public void destory() {
		if(null != mPoiSearch){
			mPoiSearch.destroy();
		}
	}
	
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			LogUtils.i( TAG + " 点击的uid:" + poi.uid);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}
}
