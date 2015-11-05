package com.b_noble.android.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.b_noble.android.coolweather.db.CoolWeatherDB;
import com.b_noble.android.coolweather.model.City;
import com.b_noble.android.coolweather.model.County;
import com.b_noble.android.coolweather.model.Province;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mqy on 15/11/3.
 */
public class Utility {

    public static synchronized boolean handleProvincesResponse(CoolWeatherDB db,String response){
        Log.d("Utility.Provinces",response);
        if(response != null){
            String[] provinces = response.split(",");
            if(provinces != null && provinces.length >0){
                for(String province : provinces){
                    Province p = new Province();
                    String[] msgs = province.split("\\|");
                    p.setProvinceName(msgs[1]);
                    p.setProvinceCode(msgs[0]);
                    db.saveProvince(p);
                }
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean handleCitiesResponse(CoolWeatherDB db,String response,int provinceId){
        Log.d("Utility.CitiesResponse",response);

        if(response != null){
            String[] cities = response.split(",");
            if(cities != null && cities.length >0){
                for(String city : cities){
                    String[] msgs = city.split("\\|");
                    City c = new City();
                    c.setCityName(msgs[1]);
                    c.setCityCode(msgs[0]);
                    c.setProvinceId(provinceId);
                    db.saveCity(c);
                }
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean handleCountyResponse(CoolWeatherDB db,String response,int cityId){
        Log.d("Utility.CountyResponse",response);
        if(response != null){
            String[] counties = response.split(",");
            if(counties != null && counties.length >0){
                for(String county : counties){
                    String[] msgs = county.split("\\|");
                    County c = new County();
                    c.setCountyName(msgs[1]);
                    c.setCountyCode(msgs[0]);
                    c.setCityId(cityId);
                    db.addCounty(c);
                }
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context,String response){
        Log.d("Utility",response);
        try{
            /**
             * {"weatherinfo":
             *      {"city":"昆山","cityid":"101190404","temp1":"21°C","temp2":"9°C",
             *      "weather":"多云转小雨","img1":"d1.gif","img2":"n7.gif","ptime":"11:00"}
                }
             */
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String cityCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesc = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");

            saveWeatherInfo(context, cityName, cityCode, temp1, temp2,
                    weatherDesc, publishTime);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context,String cityName,String cityCode,
                                       String temp1,String temp2,String weatherDesc,String pubTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("cityName", cityName);
        editor.putString("cityCode", cityCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weatherDesc", weatherDesc);
        editor.putString("pubTime",pubTime);
        editor.putString("currentDate",format.format(new Date()));
        editor.commit();
    }

    public static void main(String[] args){
        String temp = "01|北京,02|上海,03|天津,04|重庆,05|黑龙江";
        String[] msgs = temp.split(",");
        if(msgs.length >0){
            for(String msg : msgs){
                String[] mm = msg.split("\\|");
                System.out.println(mm[0] + "," + mm[1]);
            }
        }
    }
}
