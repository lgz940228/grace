package com.lgz.grace.api.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class EncryptUtils {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);
    private static String DEFAULT_CHARSET = "utf-8";
    private static final String KEY = "member_invite";
    public static final String[] KEYS = new String[]{"f256e8a0-9548-4d54-ab40-7aa93f114a02", "075163b5-9d60-4e9c-95d8-a62bed876044", "1a07d41e-d648-4054-8123-88176637c50e"};
    private static final String DES = "DES";

    public EncryptUtils() {
    }

    public static String encryptEmail(String content) {
        return encrypt("member_invite", content);
    }

    public static String decryptEmail(String content) {
        return decrypt("member_invite", content);
    }

    public static String encrypt(String key, String content) {
        try {
            byte[] keyBytes = key.getBytes(DEFAULT_CHARSET);
            byte[] contentBytes = content.getBytes(DEFAULT_CHARSET);
            byte[] encryptBytes = encrypt(contentBytes, keyBytes);
            String encryptStr = byte2hex(encryptBytes);
            return encryptStr;
        } catch (Exception var6) {
            logger.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static String decrypt(String key, String content) {
        try {
            byte[] keyBytes = key.getBytes(DEFAULT_CHARSET);
            byte[] contentBytes = content.getBytes(DEFAULT_CHARSET);
            byte[] decryptBytes = decrypt(hex2byte(contentBytes), keyBytes);
            String encryptStr = "";
            if (decryptBytes != null) {
                encryptStr = new String(decryptBytes, DEFAULT_CHARSET);
            }

            return encryptStr;
        } catch (Exception var6) {
            logger.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(src);
    }

    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        if (src == null) {
            return null;
        } else {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, securekey, sr);
            return cipher.doFinal(src);
        }
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            logger.error("错误的密文:" + new String(b));
            return null;
        } else {
            byte[] b2 = new byte[b.length / 2];

            for(int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);

                try {
                    b2[n / 2] = (byte)Integer.parseInt(item, 16);
                } catch (Exception var5) {
                    return null;
                }
            }

            return b2;
        }
    }

    public static String getEncMobile(String mobile) {
        String tempMobile = mobile;
        if (StringUtils.isNotEmpty(mobile)) {
            tempMobile = mobile.substring(0, 3) + "****" + mobile.substring(7);
        }

        return tempMobile;
    }

    public static String getEncEmail(String email) {
        String value = email;
        if (StringUtils.isNotEmpty(email) && email.indexOf("@") != -1) {
            String emailpre = email.substring(0, email.indexOf("@"));
            String emailnex = email.substring(email.indexOf("@"));
            if (emailpre.length() >= 2) {
                value = emailpre.substring(0, 2) + "***" + emailnex;
            } else {
                value = emailpre + "***" + emailnex;
            }
        }

        return value;
    }

    public static String getEncIDCard(String idCard) {
        String value = idCard;
        if (StringUtils.isNotEmpty(idCard) && idCard.length() >= 2) {
            value = idCard.substring(0, 1) + "***" + idCard.substring(idCard.length() - 1);
        }

        return value;
    }
}
