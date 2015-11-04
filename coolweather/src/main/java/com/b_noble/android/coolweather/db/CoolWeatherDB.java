package com.b_noble.android.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.b_noble.android.coolweather.model.City;
import com.b_noble.android.coolweather.model.County;
import com.b_noble.android.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mqy on 15/11/2.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "cool_weather";

    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //存储province
    public void saveProvince(Province province){
        if(province != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            db.insert("Province",null,contentValues);
        }
    }

    //加载全部省
    public List<Province> loadProvinces(){
        List<Province> ps = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province pro = new Province();
                pro.setId(cursor.getInt(cursor.getColumnIndex("id")));
                pro.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                pro.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                ps.add(pro);
            }while(cursor.moveToNext());
        }
        return ps;
    }

    //保存city
    public void saveCity(City city){
        if(city != null){
            ContentValues cv = new ContentValues();
            cv.put("city_name",city.getCityName());
            cv.put("city_code",city.getCityCode());
            cv.put("province_id",city.getProvinceId());

            db.insert("City",null,cv);
        }
    }

    //获取省市的城市
    public List<City> loadCities(int provinceid){
        List<City> cts = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceid)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(provinceid);
                cts.add(city);
            }while (cursor.moveToNext());
        }
        return cts;
    }

    //保存区县信息
    public void addCounty(County county){
        if(county!= null){
            ContentValues cv = new ContentValues();
            cv.put("county_name",county.getCountyName());
            cv.put("county_code",county.getCountyCode());
            cv.put("city_id",county.getCityId());
            db.insert("County",null,cv);
        }
    }

    //取城市下面的区县信息
    public List<County> loadCounty(int cityId){
        List<County> counties = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id = ?",new String[]{},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                counties.add(county);
            }while(cursor.moveToNext());
        }
        return counties;
    }

}
