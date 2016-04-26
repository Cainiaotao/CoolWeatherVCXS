package com.example.tantao.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tantao.coolweather.base.City;
import com.example.tantao.coolweather.base.County;
import com.example.tantao.coolweather.base.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作
 * Created by tantao on 2016/4/25.
 */
public class CoolWeatherDB {

    public static final String DBNAME="cool_weather";

    public static final int VERSION=1;

    private SQLiteDatabase database;

    private static CoolWeatherDB coolWeatherDB;

    /**
     * create SQlDataBase
     * @param context
     */
    public CoolWeatherDB(Context context){
        CoolWeatherDBHelper dbHelper=new CoolWeatherDBHelper(context,DBNAME,null,VERSION);
        database=dbHelper.getWritableDatabase();
    }

    /**
     * 异步获取实例
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null){
            coolWeatherDB=new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 保存province数据
     * @param province
     */
    public void saveProvince(Province province){
        if (province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            database.insert("Province",null,values);
        }
    }

    /**
     * 保存city数据
     * @param city
     */
    public void saveCity(City city){
        if (city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            database.insert("City",null,values);
        }
    }

    /**
     * 保存county数据
     * @param county
     */
    public void saveCounty(County county){
        if (county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCounyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            database.insert("County",null,values);
        }
    }

    /**
     * 重数据库中得到province存入list
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor=database.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /**
     * 取得数据库中得到city存入list
     * @return
     */
    public List<City> loadCitices(int provinceId){
        List<City> list=new ArrayList<City>();
        Cursor cursor=database.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToFirst()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /**
     * 获取数据库city存入list
     * @return
     */
    public List<County> loadCountice(int cityId){
        List<County> list=new ArrayList<County>();
        Cursor cursor=database.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if (cursor.moveToFirst()){
            do{
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCounyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }while (cursor.moveToNext());
        }
        return list;
    }


}
