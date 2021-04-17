package com.kyu.gabriel.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu.gabriel.core.model.dto.TencentLocDTO;
import com.kyu.gabriel.core.string.StringUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class IPUtil {

    private final static String LOCAL_IP = "127.0.0.1";
    private final static OkHttpClient client;
    private final static ObjectMapper mapper;

    static {
        client = new OkHttpClient();
        mapper = new ObjectMapper();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null) {
                if (ipAddress.contains(",")) {
                    return ipAddress.split(",")[0];
                } else {
                    return ipAddress;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isIntranetIP(String ip){
        String regex = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
        return ip.matches(regex) || ip.equals(LOCAL_IP);
    }

    public static TencentLocDTO getIPLocation(String ip, String token, String sk) throws IOException {
        final String path = "/ws/location/v1/ip";
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        params.put("key", token);
        params.put("sig", Encryptor.makeTencentLocationSign(path, params, sk));
        if (!StringUtils.isEmpty(ip) && !isIntranetIP(ip)){
            String qs = Encryptor.queryString(params);
            Request request = new Request
                    .Builder()
                    .url("https://apis.map.qq.com/ws/location/v1/ip?" + qs)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()){
                assert response.body() != null;
                String res = response.body().string();
                return mapper.readValue(res, TencentLocDTO.class);
            }
        }
        return null;
    }
}
