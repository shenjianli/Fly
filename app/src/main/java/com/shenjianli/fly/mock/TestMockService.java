package com.shenjianli.fly.mock;

import com.google.gson.Gson;
import com.shenjianli.fly.model.Test;
import com.shenjianli.fly.model.entities.TestData;
import com.shenjianli.shenlib.net.HttpResult;
import com.shenjianli.shenlib.net.mock.MockService;
import com.shenjianli.shenlib.util.LogUtils;


/**
 * Created by shenjianli on 2016/7/8.
 */
public class TestMockService extends MockService {
    @Override
    public String getJsonData() {

        TestData testData = new TestData();

        testData.setCity("taiyuan");
        testData.setCityid("33333");

        //no use
        Test weatherJson = new Test();
        weatherJson.setWeatherinfo(testData);

        HttpResult<TestData> result = new HttpResult<TestData>();
        result.setStatus("1");
        result.setErrNo("1");
        result.setErrMsg("没有错误");
        result.setData(testData);

        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);
        return resultStr;
    }
}
