package com.jarrettw.jarrettweather.activity;

import java.io.InputStream;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jarrettw.jarrettweather.R;
import com.jarrettw.jarrettweather.service.AutoUpdateService;
import com.jarrettw.jarrettweather.utility.HttpCallbackListener;
import com.jarrettw.jarrettweather.utility.HttpUtil;
import com.jarrettw.jarrettweather.utility.LogUtil;
import com.jarrettw.jarrettweather.utility.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener{
	//������ʾ������
	private TextView cityNameText;
	//������ʾ����������Ϣ
	private TextView weatherDespText;
	//������ʾ����
	private TextView temperature;
	//������ʾ��ǰ����
	private TextView currentDateText;
	//�л����а�ť
	private Button switchCity;
	//����������ť
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//��ʼ�����ؼ�
		cityNameText = (TextView)findViewById(R.id.city_name);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		temperature = (TextView)findViewById(R.id.temperature);
		currentDateText = (TextView)findViewById(R.id.current_date);
		switchCity = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		String districtName = getIntent().getStringExtra("district_name");
		LogUtil.log("WeatherActivity", "district_name = " + districtName);
		if(!TextUtils.isEmpty(districtName)){
			//���ؼ�����ʱ��ȥ��ѯ����
			weatherDespText.setText("ͬ����...");
			queryWeather(districtName);
		}else{
			//û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}
	}
	
	//��ѯ����
	private void queryWeather(String name){
		try{
			//ʹ�þۺ�����
			String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + URLEncoder.encode(name, "UTF-8") + "&key=77f1a93df176f0db8c1910b6bfe51e90";
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
				
				@Override
				public void onFinish(InputStream in){
					Utility.handleWeatherRespose(WeatherActivity.this, in);
					runOnUiThread(new Runnable(){
						@Override
						public void run(){
							showWeather();//��ʾ����
						}
					});
				}
				
				@Override
				public void onError(Exception e){
					runOnUiThread(new Runnable(){
						@Override
						public void run(){
							weatherDespText.setText("ͬ��ʧ��");
						}
					});
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		LogUtil.log("WeatherActivity", "cityName = " + prefs.getString("city_name", ""));
		LogUtil.log("WeatherActivity", "temperature = " + prefs.getString("temperature", ""));
		LogUtil.log("WeatherActivity", "weather = " + prefs.getString("weather", ""));
		LogUtil.log("WeatherActivity", "date = " + prefs.getString("date", ""));
		
		cityNameText.setText(prefs.getString("city_name", ""));
		weatherDespText.setText(prefs.getString("weather", ""));
		temperature.setText(prefs.getString("temperature", ""));
		currentDateText.setText(prefs.getString("date", ""));
		
		Intent intent = new Intent(this, AutoUpdateService.class);  //��̨�Զ����·���
		startService(intent);
	}
	
	
	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			weatherDespText.setText("ͬ����");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String districtName = preferences.getString("district_name", "");
			queryWeather(districtName);
			break;
		default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
}
