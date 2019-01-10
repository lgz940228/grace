package com.lgz.grace.api.utils;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by lgz on 2019/1/10.
 */
public class SignUtil {
    public static String doMD5Sign(String targetStr) {
        byte[] md5Result = DigestUtils.md5(targetStr.getBytes(Charsets.UTF_8));
        if (md5Result.length != 16) {
            throw new IllegalArgumentException("MD5加密结果字节数组错误");
        }
        Integer first = Math.abs(bytesToInt(md5Result, 0));
        Integer second = Math.abs(bytesToInt(md5Result, 4));
        Integer third = Math.abs(bytesToInt(md5Result, 8));
        Integer fourth = Math.abs(bytesToInt(md5Result, 12));
        return first.toString() + second.toString() + third.toString() + fourth.toString();
    }

    /**
     * 高位前，低位后，字节数组转INT
     * @param src
     * @param offset
     * @return
     */
    private static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }
}
