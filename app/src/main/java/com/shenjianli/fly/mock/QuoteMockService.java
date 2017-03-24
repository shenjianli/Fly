package com.shenjianli.fly.mock;

import com.google.gson.Gson;
import com.shen.netclient.net.MockService;
import com.shen.netclient.util.LogUtils;
import com.shenjianli.fly.model.HttpResult;
import com.shenjianli.fly.model.Quote;


/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class QuoteMockService extends MockService {
    @Override
    public String getJsonData() {

        Quote testData = new Quote();

        testData.setTitle("撑起头顶的天");
        testData.setContent("我要养成这样的习惯： 我喜欢，驾驭着代码在风驰电掣中创造完美！我喜欢，操纵着代码在随心所欲中体验生活！我喜欢，书写着代码在时代浪潮中完成经典！每一段新的代码在我手中诞生对我来说就像观看刹那花开的感动！");
        testData.setAppName("程序员");
        testData.setAppImgUrl("");
        testData.setBgImgUrl("http://pic.qiantucdn.com/58pic/25/63/65/08b58PICt2I_1024.jpg");
        testData.setContentColor("#00ffff");
        testData.setTitleColor("#f0000f");
        testData.setCountTime(10);

        HttpResult<Quote> result = new HttpResult<Quote>();

        result.setReqCode(HttpResult.REQ_SUCC);
        result.setMsg("没有错误");
        result.setData(testData);

        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);
        //返回json字符串
        return resultStr;
    }
}
