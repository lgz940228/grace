package com.lgz.grace.api.utils.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgz.grace.api.utils.httpclient.HttpClientUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author kw.hou
 * @date 2016年8月18日 下午7:49:54 逆地址编码，通过经纬度获取城市
 */
public class GeocodingUtil {

	public static final String HOST = "http://api.map.baidu.com/geocoder/v2/?";
	private static final String SK = "zWoWngIzQW7mB7De62LQPeMT3xj9l9EK";
	private static final String AK = "uTslBUkyPEyA6zKF12U4GW3AG5ZYi59U";

	public static Map<String, String> getCityByLatAndLng(String lat, String lng) {
		String sn = null;
		String address = lat + "," + lng;
		try {
			sn = getSign(address);
			address = URLEncoder.encode(address, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (sn == null) {
			return null;
		}
		Map<String, String> cityInfoMap = new HashMap<String, String>();
		String requestUrl = HOST + "location=" + address
				+ "&output=json&pois=0&ak=" + AK + "&sn=" + sn;
		String cityInfo = HttpClientUtils.getResponseBody(null, requestUrl,
				null, 2000, 2000, null, HttpClientUtils.MethodEnum.GET);
		if (StringUtils.isNotEmpty(cityInfo)) {
			JSONObject obj = JSON.parseObject(cityInfo);
			if (obj != null && obj.getIntValue("status") == 0) {
				JSONObject contentobj = obj.getJSONObject("result");
				if (contentobj != null) {
					JSONObject detailobj = contentobj
							.getJSONObject("addressComponent");
					if (detailobj != null) {
						cityInfoMap.put("city", detailobj.getString("city")
								.replace("市", ""));
						cityInfoMap.put("district",
								detailobj.getString("district"));
						cityInfoMap.put("province",
								detailobj.getString("province"));
					}
				}
			}

		}
		return cityInfoMap;
	}

	public static String getSign(String location)
			throws UnsupportedEncodingException {
		// 计算sn跟参数对出现顺序有关，所以用LinkedHashMap保存<key,value>，
		Map paras = new LinkedHashMap<String, String>();
		paras.put("location", location);
		paras.put("output", "json");
		paras.put("pois", "0");
		paras.put("ak", AK);
		// 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，
		String paramsStr = toQueryString(paras);
		// 对paramsStr前面拼接上/location/ip/?,后面直接拼接yoursk得到/location/ip/?ip=211.97.122.119&ak=yourakyoursk
		String wholeStr = new String("/geocoder/v2/?" + paramsStr + SK);
		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
		// 调用下面的MD5方法得到最后的sn签名
		String md5SN = MD5(tempStr);
		return md5SN;
	}

	// 对Map内所有value作utf8编码，拼接返回结果
	private static String toQueryString(Map<?, ?> data)
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
			e.printStackTrace();
		}
		return null;
	}

	
	public static void main(String[] args) {
		Map<String,String> cityInfoMap = getCityByLatAndLng("30.548397", "104.04701");
		System.out.println(cityInfoMap.get("city"));
	}


}
