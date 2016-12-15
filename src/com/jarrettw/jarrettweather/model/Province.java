package com.jarrettw.jarrettweather.model;

public class Province {
	//省份由id和省名构成
	private int id;
	
	private String provinceName;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getProvinceName(){
		return provinceName;
	}
	
	public void setProvinceName(String provinceName){
		this.provinceName = provinceName;
	}
	
}
