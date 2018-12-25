package com.lgz.grace.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtil.class);

    public static final String LOCAL_IP = initIP();

    public static final String LOCAL_HOST = initLocalHostName();

    /**
     * 获取本机ip地址
     * 此方法为重量级的方法，不要频繁调用
     * <br/> Created on 2013-11-21 下午2:36:27
     *
     * @return
     * @author 李洪波(hb.li@zhuche.com)
     * @since 3.2
     */
    public static String getLocalIp() {
        return LOCAL_IP;
    }

    /**
     * 获取本地机器名
     * 此方法为重量级的方法，不要频繁调用
     * 一般耗时在百毫秒，缓存使用
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalHostName() {
        return LOCAL_HOST;
    }

    public static String initLocalHostName() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.error("get hostname error", e);
        }
        LOGGER.info("get local hostName ：" + hostName);

        return hostName;
    }

    public static String initIP() {
        try {
            //根据网卡取本机配置的IP
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            String ip = null;
            a:
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ipObj = ips.nextElement();
                    if (ipObj.isSiteLocalAddress()) {
                        ip = ipObj.getHostAddress();
                        break a;
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }
}
