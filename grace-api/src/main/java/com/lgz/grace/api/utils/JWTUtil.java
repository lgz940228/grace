package com.lgz.grace.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgz on 2019/1/8.
 *
 * 一个token分3部分，按顺序为
 * 头部（header)
 * 其为载荷（payload)
 * 签证（signature)
 * 由三部分生成token
 * 3部分之间用“.”号做分隔。例如eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
 * -----------头部-----------
 * Jwt的头部承载两部分信息：
 * 声明类型，这里是jwt
 * 声明加密的算法 通常直接使用 HMAC SHA256
 * JWT里验证和签名使用的算法，可选择下面的。
 *
 * JWS	    算法名称	描述
 * HS256	HMAC256	    HMAC with SHA-256
 * HS384	HMAC384	    HMAC with SHA-384
 * HS512	HMAC512	    HMAC with SHA-512
 * RS256	RSA256	    RSASSA-PKCS1-v1_5 with SHA-256
 * RS384	RSA384	    RSASSA-PKCS1-v1_5 with SHA-384
 * RS512	RSA512	    RSASSA-PKCS1-v1_5 with SHA-512
 * ES256	ECDSA256	ECDSA with curve P-256 and SHA-256
 * ES384	ECDSA384	ECDSA with curve P-384 and SHA-384
 * ES512	ECDSA512	ECDSA with curve P-521 and SHA-512
 * // header Map
 Map<String, Object> map = new HashMap<>();
 map.put("alg", "HS256");
 map.put("typ", "JWT");
 * -----------playload-----------
 * 载荷就是存放有效信息的地方。基本上填2种类型数据
 * -标准中注册的声明的数据
 * -自定义数据
 * 由这2部分内部做base64加密。最张数据进入JWT的chaims里存放。
 * 标准中注册的声明 (建议但不强制使用)
 * iss: jwt签发者
 * sub: jwt所面向的用户
 * aud: 接收jwt的一方
 * exp: jwt的过期时间，这个过期时间必须要大于签发时间
 * nbf: 定义在什么时间之前，该jwt都是不可用的.
 * iat: jwt的签发时间
 * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
 * JWT.create().withHeader(map) // header
 * .withClaim("iss", "Service") // payload
 * .withClaim("aud", "APP")
 * .withIssuedAt(iatDate) // sign time
 * .withExpiresAt(expiresDate) // expire time
 * -----------playload-----------
 * jwt的第三部分是一个签证信息，这个签证信息算法如下：
 * base64UrlEncode(header) + "." + base64UrlEncode(payload)+your-256-bit-secret
 * 这个部分需要base64加密后的header和base64加密后的payload使用.连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第三部分
 */
public class JWTUtil {
    /**
     * APP登录Token的生成和解析
     */
    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 10天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     * JWT生成Token.<br/>
     *
     * JWT构成: header, payload, signature
     *
     * @param user_id
     *            登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(Long user_id) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP").withClaim("user_id", null == user_id ? null : user_id.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate)// expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("aaaa");
            // token 校验失败, 抛出Token验证非法异常
        }
        Map<String, Claim> claims = jwt.getClaims();
        return claims;
    }

    /**
     * 根据Token获取user_id
     *
     * @param token
     * @return user_id
     */
    public static Long getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_id");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return Long.valueOf(user_id_claim.asString());
    }

    public static String getJwtToken(){
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        //有10天有效期
        nowTime.add(Calendar.DATE, 10);
        Date expiresDate = nowTime.getTime();
        Claims claims = Jwts.claims();
        claims.put("name","cy");
        claims.put("userId", "222");
        claims.setAudience("cy");
        claims.setIssuer("cy");
        String token = Jwts.builder().setClaims(claims).setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;

    }

    public static void parseJwtToken(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        String signature = jws.getSignature();
        Map<String, String> header = jws.getHeader();
        Claims Claims = jws.getBody();
    }

    /*public static void main(String[] args) throws Exception{
        *//*String jwtToken = getJwtToken();
        parseJwtToken(jwtToken);*//*

        String token = createToken(123L);
        Map<String, Claim> stringClaimMap = verifyToken(token);
        Claim aud = stringClaimMap.get("aud");
        System.out.println(aud.asString());
    }*/
}
