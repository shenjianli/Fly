package com.shenjianli.fly.app.util;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class NetUtils {

	public static boolean checkNet(Context context) {
		boolean isMobile = checkConnection(context,ConnectivityManager.TYPE_MOBILE);
		boolean isWifi = checkConnection(context,ConnectivityManager.TYPE_WIFI);
		
		
		if(isMobile) {
			//readAPN(context);
		}

		if (!isMobile && !isWifi) {
			return false;
		}

		return true;
	}

//	private static void readAPN(Context context) {
//		ContentResolver resolver = context.getContentResolver();
//		Uri PREFERRED_APN_URI = Uri.parse("content://content://telephony/carriers/preferapn");
//		Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
//
//		if(cursor != null && cursor.isFirst()) {
//			Constants.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
//			Constants.PORT = cursor.getInt(cursor.getColumnIndex("port"));
//		}
//	}

	private static boolean checkConnection(Context context,int netType) {
		ConnectivityManager cm = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getNetworkInfo(netType);
		boolean isConnected = networkInfo.isConnected();

		return isConnected;
	}

	public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

	public static String getWifiDeviceInfo(Context context){
		StringBuilder wifiDeviceId = new StringBuilder();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		wifiDeviceId.append("SSID=");
		wifiDeviceId.append(wifiInfo.getSSID());
		wifiDeviceId.append("，MacAddress=");
		wifiDeviceId.append(wifiInfo.getMacAddress());
		wifiDeviceId.append("，HiddenSSID=");
		wifiDeviceId.append(wifiInfo.getHiddenSSID());
		wifiDeviceId.append("，NetworkId=");
		wifiDeviceId.append(wifiInfo.getNetworkId());
		wifiDeviceId.append("，Rssi=");
		wifiDeviceId.append(wifiInfo.getRssi());
		wifiDeviceId.append("，BSSID=");
		wifiDeviceId.append(wifiInfo.getBSSID());
		return wifiDeviceId.toString();
	}

	/**
	 * 获取网络类型
	 * @return
     */
	public static String GetNetworkType(Activity activity)
	{
		String strNetworkType = "";

		NetworkInfo networkInfo = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE).getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
		{
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				strNetworkType = "WIFI";
			}
			else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				String _strSubTypeName = networkInfo.getSubtypeName();

				Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
						{
							strNetworkType = "3G";
						}
						else
						{
							strNetworkType = _strSubTypeName;
						}

						break;
				}

				Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
			}
		}

		Log.e("cocos2d-x", "Network Type : " + strNetworkType);

		return strNetworkType;
	}
}
