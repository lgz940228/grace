package com.lgz.grace.api.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;

/**
 * 根据经纬度计算距离工具类
 * @author mouzongmin
 * @date 2015年12月17日
 */
public class DistanceUtil {
	
	/**地球半径*/
	private static final double R = 6378137;
	
	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param lng1 第一点经度
	 * @param lat1   第一点纬度
	 * @param lng2 第二点经度
	 * @param lat2   第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double getDistance(double lng1, double lat1, double lng2,double lat2) {
		double a, b;
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (lng1 - lng2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return d;
	}
	
	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param lnglat1 第一点经纬度
	 * @param lnglat2   第一点经纬度
	 * @return 返回距离 单位：米
	 * @throws Exception
	 */
	public static double getDistance(String lnglat1, String lnglat2) throws Exception {
		if(StringUtils.isEmpty(lnglat1) || StringUtils.isEmpty(lnglat2)){
			throw new Exception("经纬度为空");
		}
		String[] lnglatStr1 = lnglat1.replaceAll(" ", "").split(",");
		String[] lnglatStr2 = lnglat2.replaceAll(" ", "").split(",");
		double lng1 = Double.parseDouble(lnglatStr1[0]);
		double lat1 = Double.parseDouble(lnglatStr1[1]);
		double lng2 = Double.parseDouble(lnglatStr2[0]);
		double lat2 = Double.parseDouble(lnglatStr2[1]);
		return getDistance(lng1, lat1, lng2, lat2);
	}

	/**
	 * 格式化距离
	 * @param distance
	 * @return
	 */
	public static String formatDistance(double distance) {
		if(distance > 1000){
			double l = Math.ceil(distance*100/1000)/100;
			return DecimalFormat.getInstance().format(l)+"km";//去掉.0
		}
		double l = Math.ceil(distance*100)/100;
		return  DecimalFormat.getInstance().format(l)+"m";
	}
	
	public static void main(String[] args) throws Exception {
		double d = getDistance("116.476852, 39.979873","116.461781, 40.003894");
		System.out.println(formatDistance(d));
	}
}

