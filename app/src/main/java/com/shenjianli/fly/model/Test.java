package com.shenjianli.fly.model;

import com.shenjianli.fly.model.entities.TestData;

import java.io.Serializable;

/**
 * Created by edianzu on 2016/8/31.
 *
 * 实现Serializable接口，以支持序列化，这样这个对象可以使用Intent来进行传递
 *
 */
public class Test implements Serializable{

    private TestData testData;

    public TestData getTestData() {
        return testData;
    }

    public void setTestData(TestData testData) {
        this.testData = testData;
    }
}
