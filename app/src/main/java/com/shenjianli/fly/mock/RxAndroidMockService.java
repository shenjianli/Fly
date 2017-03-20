package com.shenjianli.fly.mock;

import com.google.gson.Gson;
import com.shen.netclient.net.MockService;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.model.HttpResult;
import com.shenjianli.fly.model.Test;
import com.shenjianli.fly.model.entities.TestData;


/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class RxAndroidMockService extends MockService {
    @Override
    public String getJsonData() {

        TestData testData = new TestData();

        testData.setCity("taiyuan");
        testData.setCityid("33333");

        //no use
        Test weatherJson = new Test();
        weatherJson.setTestData(testData);

        HttpResult<Test> result = new HttpResult<Test>();
        result.setReqCode(HttpResult.REQ_SUCC);
        result.setMsg("没有错误");
        result.setData(weatherJson);

        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);

        //返回json字符串
        return resultStr;
    }
}
