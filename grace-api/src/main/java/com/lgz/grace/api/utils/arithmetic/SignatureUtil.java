package com.lgz.grace.api.utils.arithmetic;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by lgz on 2019/3/13.
 */
public class SignatureUtil {
    private final static String SIGN_TYPE_RSA = "RSA";
    private final static String SIGN_ALGORITHMS = "SHA1WithRSA";
    private final static String CHARSETNAME = "UTF-8";

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm,
                                                    String priKey) throws Exception {
        if (isNullOrEmpty(algorithm) || isNullOrEmpty(priKey))
            return null;

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey;
        encodedKey = Base64.decodeBase64(priKey.getBytes(CHARSETNAME));

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, String pubKey)
            throws Exception {

        if (isNullOrEmpty(algorithm) || isNullOrEmpty(pubKey))
            return null;

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        /*StringWriter writer = new StringWriter();
        StreamUtil.io(
                new InputStreamReader(new ByteArrayInputStream(pubKey
                        .getBytes(CHARSETNAME)), CHARSETNAME), writer);*/

        byte[] encodeByte;
        encodeByte = Base64.decodeBase64(pubKey.getBytes(CHARSETNAME));

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodeByte));
    }

    public static String sign(String plain, String prikey) throws Exception {
        if (isNullOrEmpty(plain) || isNullOrEmpty(prikey))
            return null;

        PrivateKey privatekey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, prikey);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(privatekey);
        signature.update(plain.getBytes(CHARSETNAME));
        byte[] signed = signature.sign();

        return new String(Base64.encodeBase64(signed), CHARSETNAME);
    }

    public static boolean verify(String plain, String sign, String pubkey)
            throws Exception {
        if (isNullOrEmpty(plain) || isNullOrEmpty(sign)
                || isNullOrEmpty(pubkey))
            return false;

        PublicKey publicKey = getPublicKeyFromX509(SIGN_TYPE_RSA, pubkey);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(publicKey);
        signature.update(plain.getBytes(CHARSETNAME));

        return signature
                .verify(Base64.decodeBase64(sign.getBytes(CHARSETNAME)));
    }

    private static boolean isNullOrEmpty(String orgStr) {
        return (orgStr == null || orgStr.trim().length() == 0);
    }
}
