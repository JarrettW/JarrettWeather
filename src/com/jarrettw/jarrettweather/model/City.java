package com.jarrettw.jarrettweather.model;

public class City {
	//城市由id，城市名，省份id构成
	private int id;
	
	private String cityName;
	
	private int provinceId;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getCityName(){
		return cityName;
	}
	
	public void setCityName(String cityName){
		this.cityName = cityName;
	}
	
	public int getProvinceId(){
		return provinceId;
	}
	
	public void setProvinceId(int provinceId){
		this.provinceId = provinceId;
	}
	
}
