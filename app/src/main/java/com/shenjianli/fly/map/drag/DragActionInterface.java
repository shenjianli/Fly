package com.shenjianli.fly.map.drag;


import com.shenjianli.fly.map.ShowMapData;

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
	
}
