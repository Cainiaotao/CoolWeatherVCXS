package com.example.tantao.coolweather.base;

/**
 * Created by tantao on 2016/4/25.
 */
public class County {

    /**
     * id : 1
     * counyName : zigong
     * countyCode : 11
     * cityId : 112
     */

    private int id;
    private String counyName;
    private String countyCode;
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCounyName(String counyName) {
        this.counyName = counyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getCounyName() {
        return counyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public int getCityId() {
        return cityId;
    }
}
