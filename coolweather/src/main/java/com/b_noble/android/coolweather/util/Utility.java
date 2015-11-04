package com.b_noble.android.coolweather.util;

import com.b_noble.android.coolweather.db.CoolWeatherDB;
import com.b_noble.android.coolweather.model.City;
import com.b_noble.android.coolweather.model.County;
import com.b_noble.android.coolweather.model.Province;

/**
 * Created by mqy on 15/11/3.
 */
public class Utility {

    public static synchronized boolean handleProvincesResponse(CoolWeatherDB db,String response){
        if(response != null){
            String[] provinces = response.split(",");
            if(provinces != null && provinces.length >0){
                for(String province : provinces){
                    Province p = new Province();
                    String[] msgs = province.split("|");
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
        if(response != null){
            String[] cities = response.split(",");
            if(cities != null && cities.length >0){
                for(String city : cities){
                    String[] msgs = city.split("|");
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
        if(response != null){
            String[] counties = response.split(",");
            if(counties != null && counties.length >0){
                for(String county : counties){
                    String[] msgs = county.split("|");
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

}
