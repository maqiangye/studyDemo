package com.b_noble.android.coolweather.activity;

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

import com.b_noble.android.coolweather.R;
import com.b_noble.android.coolweather.service.AutoUpdateWeather;
import com.b_noble.android.coolweather.util.HttpCallbackListener;
import com.b_noble.android.coolweather.util.HttpUtil;
import com.b_noble.android.coolweather.util.Utility;

import org.w3c.dom.Text;

/**
 * Created by mqy on 15/11/4.
 */
public class WeatherAcitivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    private TextView cityName;
    private Button chooseCity;
    private Button refreshWeather;
    private TextView temp1;
    private TextView temp2;
    private TextView weatherDesc;
   // private TextView pubTime;
    private TextView currentTime;
    private TextView publishText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityName = (TextView) findViewById(R.id.city_name);
        chooseCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        temp1 = (TextView) findViewById(R.id.temp1);
        temp2 = (TextView) findViewById(R.id.temp2);
        weatherDesc = (TextView) findViewById(R.id.weather_desp);
        //pubTime = (TextView) findViewById(R.id.public_text);
        currentTime = (TextView) findViewById(R.id.current_date);
        publishText = (TextView) findViewById(R.id.public_text);

        String countyCode = getIntent().getStringExtra("county_code");

        if(!TextUtils.isEmpty(countyCode)){
            Log.d("WeatherAcitivity",countyCode);
            //县，查天气
            publishText.setText("更新中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            //显示本地天气
            showWeather();
        }

        chooseCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    //查询天气代号
    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        Log.d("WeatherAcitivity",address);
        queryFromServer(address, "countyCode");
    }

    //根据天气代号查询对应的天气情况
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    //根据地址和类型去服务器上查询天气代号或是天气情况
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if(array != null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherAcitivity.this,response);
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
                publishText.setText("同步失败");
            }
        });
    }

    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(preferences.getString("cityName",""));
        temp1.setText(preferences.getString("temp1",""));
        temp2.setText(preferences.getString("temp2",""));
        weatherDesc.setText(preferences.getString("weatherDesc",""));

        publishText.setText("今天" + preferences.getString("pubTime","") + "发布");
        currentTime.setText(preferences.getString("currentDate",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);

        //启动后台更新线程
        Intent intent = new Intent(this, AutoUpdateWeather.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.switch_city :
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather :
                publishText.setText("同步中...");
                SharedPreferences sprences = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = sprences.getString("cityCode",null);
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default :
                break;
        }

    }


}
