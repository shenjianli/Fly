package com.shenjianli.fly.app.engine.map.drag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.shenjianli.fly.app.engine.map.BaiduGeoCode;
import com.shenjianli.fly.app.engine.map.BaiduLocation;
import com.shenjianli.fly.app.engine.map.BaiduShowLabel;
import com.shenjianli.fly.app.engine.map.MapResultData;
import com.shenjianli.fly.app.engine.map.ShowMapData;
import com.shenjianli.fly.app.engine.map.UpdateMapResultListener;
import com.shenjianli.shenlib.util.LogUtils;


@SuppressLint("ClickableViewAccessibility")
public class DragBaiduMapAction implements DragActionInterface,BaiduGeoCode.GeoCodeResultListener,BaiduLocation.LocationResultListener
								{

	private final String TAG = getClass().getSimpleName();
	private Context mContext;

	private BaiduShowLabel mBaiduShowLabel;
	private BaiduLocation mBaiduLocation;
	private BaiduGeoCode mBaiduGeoCode;
	private MapView mMapView;
	
	
	public DragBaiduMapAction(View mapView,Context context,UpdateMapResultListener resultListener){
		this.mContext = context;
		this.mMapView = (MapView)mapView;
		this.mUpdateMapResultListener = resultListener;
	}


	private void initBaidu() {
		mBaiduShowLabel = new BaiduShowLabel(mMapView, mContext);
		mBaiduLocation = new BaiduLocation(mContext,this);
		mBaiduGeoCode = new BaiduGeoCode(this);
	}

	
	@Override
	public void startInitMap() {
		initBaidu();
	}

	
	public void showMapLabelByLatAndLog(double lat, double log,String title) {
		if(null != mBaiduShowLabel){
			mBaiduShowLabel.showMapLabelByLatAndLog(lat, log, title,17.0f);
		}
	}

	
	private String mCity;
	private String mAddress;
	private String mShowTitle;
	private float mMapLevel;

	@Override
	public void showMapLabelByLocation() {
		if(null != mBaiduLocation){
			mBaiduLocation.startLocation();
		}
	}

	
	@Override
	public void showMapLabelByAddressStr(ShowMapData mapData) {
		if(null != mapData){
			mCity = mapData.getHotelAddrCity();
			mAddress = mapData.getHotelAddrCounty() + mapData.getHotelAddrStreet();
			mShowTitle = mapData.getHotelName();
			mMapLevel = mapData.getMapLevel();
			if(!TextUtils.isEmpty(mCity) && !TextUtils.isEmpty(mAddress)){
				if(null != mBaiduGeoCode){
					mBaiduGeoCode.startSearchGeoCode(mCity, mAddress);
				}
			}
			else{
				LogUtils.i("城市或是地址为空");
			}
		}
	}

	@Override
	public void searchNearBy(double latitude, double longitude) {
		if(null != mBaiduGeoCode){
			mBaiduGeoCode.startSearchReverseGeoCode(latitude,longitude);
		}
	}
	
	
	
	private UpdateMapResultListener mUpdateMapResultListener;
	
	@Override
	public void destoryMap() {
		if(null != mBaiduLocation)
		{
			mBaiduLocation.destory();
		}
		if(null != mBaiduGeoCode)
		{
			mBaiduGeoCode.destory();
		}
		if(null != mBaiduShowLabel){
			mBaiduShowLabel.destory();
		}
	}

	/**
	 * 更新地理编码数据  根据地理位置获取的经纬度
	 */
	@Override
	public void updateGeoCodeResult(double lat, double log, boolean isSuccess) {
		if(isSuccess){
			if(null != mBaiduShowLabel){
				mBaiduShowLabel.showMapCenterByLatAndLog(lat, log,17.0f);
				searchNearBy(lat, log);
			}
		}else{
			//Toast.makeText(mContext, "很报歉，没有找到此地理位置", Toast.LENGTH_SHORT).show();
			if(null != mUpdateMapResultListener){
				MapResultData data = new MapResultData(isSuccess);
				data.setWhat(UpdateMapResultListener.RESULT_GET_LOT_LOG_DATA_FAIL);
				data.setResult("很报歉，没有找到此地理位置");
				mUpdateMapResultListener.updateMapResult(data );
			}
		}
	}



	/**
	 * 更新反地理编码结果数据
	 */
	@Override
	public void updateReverseGeoCodeResult(String address,boolean isSuccess) {
		if(isSuccess){
			LogUtils.i("获取到的当前位置：" + address);
			if(null != mUpdateMapResultListener){
				MapResultData data = new MapResultData(isSuccess);
				data.setWhat(UpdateMapResultListener.LOCATION_ADDRESS);
				data.setResult(address);
				LogUtils.i("上报反编码的结果数据");
				mUpdateMapResultListener.updateMapResult(data);
			}
		}
		else{
			Toast.makeText(mContext, "很报歉，没有找到相关信息", Toast.LENGTH_SHORT).show();
			if(null != mUpdateMapResultListener){
				MapResultData data = new MapResultData(isSuccess);
				data.setWhat(UpdateMapResultListener.LOCATION_ADDRESS);
				data.setResult(address);
				mUpdateMapResultListener.updateMapResult(data );
			}
		}
	}


	public void updateLocationResult(double lat, double log, String address,
			boolean isSuccess) {
		if(isSuccess){
			if(null != mBaiduShowLabel){
				mBaiduShowLabel.showMapCenterByLatAndLog(lat, log, 17.0f);
			}
			searchNearBy(lat, log);
		}
		else{
			Toast.makeText(mContext, "很报歉，没有找到定位相关信息", Toast.LENGTH_SHORT).show();
		}
	}



	@Override
	public void updateReverseGeoCodeResult(ReverseGeoCodeResult result,
			boolean isSuccess) {
		if(isSuccess){
			if(null != mUpdateMapResultListener){
				MapResultData data = new MapResultData(isSuccess);
				data.setWhat(UpdateMapResultListener.RESULT_RECOMMAND_DATA);
				data.setResult(result);
				mUpdateMapResultListener.updateMapResult(data);
			}
		}
		else{
			Toast.makeText(mContext, "很报歉，没有找到定位相关信息", Toast.LENGTH_SHORT).show();
		}
	}

}
