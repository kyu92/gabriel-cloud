package com.kyu.gabriel.user.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public Producer KaptchaProducer() {
        Properties kaptchaProperties = new Properties();
        kaptchaProperties.put("kaptcha.border", "yes");
        kaptchaProperties.put("kaptcha.border.color", "105,179,90");
        kaptchaProperties.put("kaptcha.textproducer.char.length","4");
        kaptchaProperties.put("kaptcha.image.height","48");
        kaptchaProperties.put("kaptcha.session.key","code");
        kaptchaProperties.put("kaptcha.image.width","210");
        kaptchaProperties.put("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
        kaptchaProperties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        kaptchaProperties.put("kaptcha.textproducer.font.color","blue");
        kaptchaProperties.put("kaptcha.textproducer.char.space","3");
        kaptchaProperties.put("kaptcha.textproducer.font.size","40");
        kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        //kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.DefaultNoise");
        kaptchaProperties.put("kaptcha.textproducer.char.string","acdefhkmnprtwxy2345678");

        Config config = new Config(kaptchaProperties);
        return config.getProducerImpl();
    }
}
