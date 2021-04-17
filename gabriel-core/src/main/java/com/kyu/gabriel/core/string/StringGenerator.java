package com.kyu.gabriel.core.string;

import java.text.MessageFormat;
import java.util.Random;

public abstract class StringGenerator {

    private static final String number;
    private static final String upper;
    private static final String lower;
    private static final String chars;
    private static final Random random;

    static {
        number = "1234567890";
        upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        lower = "abcdefghijklmnopqrstuvwxyz";
        chars = "+-*/\\|{}[]';\":?.,<>`~@#$%^&*()_=";
        random = new Random();
    }

    private static String _random(int length, String randomMap){
        StringBuilder sb = new StringBuilder();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(randomMap.length());
            sb.append(randomMap.charAt(index));
        }
        return sb.toString();
    }

    public static String randomStr(int length){
        String randomMap = number + upper + lower;
        return _random(length, randomMap);
    }

    public static String randomStr(int length, boolean needUpper){
        String randomMap = number + (needUpper ? (upper + lower) : lower);
        return _random(length, randomMap);
    }

    public static String randomStr(int length, boolean needUpper, boolean needChar){
        String randomMap = number + (needUpper ? (upper + lower) : lower);
        if (needChar){
            randomMap += chars;
        }
        return _random(length, randomMap);
    }

    public static String randomOnlyNumber(int length){
        return _random(length, number);
    }

    public static String makeEmailCaptcha(String email, String indexUrl, String captcha, int expire){
        String unit = "秒";
        if (expire > 86400){
            unit = "天";
            expire = expire / 86400;
        } else if (expire > 3600){
            unit = "小时";
            expire = expire / 3600;
        } else if (expire > 120){
            unit = "分钟";
            expire = expire / 60;
        }
        return String.format("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        .wrapper{\n" +
                "            border: 1px solid rgba(120, 120, 120, 0.6);\n" +
                "            width: 50%%;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        @media screen and (max-width: 768px) {\n" +
                "            .wrapper{\n" +
                "                width: 100%%;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"wrapper\">\n" +
                "    <div style=\"border-bottom: 1px solid rgba(120, 120, 120, 0.6); padding: 10px 0 10px 30px\">\n" +
                "        <a href=\"%s\" style=\"font-size: 40px;color: #409eff;text-decoration: none\">Gabriel</a>\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <div style=\"margin: 30px; font-size: 14px\">\n" +
                "            <span>您的邮箱地址为:</span>\n" +
                "            <a href=\"mailto:%s\">%s</a>,\n" +
                "            <span>验证码为: </span><br />\n" +
                "            <div style=\"margin: 30px 0; color: #409eff; font-size: 35px\">\n" +
                "                %s\n" +
                "            </div>\n" +
                "            <span>验证码有效期为%s%s，请在有效期内输入!</span>\n" +
                "        </div>\n" +
                "        <div style=\"width: 95%%; margin: 0 auto; height: 50px; border-top: 1px dashed rgba(180, 180, 180, 0.6); display: flex; align-items: center\">\n" +
                "            <span style=\"font-size: 12px; color: gray\">本邮件为系统邮件，请勿回复。</span>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>", indexUrl, email, email, captcha, expire, unit);
    }
}
