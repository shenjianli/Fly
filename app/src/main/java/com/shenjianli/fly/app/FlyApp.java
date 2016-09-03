package com.shenjianli.fly.app;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.shenjianli.fly.R;
import com.shenjianli.shenlib.Constants;
import com.shenjianli.shenlib.LibApp;
import com.shenjianli.shenlib.base.ActivityManager;
import com.shenjianli.shenlib.exception.CrashHandler;
import com.shenjianli.shenlib.util.FileUtils;
import com.shenjianli.shenlib.util.LogUtils;


/**
 * Created by shenjianli on 2016/7/14.
 */
public class FlyApp extends Application {

    private static FlyApp mMobileApp;

    private boolean isConnect;

    @Override
    public void onCreate() {
        super.onCreate();
        LibApp.getLibInstance().setMobileContext(this);
        if(FileUtils.getProperties(this, R.raw.mobile)){
            String mode = FileUtils.getPropertyValueByKey("mode");
            LogUtils.i("开发模式为：" + mode);
            if(Constants.DEV_MODE.equals(mode)){
                LibApp.getLibInstance().setLogEnable(true);
                LibApp.getLibInstance().setUrlConfigManager(R.xml.url);
            }
        }
        LibApp.getLibInstance().setBeanFactoryConfig(R.raw.bean);
        //BeanFactory.getBeanFactory().initBeanFactory(R.raw.bean);
       // BeanFactory.getBeanFactory().initBeanFactory("bean");

        if(!LogUtils.isOutPutLog){
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        mMobileApp = this;
    }

    public static FlyApp getAppInstance(){
        return  mMobileApp;
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