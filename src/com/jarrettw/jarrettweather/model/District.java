package com.jarrettw.jarrettweather.model;

public class District {
	//����id������������id����
	private int id;
	
	private String districtName;
	
	private int cityId;
	
	public int getDistrictId(){
		return id;
	}
	
	public void setDistrictId(int id){
		this.id = id;
	}
	
	public String getDistrictName(){
		return districtName;
	}
	
	public void setDistrictName(String districtName){
		this.districtName = districtName;
	}
	
	public int getCityId(){
		return cityId;
	}
	
	public void setCityId(int cityId){
		this.cityId = cityId;
	}
	
}
