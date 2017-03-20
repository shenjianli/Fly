package com.shenjianli.fly.api;

import com.shenjianli.fly.model.SlideRes;

import retrofit2.Call;
import retrofit2.http.GET;


public interface HomeService {

	@GET("/mobile/indexSlide.jhtml")
	Call<SlideRes> getSlideData();
}
