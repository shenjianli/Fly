package com.shenjianli.fly.model.entities;

import java.io.Serializable;

/**
 * Created by edianzu on 2016/8/31.
 *
 * 实现Serializable接口，以支持序列化，这样这个对象可以使用Intent来进行传递
 *
 */
public class TestData implements Serializable{


    private String city;
    private String cityid;

    public void setCity(String city) {
        this.city = city;
    }
    public void setCityid(String cityid) {
        this.cityid = cityid;
    }


    public String getCity() {
        return city;
    }

    public String getCityid() {
        return cityid;
    }
}
