package com.shenjianli.fly.app.engine.map.drag;


import com.shenjianli.fly.app.engine.map.ShowMapData;

public interface DragActionInterface {

	/**
	 * 根据地理地址来在地图上显示图标和名称
	 * @param mapData
	 */
	public void showMapLabelByAddressStr(ShowMapData mapData);
	
	/**
	 * 根据地图定位来显示用户的当前位置
	 */
	public void showMapLabelByLocation();

	public void startInitMap();

	public void searchNearBy(double latitude, double longitude);

	public void destoryMap();

	public void showCircleByLatAndLog(double latitude, double longitude,int radius);

	public void showCircleByInfo(double latitude, double longitude, int radius, String info);

	public void refreshMap();
	
}
