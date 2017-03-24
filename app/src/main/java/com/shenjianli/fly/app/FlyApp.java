package com.shenjianli.fly.app;



import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shen.netclient.NetClient;
import com.shen.netclient.engine.NetClientLib;
import com.shen.netclient.util.FileUtils;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.BuildConfig;
import com.shenjianli.fly.R;

/**
 * Created by shenjianli on 2016/7/14.
 */
public class FlyApp extends Application {

    private static FlyApp mMobileApp;

    private boolean isConnect;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        NetClient.addNetworkInterceptor(new StethoInterceptor());

        NetClientLib.getLibInstance().setMobileContext(this);

        initByGradleFile();

        NetClientLib.getLibInstance().setBeanFactoryConfig(R.raw.bean);
        //BeanFactory.getBeanFactory().initBeanFactory(R.raw.bean);
       // BeanFactory.getBeanFactory().initBeanFactory("bean");

        if(!LogUtils.isOutPutLog){
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        Fresco.initialize(this);
        mMobileApp = this;
    }

    public static FlyApp getAppInstance(){
        return  mMobileApp;
    }


    /*
   根据主项目中的gradle配置文件开初始化不同的开发模式
    */
    private void initByGradleFile() {

        if(Constants.TEST_MODE.equals(BuildConfig.MODE)){
            NetClientLib.getLibInstance().setLogEnable(true);
            NetClientLib.getLibInstance().setUrlConfigManager(R.xml.url);
        }
        else if(Constants.DEV_MODE.equals(BuildConfig.MODE))
        {
            NetClientLib.getLibInstance().setLogEnable(true);
            NetClientLib.getLibInstance().setServerBaseUrl(BuildConfig.SERVER_URL);
        }
        else if(Constants.RELEASE_MODE.equals(BuildConfig.MODE)){
            NetClientLib.getLibInstance().setLogEnable(false);
            NetClientLib.getLibInstance().setServerBaseUrl(BuildConfig.SERVER_URL);
        }
    }


    /*
    根据Raw中的mobile配置文件来初始化开发模式
     */
    private void initByRawConfigFile() {
        if(FileUtils.getProperties(this, R.raw.mobile)){
            String mode = FileUtils.getPropertyValueByKey("mode");
            String baseUrl = FileUtils.getPropertyValueByKey("baseUrl");
            LogUtils.i("开发模式为：" + mode);
            if(Constants.TEST_MODE.equals(mode)){
                NetClientLib.getLibInstance().setLogEnable(true);
                NetClientLib.getLibInstance().setUrlConfigManager(R.xml.url);
            }
            else if(Constants.DEV_MODE.equals(BuildConfig.MODE))
            {
                NetClientLib.getLibInstance().setLogEnable(true);
                NetClientLib.getLibInstance().setServerBaseUrl(baseUrl);
            }
            else if(Constants.RELEASE_MODE.equals(BuildConfig.MODE)){
                NetClientLib.getLibInstance().setLogEnable(false);
                NetClientLib.getLibInstance().setServerBaseUrl(baseUrl);
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setConnect(boolean b) {
        isConnect = b;
    }

    public boolean isConnect() {
        return isConnect;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前客户端版本信息
     */
    public String getCurrentVersion(){
        String versionName = "mobileVersion_";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName += info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
            versionName += "1.0.0";
        }
        versionName += "_";
        return versionName;
    }


    public void exit(){
        ActivityManager.getInstance().appExit(this);
    }
}
