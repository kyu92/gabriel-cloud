package com.kyu.gabriel.core.security;

import com.kyu.gabriel.core.string.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Encryptor {

    @Setter
    @Getter
    private String separator;
    private final static Encryptor encryptor;
    private final static char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    static {
        encryptor = new Encryptor("@");
    }

    private Encryptor(String separator){
        this.separator = separator;
    }

    public static Encryptor getInstance(){
        return encryptor;
    }

    public static Map<String, String> mapSort(Map<String, String> target){
        Map<String, String> sortedMap = new TreeMap<>(target);
        List<Map.Entry<String, String>> list = new ArrayList<>(sortedMap.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return sortedMap;
    }

    public static String queryString(Map<String, String> data){
        data = mapSort(data);
        List<String> sub = new ArrayList<>();
        for (Map.Entry<String, String> entry: data.entrySet()){
            if (!StringUtils.isEmpty(entry.getValue())){
                sub.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return String.join("&", sub);
    }

    public String generatePassword(String uuid, String password){
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(password)){
            throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
        }
        String str = uuid + this.separator + password;
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            for (byte b : hash) {
                if ((0xff & b) < 0x10) {
                    hexString.append("0").append(Integer.toHexString((0xFF & b)));
                } else {
                    hexString.append(Integer.toHexString(0xFF & b));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    public static String to32MD5(String plaintext){
        if (StringUtils.isEmpty(plaintext)){
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(plaintext.getBytes(StandardCharsets.UTF_8));
            byte []data = digest.digest();
            char[] str = new char[data.length * 2];
            int k = 0;
            for (byte byte0 : data) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String to16MD5(String plaintext){
        String md5 = to32MD5(plaintext);
        if (md5 != null){
            return md5.substring(8, 24);
        }
        return null;
    }

    public static String makeTencentLocationSign(String path, Map<String, String> params, String sk){
        String qs = queryString(params);
        String unSignString = path + "?" + qs + sk;
        return Objects.requireNonNull(to32MD5(unSignString)).toLowerCase(Locale.ROOT);
    }

    public static void main(String[] args) {
        System.out.println(to32MD5("Hello World"));
    }
}
