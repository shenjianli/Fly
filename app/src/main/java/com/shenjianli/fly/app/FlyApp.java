package com.shenjianli.fly.app;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shen.netclient.NetClient;
import com.shen.netclient.engine.NetClientLib;
import com.shen.netclient.util.FileUtils;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.BuildConfig;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.db.DaoMaster;
import com.shenjianli.fly.app.db.DaoSession;

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

        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());

        NetClient.addNetworkInterceptor(new StethoInterceptor());

        NetClientLib.getLibInstance().setMobileContext(this);

        initByGradleFile();

        initGreenDao();
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


    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private void initGreenDao() {

        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "fly-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
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

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
