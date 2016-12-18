package com.jarrettw.jarrettweather.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrettw.jarrettweather.R;
import com.jarrettw.jarrettweather.db.JarrettWeatherDB;
import com.jarrettw.jarrettweather.model.City;
import com.jarrettw.jarrettweather.model.District;
import com.jarrettw.jarrettweather.model.Province;
import com.jarrettw.jarrettweather.utility.HttpCallbackListener;
import com.jarrettw.jarrettweather.utility.HttpUtil;
import com.jarrettw.jarrettweather.utility.MyApplication;
import com.jarrettw.jarrettweather.utility.Utility;

//����ʡ�������ݵĻ
public class ChooseAreaActivity extends Activity{
	
	private static final int LEVEL_PROVINCE = 0;
	
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_DISTRICT = 2;
	
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
			startActivity(intent);
			finish();
			return;
		}
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		lists = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists);
		jarrettWeatherDB = JarrettWeatherDB.getInstance(this);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(i);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(i);
					queryDistricts();
				}else if(currentLevel == LEVEL_DISTRICT){
					String districtName = districtList.get(i).getDistrictName();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("district_name", districtName);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();//����ʡ������
	}
	
	//��ѯȫ������ʡ�����ݣ����ȴ����ݿ��ѯ�����û�е���������ѯ
	private void queryProvinces(){
		provinceList = jarrettWeatherDB.loadProvinces();
		if(provinceList.size() > 0){
			lists.clear();
			for(Province province : provinceList){
				lists.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer("Province");
		}
	}
	
	
	//��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û���ٴӷ�������ѯ
	private void queryCities(){
		cityList = jarrettWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			lists.clear();
			for(City city : cityList){
				lists.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer("City");
		}
	}
	
	//��ѯѡ�����������أ����ȴ����ݿ��ѯ�����û���ٴӷ�������ѯ
	private void queryDistricts(){
		districtList = jarrettWeatherDB.loadDistricts(selectedCity.getId());
		if(districtList.size() > 0){
			lists.clear();
			for(District district : districtList){
				lists.add(district.getDistrictName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_DISTRICT;
		}else{
			queryFromServer("District");
		}
	}
	
	//���ݴ�������ʹӷ������ϲ�ѯʡ��������
	private void queryFromServer(final String type){
		showProgressDialog();
		HttpUtil.sendHttpRequest("http://v.juhe.cn/weather/citys?key=77f1a93df176f0db8c1910b6bfe51e90", new HttpCallbackListener(){
			@Override
			public void onFinish(InputStream in){
				boolean result = Utility.handleResponse(JarrettWeatherDB.getInstance(MyApplication.getContext()), in);
				if(result){
					//ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable(){
						@Override
						public void run(){
							closeProgressDialog();
							if(type.equals("Province")){
								queryProvinces();
							}else if(type.equals("City")){
								queryCities();
							}else if(type.equals("District")){
								queryDistricts();
							}
						}
					});
				}
			}
			@Override
			public void onError(Exception e){
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	//��ʾ���ȶԻ���
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCancelable(false);
		}
		progressDialog.show();
	}
	
	//�رս��ȶԻ���
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	//����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳�
	@Override
	public void onBackPressed(){
		if(currentLevel == LEVEL_DISTRICT){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
		}
		finish();
	}
}