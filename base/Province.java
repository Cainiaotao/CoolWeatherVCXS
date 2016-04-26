package com.example.tantao.coolweather.base;

/**
 * Created by tantao on 2016/4/25.
 */
public class Province {

    /**
     * Id : 1
     * provinceName : sichuan
     * provinceCode : ss
     */

    private int id;
    private String provinceName;
    private String provinceCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }
}
