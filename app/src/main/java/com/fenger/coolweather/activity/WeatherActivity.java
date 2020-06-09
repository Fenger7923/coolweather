package com.fenger.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fenger.coolweather.R;
import com.fenger.coolweather.service.AutoUpdateService;
import com.fenger.coolweather.util.HttpCallBackListener;
import com.fenger.coolweather.util.HttpUtil;
import com.fenger.coolweather.util.Utility;

/**
 * com.fenger.coolweather.activity
 * Created by fenger
 * in 2020/6/2
 */
public class WeatherActivity extends Activity {

    private LinearLayout weatherInfoLayout;

    private TextView cityNameText;//城市名称
    private TextView publishText;//发布时间
    private TextView weatherDespText;//天气描述信息
    private TextView temp1Text;//发布时间
    private TextView temp2Text;//最高气温
    private TextView currentDateText;//当前时间
    private Button switchCity;//切换城市
    private Button refreshWeather;//更新天气

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = findViewById(R.id.weather_info_layout);
        cityNameText = findViewById(R.id.city_name);
        publishText = findViewById(R.id.publish_text);
        weatherDespText = findViewById(R.id.weather_desp);
        temp1Text = findViewById(R.id.temp1);
        temp2Text = findViewById(R.id.temp2);
        currentDateText = findViewById(R.id.current_date);
        switchCity = findViewById(R.id.switch_city);
        refreshWeather = findViewById(R.id.refresh_weather);

        String countryCode = getIntent().getStringExtra("country_code");
        if (!TextUtils.isEmpty(countryCode)){
            publishText.setText("同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        }else{
            showWeather();
        }

        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,ChooseActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishText.setText("更新中。。。");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = preferences.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });
    }

    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name",""));
        temp1Text.setText(preferences.getString("temp1",""));
        temp2Text.setText(preferences.getString("temp2",""));
        weatherDespText.setText(preferences.getString("weather_desp",""));
        publishText.setText("今天"+preferences.getString("publish_time","")+"发布");
        currentDateText.setText(preferences.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void queryWeatherCode(String countryCode) {
        String address =  "http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
        queryFromServer(address,"countryCode");
    }

    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    private void queryFromServer(String address, final String type) {
        Log.d("fenger", "queryFromServer: "+address+"   "+type);
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if ("countryCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        if ("countryCode".equals(type)){
                            if(!TextUtils.isEmpty(response)){
                                String[] array = response.split("\\|");
                                if (array != null && array.length == 2){
                                    String weatherCode = array[1];
                                    queryWeatherInfo(weatherCode);
                                }
                            }
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }
}
