package com.shenjianli.shenlib.net;

import com.shenjianli.shenlib.Constants;
import com.shenjianli.shenlib.LibApp;
import com.shenjianli.shenlib.cache.CacheInterceptor;
import com.shenjianli.shenlib.net.cookie.PersistentCookieStore;
import com.shenjianli.shenlib.net.cookie.WebkitCookieManagerProxy;
import com.shenjianli.shenlib.net.interceptor.HeaderInterceptor;
import com.shenjianli.shenlib.net.interceptor.QueryParameterInterceptor;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class NetClient {

	private static Retrofit retrofit = null;
    public static Retrofit retrofit() {
        if (retrofit == null) {
	         OkHttpClient okHttpClient = new OkHttpClient();
            /**
             *设置缓存
             */
	         File cacheFile = new File(LibApp.getLibInstance().getMobileContext().getExternalCacheDir(), "MallCache");
	         Cache cache = new Cache(cacheFile, Constants.CACHE_SIZE);
            okHttpClient.setCache(cache);
            CacheInterceptor noNetcache = new CacheInterceptor();//无网络拦截器
            okHttpClient.interceptors().add(noNetcache);//无网络
            okHttpClient.networkInterceptors().add(noNetcache);//有网络
            /**
             *  公共参数，代码略
             */
             
            //公共参数
            okHttpClient.interceptors().add(new QueryParameterInterceptor());
            /**
             * 设置头，代码略
             */           
            //设置头
            okHttpClient.interceptors().add(new HeaderInterceptor());
			 /**
             * Log信息拦截器
              *
             */
//             if (LogUtils.isOutPutLog) {
//            	    // Log信息拦截器
//            	    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            	    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            	    //设置 Debug Log 模式
//                    okHttpClient.interceptors().add(loggingInterceptor);
//            	}

            /**
             * 设置MockService
             */
            //okHttpClient.interceptors().add(new MockServerInterceptor());

			  /**
             * 设置cookie，代码略
             */
             //CookieManager cookieManager = new CookieManager();
             //cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
             //builder.cookieJar(new JavaNetCookieJar(cookieManager));

            PersistentCookieStore persistentCookieStore = new PersistentCookieStore(LibApp.getLibInstance().getMobileContext());
            WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(persistentCookieStore, java.net.CookiePolicy.ACCEPT_ALL);
            java.net.CookieHandler.setDefault(coreCookieManager);
            okHttpClient.setCookieHandler(coreCookieManager);



            /**
            * 设置超时和重连，代码略
            */
            /**
             * 设置超时和重连，代码略
             */
            //设置超时
            okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            //错误重连
            okHttpClient.setRetryOnConnectionFailure(true);

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_BASE_URL)
                    .client(okHttpClient)
            //      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }


    /* 可以通过 setLevel 改变日志级别
 共包含四个级别：NONE、BASIC、HEADER、BODY

NONE 不记录

BASIC 请求/响应行
--> POST /greeting HTTP/1.1 (3-byte body)
<-- HTTP/1.1 200 OK (22ms, 6-byte body)

HEADER 请求/响应行 + 头

--> Host: example.com
Content-Type: plain/text
Content-Length: 3

<-- HTTP/1.1 200 OK (22ms)
Content-Type: plain/text
Content-Length: 6

BODY 请求/响应行 + 头 + 体
*/
}
