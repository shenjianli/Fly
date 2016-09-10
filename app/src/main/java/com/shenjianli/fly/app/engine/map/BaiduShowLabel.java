package com.shenjianli.fly.app.engine.map;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.shenjianli.fly.R;

public class BaiduShowLabel {

	private BaiduMap mBaiduMap;
	private MapView mMapView;
	private Context mContext;
	
	//是否显示了酒店位置标签
	private boolean hasShowLabel = false;
	
	public BaiduShowLabel(MapView mapView,Context context){
		this.mMapView = mapView;
		this.mContext = context;
		mBaiduMap = mMapView.getMap();
		initMap();
	}
	
	private void initMap() {
		//普通地图  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
	}
	
	public void showMapLabelByLatAndLog(double lat, double log,String title,float mapLevel) {
		mBaiduMap.clear();

		//定义Maker坐标点  
		LatLng point = new LatLng(lat, log);  
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icbc_baidu_location_lable);
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
		Marker mMarker = (Marker) mBaiduMap.addOverlay(option);
		
		if(!TextUtils.isEmpty(title)){
			TextView tv = new TextView(mContext);
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundResource(R.drawable.icbc_baidu_location_label_text_bg);
			tv.setText(title);
			LatLng ll = mMarker.getPosition();
			InfoWindow mInfoWindow = new InfoWindow(tv, ll, -50);
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
		if(false == hasShowLabel){
			hasShowLabel = true;
		}
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.setMapStatus(status);
		if(mapLevel > 14.0){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
			mBaiduMap.setMapStatus(msu);
		}
		
	}
	
	
	public void showMapCenterByLatAndLog(double lat, double log,float mapLevel) {
		mBaiduMap.clear();
		LatLng point = new LatLng(lat, log);  
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.setMapStatus(status);
		if(mapLevel > 14.0){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
			mBaiduMap.setMapStatus(msu);
		}
		
	}
	
	
	public void showMapLabelByLocation(double lat, double log,String title) {
		
		mBaiduMap.clear();
		//定义Maker坐标点  
		LatLng point = new LatLng(lat, log);  
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icbc_baidu_location_lable);  
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
		Marker mMarker = (Marker) mBaiduMap.addOverlay(option);
		
		if(!TextUtils.isEmpty(title)){
			TextView tv = new TextView(mContext);
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundResource(R.drawable.icbc_baidu_location_label_text_bg);
			tv.setText(title);
			LatLng ll = mMarker.getPosition();
			InfoWindow mInfoWindow = new InfoWindow(tv, ll, -50);
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
		if(false == hasShowLabel){
			hasShowLabel = true;
		}
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.setMapStatus(status);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
	    mBaiduMap.setMapStatus(msu);
		
	}
	
	public void showMapLabelByAddressStr(String city,String address)
	{
		
	}

	public void destory() {
		// TODO Auto-generated method stub
		if(null != mBaiduMap){
			// 关闭定位图层
			//mBaiduMap.setMyLocationEnabled(false);
			mBaiduMap.clear();
		}
	}

	public void showMapLabelByPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		mBaiduMap.clear();
		PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
	}
	
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
//				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
//						.poiUid(poi.uid));
			// }
			return true;
		}
	}
	
}
