package com.jarrettw.jarrettweather.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonReader;

import com.jarrettw.jarrettweather.db.JarrettWeatherDB;
import com.jarrettw.jarrettweather.model.City;
import com.jarrettw.jarrettweather.model.District;
import com.jarrettw.jarrettweather.model.Province;

//�ṩ�˹������������ʹ�����������ص�����
public class Utility {
	
	private static JarrettWeatherDB jarrettWeatherDB;
	
	//�����ʹ�����������ص�����
	public static boolean handleResponse(JarrettWeatherDB jarrettWeatherDb, InputStream in){
		LogUtil.log("Utility", "handleResponse");
		jarrettWeatherDB = jarrettWeatherDb;
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		boolean flag = false;
		try{
			reader.beginObject();
			while(reader.hasNext()){
				String nodeName = reader.nextName();
				if(nodeName.equals("resultcode")){
					LogUtil.log("Utility", "resultcode = " + reader.nextString());
					flag = true;
				}else if(nodeName.equals("result") && flag){
					saveAreaToDatabase(reader);//���浽���ݿ�
				}else{
					reader.skipValue();
				}
			}
			reader.endObject();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	//���浽���ݿ�
	private static boolean saveAreaToDatabase(JsonReader reader){
		LogUtil.log("Utility", "saveAreaToDatabase");
		String provinceName = null;	//ʡ��
		String cityName = null;//����
		String districtName = null;//����
		List<String> provinceNames = new ArrayList<String>();//ʡ���б�
		List<String> cityNames = new ArrayList<String>();//�����б�
		boolean changedProvince = false;//�л�ʡ
		boolean changedCity = false;//�л���
		int provinceId = 0;//ʡId
		int cityId = 0;//��Id
		int districtId = 0;//��Id
		Province previousProvince = new Province();//��ǰ��ʡ
		City previousCity = new City();//��ǰ����
		
		try{
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				while(reader.hasNext()){//�����ǰ������������һ��Ԫ��
					String nodeName = reader.nextName();
					if(nodeName.equals("province")){
						provinceName = reader.nextString().trim();
						if(!provinceNames.contains(provinceName)){
							provinceNames.add(provinceName);
							changedProvince = true;
							provinceId++;
						}
					}else if(nodeName.equals("city")){
						cityName = reader.nextString().trim();
						if(!cityNames.contains(cityName)){
							cityNames.add(cityName);
							changedCity = true;
							cityId++;
							LogUtil.log("Utility", "********changedCity = " + changedCity);
						}
					}else if(nodeName.equals("district")){
						districtName = reader.nextString().trim();
					}else{
						reader.skipValue();//�ݹ�������һ��ֵ
					}
				}
				reader.endObject();
				LogUtil.log("Utility", "\nprovince_name = " + provinceName + "\ncity_name = " + cityName + "\ndistrict_name = " + districtName);
				
				if(changedProvince){//�����ǰʡ���Ѹı�
					Province province = new Province();
					province.setId(provinceId);
					province.setProvinceName(provinceName);
					previousProvince = province;
					jarrettWeatherDB.saveProvince(province);
					changedProvince = false;
					LogUtil.log("Utility", "province_id = " + province.getId() + "\tprovince_name = " + province.getProvinceName());
				}
				if(changedCity){//�����ǰ���Ѹı�
					City city = new City();
					city.setId(cityId);
					city.setCityName(cityName);
					city.setProvinceId(previousProvince.getId());
					previousCity = city;
					jarrettWeatherDB.saveCity(city);
					changedCity = false;
					LogUtil.log("Utility", "city_id = " + city.getId() + "\tcity_name = " + city.getCityName() + "\tprovince_id = " + city.getProvinceId());
				}
				
				District district = new District();
				district.setDistrictId(districtId);
				district.setDistrictName(districtName);
				district.setCityId(previousCity.getId());
				jarrettWeatherDB.saveDistrict(district);
				LogUtil.log("Utility", "district_id = " + district.getDistrictId() + "\tdistrict_name = " + district.getDistrictName() + "\tcity_id = " + district.getCityId());
			}
			reader.endArray();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	//����������Ӧ
	public static boolean handleWeatherRespose(Context context, InputStream in){
		LogUtil.log("Utility", "handleWeatherResponse");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));//�����ȡ��
		String line;
		StringBuilder response = new StringBuilder();
		try{
			while((line = reader.readLine()) != null){
				response.append(line);
			}
			LogUtil.log("Utility", "response = " + response.toString());
			return parseWeatherInfo(context, response.toString());//����������Ϣ
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	//����������Ϣ
	private static boolean parseWeatherInfo(Context context, String data){
		LogUtil.log("Utility", "parseWeatherInfo");
		try{
			JSONObject response = new JSONObject(data);
			String resultCode = response.getString("resultcode");
			LogUtil.log("Utility", "resultcode = " + resultCode);
			if(resultCode.equals("200")){
				JSONObject result = response.getJSONObject("result");
				JSONObject today = result.getJSONObject("today");
				String temperature = today.getString("temperature");
				String cityName = today.getString("city");
				String weather = today.getString("weather");
				String date = today.getString("date_y");
				LogUtil.log("Utility", "\ntemperature = " + temperature + "\ncityName = " + cityName + "\nweather = " + weather + "\ndate = " + date);
				return saveWeatherInfo(context, cityName, weather, temperature, date);  //����������Ϣ
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	//����������Ϣ
	private static boolean saveWeatherInfo(Context context, String cityName, String weather, String temperature, String date){
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather", weather);
		editor.putString("temperature", temperature);
		editor.putString("date", date);
		editor.commit();
		return false;
	}
	
}
