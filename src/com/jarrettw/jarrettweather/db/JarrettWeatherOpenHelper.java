package com.jarrettw.jarrettweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//作用：建立数据库表
public class JarrettWeatherOpenHelper extends SQLiteOpenHelper{
	
	//省份表
	private static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement, "
			+ "province_name text)";
	
	//城市表
	private static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement, "
			+ "city_name text, "
			+ "province_id integer)";
	
	//县表
	private static final String CREATE_DISTRICT = "create table District("
			+ "id integer primary key autoincrement, "
			+ "district_name text, "
			+ "city_id integer)";
	
	//构造方法,必须实现基类构造方法
	public JarrettWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	
	//覆写
	@Override
	public void onCreate(SQLiteDatabase db){
		//创建以上三个表
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_DISTRICT);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
	}
	
}
