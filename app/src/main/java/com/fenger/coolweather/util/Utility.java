package com.fenger.coolweather.util;

import android.text.TextUtils;

import com.fenger.coolweather.db.CoolWearherDB;
import com.fenger.coolweather.model.City;
import com.fenger.coolweather.model.Country;
import com.fenger.coolweather.model.Province;

/**
 * com.fenger.coolweather.util
 * Created by fenger
 * in 2020/5/10
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWearherDB coolWearherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p :allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //存储到province表
                    coolWearherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleCitiesResponse(CoolWearherDB coolWearherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0){
                for(String p :allCities){
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //存储到city表
                    coolWearherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleCountriesResponse(CoolWearherDB coolWearherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCountries = response.split(",");
            if(allCountries != null && allCountries.length > 0){
                for(String p :allCountries){
                    String[] array = p.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    //存储到Country表
                    coolWearherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }
}
