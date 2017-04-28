package com.shenjianli.fly.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by edianzu on 2017/4/10.
 */
@Entity
public class LocationEntity {

    @Id
    private Long id;

    @Property(nameInDb = "sign_log")
    private Double log;

    @Property(nameInDb = "sign_lat")
    private Double lat;

    @Property(nameInDb = "sign_address")
    private String address;

    @Property(nameInDb = "sign_province")
    private String province;

    @Property(nameInDb = "sign_city")
    private String city;

    @Property(nameInDb = "sign_district")
    private String district;

    @Property(nameInDb = "sign_street")
    private String street;

    @Property(nameInDb = "sign_info")
    private String info;

    @Generated(hash = 2055467459)
    public LocationEntity(Long id, Double log, Double lat, String address,
            String province, String city, String district, String street,
            String info) {
        this.id = id;
        this.log = log;
        this.lat = lat;
        this.address = address;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
        this.info = info;
    }

    @Generated(hash = 1723987110)
    public LocationEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLog() {
        return this.log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
