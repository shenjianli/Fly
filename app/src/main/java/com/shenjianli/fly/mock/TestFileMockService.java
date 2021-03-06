package com.shenjianli.fly.mock;


import com.shen.netclient.net.MockService;
import com.shen.netclient.util.FileUtils;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.R;
import com.shenjianli.fly.app.FlyApp;

/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class TestFileMockService extends MockService {
    @Override
    public String getJsonData() {

        //直接读取文件中的json字符串
        String resultStr = FileUtils.readFromRaw(FlyApp.getAppInstance(), R.raw.test);
        //String resultStr = FileUtils.readFromAssets(App.getAppInstance(),"test.txt");
        LogUtils.i("文件中读取到的json字符串为：" + resultStr);

        //返回json字符串
        return resultStr;
    }
}
