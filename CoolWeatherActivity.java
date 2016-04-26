package com.example.tantao.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tantao.coolweather.server.AutoUpdateService;
import com.example.tantao.coolweather.util.HttpCallbackListener;
import com.example.tantao.coolweather.util.HttpUtil;
import com.example.tantao.coolweather.util.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CoolWeatherActivity extends AppCompatActivity {

    @InjectView(R.id.city_nameText)
    TextView cityNameText;
    @InjectView(R.id.minText)
    TextView minText;
    @InjectView(R.id.maxText)
    TextView maxText;
    @InjectView(R.id.weatherText)
    TextView weatherText;
    @InjectView(R.id.updatetimeText)
    TextView updatetimeText;
    @InjectView(R.id.datetimeText)
    TextView datatimeText;
    @InjectView(R.id.RefreshBtn)
    Button RefreshBtn;
    @InjectView(R.id.backBtn)
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_weather);
        ButterKnife.inject(this);
        toolMether();

        String cityCode = getIntent().getStringExtra("city_code");
        if (!TextUtils.isEmpty(cityCode)) {
            Log.d("cityCode", cityCode);
            showWeatherWrit();
            queryweather(cityCode);
        }

    }

    private void toolMether() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void queryweather(String citycode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + citycode + ".html";
        queryFromServer(address, "citycode");
    }

    private void queryFromServer(String address, String type) {
        HttpUtil.sendResponseHttpURLConnection(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String request) {
                Utility.handleWeatherResponse(CoolWeatherActivity.this, request);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception err) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherWrit();
                        Toast.makeText(CoolWeatherActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void showWeatherWrit(){
        cityNameText.setText("同步中....");
        minText.setText("同步中....");
        maxText.setText("同步中....");
        weatherText.setText("同步中....");
        updatetimeText.setText("同步中....");
        datatimeText.setText("同步中....");
    }

    private void showWeather() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        minText.setText("最低温度：" + prefs.getString("temp1", ""));
        maxText.setText("最高温度：" + prefs.getString("temp2", ""));
        weatherText.setText("天气：" + prefs.getString("weather_desp", ""));
        updatetimeText.setText("更新时间：" + prefs.getString("publish_time", ""));
        datatimeText.setText("日期" + prefs.getString("current_date", ""));

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @OnClick({R.id.RefreshBtn, R.id.backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RefreshBtn:
                //RefreshBtn.setText("刷新数据中...");
                showWeatherWrit();
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
                String weathercode=preferences.getString("weather_code","");
                queryweather(weathercode);

                break;
            case R.id.backBtn:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

//    @OnClick(R.id.backBtn,R.id.RefreshBtn)
//    public void onClick() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @OnClick(R.id.RefreshBtn)
//    public void onClick() {
//    }
}
