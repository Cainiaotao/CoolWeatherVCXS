package com.example.tantao.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tantao.coolweather.base.City;
import com.example.tantao.coolweather.base.Province;
import com.example.tantao.coolweather.db.CoolWeatherDB;
import com.example.tantao.coolweather.util.HttpCallbackListener;
import com.example.tantao.coolweather.util.HttpUtil;
import com.example.tantao.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.provincelistview)
    ListView provincelistview;


    private static final int LEVE_PROVINCE = 0;
    private static final int LEVE_CITIES = 1;
    private static final int LEVE_COUNTIES = 2;
    @InjectView(R.id.backBtn)
    Button backBtn;

    private List<Province> provinceList;
    private List<City> cityList;

    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private int currentLevel;
    private Province selectprovince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        drawerMetherd();
        customAdapter();

    }

    private void drawerMetherd() {

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void customAdapter() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        provincelistview.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        //queryProvince();
        provincelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVE_PROVINCE) {
                    selectprovince = provinceList.get(position);
                    queryCities();
                }
                else {
                    String citycode=cityList.get(position).getCityCode();
                    Intent intent=new Intent(MainActivity.this,CoolWeatherActivity.class);
                    intent.putExtra("city_code",citycode);
                    startActivity(intent);
                    finish();
                }
            }
        });

        queryProvince();

    }

    private void queryProvince() {
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            provincelistview.setSelection(0);
            //titleText.setText("中国");
            currentLevel = LEVE_PROVINCE;
        } else {
            queryFromServer("province");
        }
    }

    private void queryCities() {
        cityList = coolWeatherDB.loadCitices(selectprovince.getId()-1);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            provincelistview.setSelection(0);
            //titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVE_CITIES;
        } else {
            queryFromServer("city");
        }
    }

    private void queryFromServer(final String type) {
        Log.d("queryFormServer", "Start");
        String address = "http://10.10.1.131:8080/weatherdata.json";
        showProgressDialog();
        Log.d("queryFormServer", "StartHttp");
        HttpUtil.sendResponseHttpURLConnection(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String request) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(coolWeatherDB, request);
                } else if ("city".equals(type)) {
                    result = Utility.handleProvincesResponse(coolWeatherDB, request);
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Log.d("queryFormServer", "is ok");
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCities();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception err) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private ProgressDialog progressDialog;

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @OnClick(R.id.backBtn)
    public void onClick() {
        if (currentLevel == LEVE_CITIES) {
            queryProvince();
        } else {
            finish();
        }
    }
}
