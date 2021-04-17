package com.kyu.gabriel.core.result;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kyu.gabriel.core.exception.UnSupportTypeException;

import java.util.*;

public class JWTUtil {

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static String stringToUnicode(String str) {
        if (str == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] c = str.toCharArray();
        for (char value : c) {
            if (isChinese(value)){
                sb.append("\\u").append(Integer.toHexString(value));
            } else {
                sb.append(value);
            }
        }
        return sb.toString();
    }


    private static void setPayload(JWTCreator.Builder builder, Map<String, Object> payload) throws UnSupportTypeException {
        for (Map.Entry<String, Object> entry: payload.entrySet()){
            Object value = entry.getValue();
            if (value == null){
                continue;
            }
            switch (value.getClass().getSimpleName()){
                case "String": {
                    builder.withClaim(entry.getKey(), (String) value);
                    break;
                }
                case "Date": {
                    builder.withClaim(entry.getKey(), (Date) value);
                    break;
                }
                case "Integer": {
                    builder.withClaim(entry.getKey(), (Integer) value);
                    break;
                }
                case "Double": {
                    builder.withClaim(entry.getKey(), (Double) value);
                    break;
                }
                case "Boolean": {
                    builder.withClaim(entry.getKey(), (Boolean) value);
                    break;
                }
                case "Long": {
                    builder.withClaim(entry.getKey(), (Long) value);
                    break;
                }
                default: {
                    if (value instanceof Map<?, ?>){
                        builder.withClaim(entry.getKey(), CastUtil.castMap(value, String.class, Object.class));
                    } else if (value instanceof List<?>) {
                        builder.withClaim(entry.getKey(), CastUtil.castList(value, Object.class));
                    } else {
                        throw new UnSupportTypeException("不支持该类型的数据");
                    }
                }
            }
        }
    }

    public static String createJWT(Map<String, Object> payload, int expires, String secret) throws UnSupportTypeException {
        Calendar ins = Calendar.getInstance();
        ins.add(Calendar.SECOND, expires);
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(ins.getTime());
        setPayload(builder, payload);
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public static String createJWT(Map<String, Object> payload, Calendar expires, String secret) throws UnSupportTypeException {
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(expires.getTime());
        setPayload(builder, payload);
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public static String createJWT(Map<String, Object> payload, Date expires, String secret) throws UnSupportTypeException {
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(expires);
        setPayload(builder, payload);
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public static DecodedJWT verifyJWT(String JWTString, String secret){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(JWTString);
    }

    public static <T> T getClaim(DecodedJWT jwt, String name, Class<T> clazz){
        return jwt.getClaim(name).as(clazz);
    }

    public static Map<String, Object> getClaims(DecodedJWT jwt){
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Claim> entry: jwt.getClaims().entrySet()){
            result.put(entry.getKey(), entry.getValue().as(Object.class));
        }
        return result;
    }
}
