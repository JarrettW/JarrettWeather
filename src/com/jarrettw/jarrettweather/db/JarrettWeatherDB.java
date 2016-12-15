package com.jarrettw.jarrettweather.db;

import java.util.ArrayList;
import java.util.List;

import com.jarrettw.jarrettweather.model.City;
import com.jarrettw.jarrettweather.model.District;
import com.jarrettw.jarrettweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//作用：封装常用数据库操作
public class JarrettWeatherDB {
	
	//数据库名
	public static final String DB_NAME = "jarrett_weather";
	
	//数据库版本
	public static final int VERSION = 1;
	
	private static JarrettWeatherDB jarrettWeatherDB;
	
	private SQLiteDatabase db;
	
	//将构造方法私有化
	private JarrettWeatherDB(Context context){
		JarrettWeatherOpenHelper dbHelper = new JarrettWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();//创建一个可读写的数据库
	}
	
	//获取JarrettWeatherDB的实例,synchronized:Java解决共享资源竞争的内置支持
	public synchronized static JarrettWeatherDB getInstance(Context context){
		if(jarrettWeatherDB == null){
			jarrettWeatherDB = new JarrettWeatherDB(context);
		}
		return jarrettWeatherDB;
	}
	
	//将Province实例存储到数据库
	public synchronized void saveProvince(Province province){
		ContentValues values = new ContentValues();
		values.put("province_name", province.getProvinceName());
		db.insert(DB_NAME, null, values);
	}
	
	//从数据库读取全国所有的省份信息
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		//Cursor: 此接口提供对数据库的查询所返回的结果集的随机读写访问。
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	//将City实例存储到数据库
	public synchronized void saveCity(City city){
		ContentValues values = new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
//		LogUtil.log("JarrettWeatherDB", "city_id = " + city.getId() + "\tcity_name = " + city.getCityName() + "\tprovince_id = " + city.getProvinceId(), LogUtil.NOTHING);
	}
	
	//从数据库读取全省内的城市信息
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	//将District实例存储到数据库
	public synchronized void saveDistrict(District district){
		ContentValues values = new ContentValues();
		values.put("district_name", district.getDistrictName());
		values.put("city_id", district.getCityId());
		db.insert("District", null, values);
//		LogUtil.log("JarrettWeatherDB", "#################\ndistrict_id = " + district.getId() + "\ndistrict_name = " + city.getDistrictName() + "\ncity_id = " + city.getCityId(), LogUtil.NOTHING);
	}
	
	//从数据路读取某城市下的所有县信息
	public List<District> loadDistricts(int cityId){
		List<District> list = new ArrayList<District>();
		Cursor cursor = db.query("District", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				District districts = new District();
				districts.setDistrictId(cursor.getInt(cursor.getColumnIndex("id")));
				districts.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
				districts.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(districts);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
}










