package com.example.tantao.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.tantao.coolweather.base.City;
import com.example.tantao.coolweather.base.Province;
import com.example.tantao.coolweather.db.CoolWeatherDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Created by tantao on 2016/4/25.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response){
        if (!TextUtils.isEmpty(response))
        {
            try{
                JSONArray jsonArray=new JSONArray(response);
                StringBuilder reader=new StringBuilder();
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String provincestr = jsonObject.getString("省");
                    JSONArray cities = jsonObject.getJSONArray("市");
                    for (int j = 0; j < cities.length(); j++) {
                        JSONObject jsonObjectCity = cities.getJSONObject(j);
                        String citycode = jsonObjectCity.getString("编码");
                        String cityname = jsonObjectCity.getString("市名");
                        // String result = provincestr + "." + city + "\t" + code + "\n";
                        City city=new City();
                        city.setCityName(cityname);
                        city.setCityCode(citycode);
                        city.setProvinceId(i);
                        coolWeatherDB.saveCity(city);
                    }
                    Province province=new Province();
                    province.setProvinceName(provincestr);
                    province.setProvinceCode(String.valueOf(i));
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context,String request){
        try{
            JSONObject jsonObject=new JSONObject(request);
            JSONObject weahterinfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weahterinfo.getString("city");
            String cityId=weahterinfo.getString("cityid");
            String temp1=weahterinfo.getString("temp1");
            String temp2=weahterinfo.getString("temp2");
            String weather=weahterinfo.getString("weather");
            String time=weahterinfo.getString("ptime");
            saveWeatherInfo(context,cityName,cityId,temp1,temp2,weather,time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void saveWeatherInfo(Context context,String cityName,String cityId,String temp1,String temp2,String weatherDesp,String publishtime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", cityId);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishtime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }

}
