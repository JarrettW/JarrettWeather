package com.jarrettw.jarrettweather.service;

import java.io.InputStream;
import java.net.URLEncoder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.jarrettw.jarrettweather.receiver.AutoUpdateReceiver;
import com.jarrettw.jarrettweather.utility.HttpCallbackListener;
import com.jarrettw.jarrettweather.utility.HttpUtil;
import com.jarrettw.jarrettweather.utility.Utility;

public class AutoUpdateService extends Service{

	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		new Thread(new Runnable(){
			@Override
			public void run(){
				updateWeather();//更新天气
			}
		}).start();
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		int threeHour = 3 * 60 * 60 * 1000; //这是3消失的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + threeHour;
		Intent intent1 = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	//更新天气
	private void updateWeather(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String name = preferences.getString("county_name", "");
		try{
			String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + URLEncoder.encode(name, "UTF-8") + "&key77f1a93df176f0db8c1910b6bfe51e90";
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
				@Override
				public void onFinish(InputStream in){
					Utility.handleWeatherRespose(AutoUpdateService.this, in);
				}
				
				@Override
				public void onError(Exception e){
					e.printStackTrace();
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
