package com.shenjianli.fly.mock;

import com.google.gson.Gson;
import com.shenjianli.fly.test.Test;
import com.shenjianli.fly.test.TestData;
import com.shenjianli.shenlib.net.mock.MockService;
import com.shenjianli.shenlib.util.LogUtils;


/**
 * Created by shenjianli on 2016/7/8.
 */
public class TestMockService extends MockService {
    @Override
    public String getJsonData() {
        TestData weatherinfo = new TestData();
        weatherinfo.setCity("taiyuan");
        weatherinfo.setCityid("33333");
        Test weatherJson = new Test();
        weatherJson.setWeatherinfo(weatherinfo);
        String result =  new Gson().toJson(weatherJson);
        LogUtils.i("获得的json字符串为：" + result);
        return result;
    }
}
