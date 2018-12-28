package com.lgz.grace.api.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class CookieUtils { 
	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(CookieUtils.class);

	private static final String PREFIX = "maimaicheusermk";

	public static final String ILLEGAL_USER = "illegal_user";
	/**
	 * 获取cookie的值
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String key) {
		String value = null;
		try {
			Cookie[] cs = request.getCookies();
			if (cs == null || cs.length == 0) {
				return null;
			}
			for (Cookie c : cs) {
				if (c.getName().equals(key)) {
					value = c.getValue();
					value = URLDecoder.decode(value, "UTF-8");
					break;
				}
			}
		} catch (Exception e) {
			logger.error("CookieUtils getCookieValue:" + key + ",exception:", e);
		}
		return value;
	}

	/**
	 * 添加cookie，支持httpOnly
	 * 
	 * @param response
	 * @param cookie
	 */
	public static void addHttpOnlyCookie(HttpServletResponse response,
			Cookie cookie) {
		if (response == null || cookie == null) {
			return;
		}
		// 依次取得cookie中的名称、值、最大生存时间、路径、域和是否为安全协议信息
		String cookieName = cookie.getName();
		String cookieValue = cookie.getValue();
		int maxAge = cookie.getMaxAge();
		String path = cookie.getPath();
		String domain = cookie.getDomain();
		boolean isSecure = cookie.getSecure();
		if(StringUtils.isEmpty(cookieValue)){
			return ;
		}
		try {
			cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		   logger.error("addHttpOnlyCookie", e);
		}
		StringBuffer strBufferCookie = new StringBuffer();
		strBufferCookie.append(cookieName + "=" + cookieValue + ";");

		if (maxAge >= 0) {
			strBufferCookie.append("Max-Age=" + cookie.getMaxAge() + ";");
		}

		if (StringUtils.isNotEmpty(domain)) {
			strBufferCookie.append("domain=" + domain + ";");
		}

		if (StringUtils.isNotEmpty(path)) {
			strBufferCookie.append("path=" + path + ";");
		}

		if (isSecure) {
			strBufferCookie.append("secure;HttpOnly;");
		} else {
			strBufferCookie.append("HttpOnly;");
		}
		response.addHeader("Set-Cookie", strBufferCookie.toString());
	}
	
	/*public static void doCookie(HttpServletRequest request,HttpServletResponse response) throws Exception {

		String mmcweb_usermk = CookieUtils.getCookieValue(request, Constant.MMCWEB_USER_MK);
		String mmcweb_usermk_en = CookieUtils.getCookieValue(request, Constant.MMCWEB_USER_MK_EN);
		if(StringUtils.isEmpty(mmcweb_usermk) || StringUtils.isEmpty(mmcweb_usermk_en)){
			mmcweb_usermk = request.getSession().getId();
			Cookie cookie = new Cookie(Constant.MMCWEB_USER_MK, mmcweb_usermk);
			cookie.setPath("/");
			cookie.setMaxAge(3600 * 24 * 365);// 设置浏览历史的cookie有效期为一年以防止浏览器关闭时被清除

			String enmmcweb_usermk = Encrypt.md5EncodeHex(PREFIX+mmcweb_usermk);
			Cookie enCookie = new Cookie(Constant.MMCWEB_USER_MK_EN, enmmcweb_usermk);
			enCookie.setPath("/");
			enCookie.setMaxAge(3600 * 24 * 365);// 设置浏览历史的cookie有效期为一年以防止浏览器关闭时被清除

			CookieUtils.addHttpOnlyCookie(response, cookie);
			CookieUtils.addHttpOnlyCookie(response, enCookie);
		}
	}

	public static String getMMCUserMark(HttpServletRequest request){
		String mmcweb_usermk = CookieUtils.getCookieValue(request, Constant.MMCWEB_USER_MK);
		String mmcweb_usermk_en = CookieUtils.getCookieValue(request, Constant.MMCWEB_USER_MK_EN);
		if (StringUtils.isEmpty(mmcweb_usermk) || StringUtils.isEmpty(mmcweb_usermk_en)){
			return CookieUtils.ILLEGAL_USER;
		}
		String enmmcweb_usermk = Encrypt.md5EncodeHex(PREFIX+mmcweb_usermk);
		if(enmmcweb_usermk.equals(mmcweb_usermk_en)){
			return mmcweb_usermk;
		}
		return CookieUtils.ILLEGAL_USER;
	}*/


}
