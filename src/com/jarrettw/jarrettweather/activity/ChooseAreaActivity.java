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

//����ʡ�������ݵĻ
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
	
	//ʡ�б�
	private List<Province> provinceList;
	//���б�
	private List<City>	cityList;
	//���б�
	private List<District> districtList;
	//ѡ�е�ʡ��
	private Province selectedProvince;
	//ѡ�еĳ���
	private City selectedCity;
	//��ǰѡ�еļ���
	private int currentLevel;
//	�Ƿ��WeatherActivity��ת����
	private boolean isFromWeatherActivity;
	
	@Override
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//�Ѿ�ѡ���˳����Ҳ��Ǵ�WeatherActivity��ת�������Ż�ֱ����ת��WeatherActivity
		if(preferences.getBoolean("city_selected", false) && !isFromWeatherActivity){
			Intent intent = new Intent(this, WeatherActivity.class);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
