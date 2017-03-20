package com.shenjianli.fly.test;


import com.shen.netclient.NetClient;
import com.shen.netclient.engine.RetrofitCallback;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.api.HomeService;
import com.shenjianli.fly.model.SlideRes;

import retrofit2.Call;


public class PreHomeDataManager {

	private Call<SlideRes> slideData;


	private SlideRes mSlideRes;


	private PreHomeDataManager() {

	}

	private static PreHomeDataManager mPreHomeDataManager = new PreHomeDataManager();

	public static synchronized PreHomeDataManager getPreHomeDataManager() {
		if (null == mPreHomeDataManager) {
			mPreHomeDataManager = new PreHomeDataManager();
		}
		return mPreHomeDataManager;
	}

	public void startPreLoadDataOfHome() {
		getHomeData();
	}

	HomeService homeService;
	RetrofitCallback<SlideRes> callback;
	private void getHomeData() {
		
		LogUtils.i("开始预加载首页数据");
		if(null == homeService){
			homeService = NetClient.retrofit().create(HomeService.class);
		}

		if(null == callback){
			callback = new RetrofitCallback<SlideRes>(){

				@Override
				public void onSuccess(SlideRes slideRes) {
					LogUtils.i(" 预加载---轮播图----请求成功" +  slideRes.toString());
				}

				@Override
				public void onFail(String errorMsg) {
					LogUtils.i(" 预加载---轮播图----请求失败:" + errorMsg);
				}
			};
		}
		slideData = homeService.getSlideData();
		slideData.enqueue(callback);
	}

}
