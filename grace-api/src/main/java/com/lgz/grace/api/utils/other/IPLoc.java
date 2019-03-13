package com.lgz.grace.api.utils.other;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgz.grace.api.utils.httpclient.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

//java版计算signature签名

/**
 * @desc:百度ip定位api
 * @author 颜全弘(qh.yan01@zuche.com) 2015-12-1
 * @see ##http://developer.baidu.com/map/index.php?title=webapi/ip-api
 */
public class IPLoc {

	public static final String DOMAIN = "http://api.map.baidu.com/location/ip?";
	private static final String SK = "zWoWngIzQW7mB7De62LQPeMT3xj9l9EK";
	public static final String AK = "uTslBUkyPEyA6zKF12U4GW3AG5ZYi59U";
	private static final Logger logger = LoggerFactory.getLogger(IPLoc.class);

//	public static void main(String[] args) throws UnsupportedEncodingException {
//		String ip = "124.126.245.162";
//		Map<String, String> result = new HashMap<String, String>();
//		result=getCityByIp(ip);
//		System.out.println(JSON.toJSONString(result));
//	}

	/**
	 * Desc:根据客户端ip获取所在城市
	 * 
	 * @param ip
	 * @return
	 */
	public static Map<String, String> getCityByIp(String ip) {
		// 计算sn跟参数对出现顺序有关，所以用LinkedHashMap保存<key,value>，
		Map<String,String> paras = new LinkedHashMap<String, String>();
		paras.put("ip", ip);
		paras.put("ak", AK);
		paras.put("coor","bd09ll");

		if(StringUtils.isEmpty(ip)){
			return null;
		}
		String sn = null;
		try {
			sn = getSign(paras);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (sn != null) {
			paras.put("sn",sn);
		}else{
			return null;
		}
		Map<String, String> cityInfoMap = null;
		String url="";
		try {
			url = DOMAIN+toQueryString(paras);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		long start = System.currentTimeMillis();
		String cityInfo = HttpClientUtils.getResponseBody(null, url, null, 3000,
				5000, null, HttpClientUtils.MethodEnum.GET);
		long spendtime = System.currentTimeMillis() - start;
		logger.error("baidu api ip location,spend time:{}ms",spendtime);
		if (org.apache.commons.lang.StringUtils.isNotEmpty(cityInfo)) {
			JSONObject obj = JSON.parseObject(cityInfo);
			if (obj != null && obj.getIntValue("status") == 0) {
				cityInfoMap = new HashMap<String, String>();
				JSONObject contentobj = obj.getJSONObject("content");
				if (contentobj != null) {
					JSONObject detailobj = contentobj
							.getJSONObject("address_detail");
					if (detailobj != null) {
						cityInfoMap.put("city", detailobj.getString("city"));
						cityInfoMap.put("district",
								detailobj.getString("district"));
						cityInfoMap.put("province",
								detailobj.getString("province"));
					}
					JSONObject point = contentobj.getJSONObject("point");
					if(point != null){
						cityInfoMap.put("lng",point.getString("x"));
						cityInfoMap.put("lat",point.getString("y"));
					}
				}
			}
		}
		return cityInfoMap;
	}

	public static String getCityNameByIp(String ip) {
		Map<String, String> cityInfo = getCityByIp(ip);
		if (null == cityInfo) {
			return null;
		}
		return cityInfo.get("city").replace("市", "");
	}

	public static LocateCityInfo getLocationCityById(String ip) {
		Map<String, String> cityInfo = getCityByIp(ip);
		if (null == cityInfo) {
			return null;
		}
		LocateCityInfo locationCity  =  new  LocateCityInfo();
		locationCity.setLng(cityInfo.get("lng"));
		locationCity.setLat(cityInfo.get("lat"));
		locationCity.setLoc_city(cityInfo.get("city"));
		locationCity.setLoc_province(cityInfo.get("province"));
		return locationCity;
	}


	public static String getSign(Map<String,String> paras) throws UnsupportedEncodingException {

		// 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，
		String paramsStr = toQueryString(paras);
		// 对paramsStr前面拼接上/location/ip/?,后面直接拼接yoursk得到/location/ip/?ip=211.97.122.119&ak=yourakyoursk
		String wholeStr = new String("/location/ip?" + paramsStr + SK);
		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
		// 调用下面的MD5方法得到最后的sn签名
		String md5SN = MD5(tempStr);
		return md5SN;
	}

	// 对Map内所有value作utf8编码，拼接返回结果
	public static String toQueryString(Map<?, ?> data)
			throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		for (Entry<?, ?> pair : data.entrySet()) {
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(),
					"UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}

	// 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
	private static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}
