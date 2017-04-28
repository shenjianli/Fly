package com.shenjianli.fly.app.engine.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;

public class BaiduShowLabel {

	private BaiduMap mBaiduMap;
	private MapView mMapView;
	private Context mContext;
	
	//是否显示了酒店位置标签
	private boolean hasShowLabel = false;
	private float mapLevel;

	public BaiduShowLabel(MapView mapView,Context context){
		this.mMapView = mapView;
		this.mContext = context;
		mBaiduMap = mMapView.getMap();
		initMap();
	}
	
	private void initMap() {
		//普通地图  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);
	}
	
	public void showMapLabelByLatAndLog(double lat, double log,String title,float mapLevel) {

		LogUtils.i("地图显示标签位置,经度:" + lat +"纬度:"  + log);
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
		if(mapLevel != mapLevel){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
			mBaiduMap.setMapStatus(msu);
		}
		
	}
	
	
	public void showMapCenterByLatAndLog(double lat, double log,float mapLevel) {
		LogUtils.i("地图显示标签位置,经度:" + log +"纬度:"  + lat);
		LatLng point = new LatLng(lat, log);  
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.setMapStatus(status);
		if(mapLevel != mapLevel){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
			mBaiduMap.setMapStatus(msu);
		}
		
	}
	
	
	public void showMapLabelByLocation(double lat, double log,String title) {

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
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
	    mBaiduMap.setMapStatus(msu);
		
	}
	
	public void showMapLabelByAddressStr(String city,String address)
	{
		
	}

	public void destory() {
		if(null != mBaiduMap){
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
			mBaiduMap.clear();
		}
	}

	public void showMapLabelByPoiResult(PoiResult result) {
		PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
	}

	/**
	 * 在地图上显示圆形区别
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param radius 半径 （米）
     */
	public void showMapCircleByRadius(double latitude, double longitude, int radius) {
		//showMapCircleByInfoWindow(latitude, longitude, radius);
		showMapCircleByInfoWindow(latitude,longitude,radius,"");
	}

	private void showMapCircleByInfoWindow(double latitude, double longitude, int radius,String info) {
		LatLng llCircle = new LatLng(latitude, longitude);
		OverlayOptions ooCircle = new CircleOptions()
				.center(llCircle).stroke(new Stroke(5, 0x3500ff00)).fillColor(0x35ff00FF)
				.radius(radius);
		mBaiduMap.addOverlay(ooCircle);

		//构建Marker图标
//		BitmapDescriptor bitmap = BitmapDescriptorFactory
//				.fromResource(R.drawable.icbc_baidu_location_lable);
//		//构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions()
//				.position(llCircle)
//				.icon(bitmap);
//		//在地图上添加Marker，并显示
//		Marker mMarker = (Marker) mBaiduMap.addOverlay(option);

		Bitmap bgBitmap = null;
		Bitmap textBitmap = null;
		try {
			bgBitmap = drawbitmap();
			textBitmap = drawtext(bgBitmap,info);
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(textBitmap);
			OverlayOptions textOption = new MarkerOptions().position(llCircle).icon(bitmap);
			mBaiduMap.addOverlay(textOption);
		}
		finally{
			if(null != bgBitmap){
				bgBitmap.recycle();
			}
			if(null != textBitmap){
				textBitmap.recycle();
			}
			bgBitmap = null;
			textBitmap = null;
		}


//		TextView tv = new TextView(mContext);
//		tv.setTextColor(Color.WHITE);
//		tv.setBackgroundResource(R.drawable.icbc_baidu_location_label_text_bg);
//		tv.setText("签到区域");
//		tv.setGravity(Gravity.CENTER);
//		LatLng ll = mMarker.getPosition();
//		InfoWindow mInfoWindow = new InfoWindow(tv, ll, -50);
//		mBaiduMap.showInfoWindow(mInfoWindow);

		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(llCircle);
		mBaiduMap.setMapStatus(status);
//		if(mapLevel > 14.0){
//			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
//			mBaiduMap.setMapStatus(msu);
//		}
	}

//	private void showMapCircleByInfoWindow(double latitude, double longitude, int radius) {
//		LatLng llCircle = new LatLng(latitude, longitude);
//		OverlayOptions ooCircle = new CircleOptions()
//				.center(llCircle).stroke(new Stroke(5, 0x3500ff00)).fillColor(0x35ff00FF)
//				.radius(radius);
//		mBaiduMap.addOverlay(ooCircle);
//
//		//构建Marker图标
//		BitmapDescriptor bitmap = BitmapDescriptorFactory
//				.fromResource(R.drawable.icbc_baidu_location_lable);
//		//构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions()
//				.position(llCircle)
//				.icon(bitmap);
//		//在地图上添加Marker，并显示
//		Marker mMarker = (Marker) mBaiduMap.addOverlay(option);
//
//
//		TextView tv = new TextView(mContext);
//		tv.setTextColor(Color.WHITE);
//		tv.setBackgroundResource(R.drawable.icbc_baidu_location_label_text_bg);
//		tv.setText("签到区域");
//		tv.setGravity(Gravity.CENTER);
//		LatLng ll = mMarker.getPosition();
//		InfoWindow mInfoWindow = new InfoWindow(tv, ll, -50);
//		mBaiduMap.showInfoWindow(mInfoWindow);
//
//		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(llCircle);
//		mBaiduMap.setMapStatus(status);
////		if(mapLevel > 14.0){
////			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
////			mBaiduMap.setMapStatus(msu);
////		}
//	}


