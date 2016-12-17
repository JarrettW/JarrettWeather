package com.jarrettw.jarrettweather.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jarrettw.jarrettweather.db.JarrettWeatherDB;
import com.jarrettw.jarrettweather.model.City;
import com.jarrettw.jarrettweather.model.District;
import com.jarrettw.jarrettweather.model.Province;

//遍历省市县数据的活动
public class ChooseAreaActivity extends Activity{
	
	private static final int LEVEL_PROVINCE = 0;
	
	private static final int LEVEL_CITY = 1;
	
	private static final int ELVEL_DISTRICT = 2;
	
	private ProgressDialog progressDialog;
	
	private TextView titleText;
	
	private ListView listView;
	
	private List<String> lists;
	
	private ArrayAdapter<String> adapter;
	
	private JarrettWeatherDB jarrettWeatherDB;
	
	//省列表
	private List<Province> provinceList;
	//市列表
	private List<City>	cityList;
	//县列表
	private List<District> districtList;
	//选中的省份
	private Province selectedProvince;
	//选中的城市
	private City selectedCity;
	//当前选中的级别
	private int currentLevel;
//	是否从WeatherActivity跳转过来
	private boolean isFromWeatherActivity;
	
	@Override
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//已经选择了城市且不是从WeatherActivity跳转过来，才会直接跳转到WeatherActivity
		if(preferences.getBoolean("city_selected", false) && !isFromWeatherActivity){
			Intent intent = new Intent(this, WeatherActivity.class);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
