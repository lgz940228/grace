package com.lgz.grace.api.utils;

import java.io.Serializable;

/**
 * 定位到的用户所在城市和经纬度
 * @Description:
 * @author mouzongmin
 * @date 2016年4月8日
 */
public class LocateCityInfo implements Serializable {
	
	private String lng;
	
	private String lat;
	
	private String loc_city;
	
	private String loc_province;
	
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLoc_city() {
		return loc_city;
	}
	public void setLoc_city(String loc_city) {
		this.loc_city = loc_city;
	}
	public String getLoc_province() {
		return loc_province;
	}
	public void setLoc_province(String loc_province) {
		this.loc_province = loc_province;
	}
	
}