	private  void showMapCircle(double latitude, double longitude, int radius){
		LatLng llCircle = new LatLng(latitude, longitude);
		OverlayOptions ooCircle = new CircleOptions()
				.center(llCircle).stroke(new Stroke(5, 0x3500ff00)).fillColor(0x35ff00FF)
				.radius(radius);
		mBaiduMap.addOverlay(ooCircle);

		// 添加文字
		LatLng llText1 = new LatLng(latitude, longitude);
		OverlayOptions ooText1 = new TextOptions().bgColor(0x55FFFF00)
				.fontSize(100).fontColor(0x55FF00FF).text("签到区域")
				.position(llText1);
		mBaiduMap.addOverlay(ooText1);


		//构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icbc_baidu_location_lable);
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions()
				.position(llCircle)
				.icon(bitmap);
		//在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);


		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(llCircle);
		mBaiduMap.setMapStatus(status);
//		if(mapLevel > 14.0){
//			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mapLevel);
//			mBaiduMap.setMapStatus(msu);
//		}
	}

	private Bitmap drawtext(Bitmap bitmap3,String info) {
		int width = bitmap3.getWidth(), hight = bitmap3.getHeight();
		Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
		Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上
		Paint photoPaint = new Paint(); //建立画笔
		photoPaint.setDither(true); //获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);//过滤一些
		Rect src = new Rect(0, 0, bitmap3.getWidth(), bitmap3.getHeight());//创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
		canvas.drawBitmap(bitmap3, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
		textPaint.setTextSize(28.0f);//字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
		textPaint.setColor(Color.WHITE);//采用的颜色
		//textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
		canvas.drawText(info, 23, 32, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}
	private Bitmap drawbitmap() {
		Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icbc_baidu_location_label_text_bg);
		int width = photo.getWidth();
		int hight = photo.getHeight();
		Bitmap newb = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newb);// 初始化和方框一样大小的位图
		Paint photoPaint = new Paint(); // 建立画笔
		canvas.drawBitmap(photo, 0, 0, photoPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newb;
	}

	public void setMyLocationData(MyLocationData locationData){
		if(null != mBaiduMap){
			mBaiduMap.setMyLocationData(locationData);
		}
	}

	public void setMapLevel(float mapLevel) {
		this.mapLevel = mapLevel;
	}

	public void refresh() {
		mBaiduMap.clear();
	}

	public void showMapCircleByRadius(double latitude, double longitude, int radius, String info) {
		showMapCircleByInfoWindow(latitude,longitude,radius,info);
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
