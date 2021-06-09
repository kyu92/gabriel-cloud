package com.kyu.gabriel.user.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.cache.RedisUtil;
import com.kyu.gabriel.core.client.UserService;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.user.entity.MailBean;
import com.kyu.gabriel.user.util.MailUtil;
import com.kyu.gabriel.core.exception.UnSupportTypeException;
import com.kyu.gabriel.core.result.JWTUtil;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.core.security.Encryptor;
import com.kyu.gabriel.core.string.StringGenerator;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CaptchaController {

    private final Producer captchaProducer;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
    private final UserService userService;
    private final ConfigMap configMap;
    private final Encryptor encryptor;

    @Autowired
    public CaptchaController(Producer captchaProducer,
                             MailUtil mailUtil,
                             RedisTemplate<String, Object> redisTemplate,
                             UserService userService,
                             ConfigMap configMap){
        this.captchaProducer = captchaProducer;
        this.mailUtil = mailUtil;
        this.redisUtil = new RedisUtil(redisTemplate);
        this.userService = userService;
        this.configMap = configMap;
        this.encryptor = Encryptor.getInstance();
    }

    @GetMapping("/captcha")
    public ResultMap<String> getCaptchaImage(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //生成图片
        BufferedImage image = captchaProducer.createImage(capText);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", stream);
        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
        return ResultMap.success(0,null, s);
    }

    @Authentication
    @PostMapping("/captcha")
    public ResultMap<Void> sendEmailCaptcha(HttpServletRequest request){
        int expire = Integer.parseInt(configMap.get("captchaExpire"));
        User user = (User) request.getAttribute("user");
        String captcha = StringGenerator.randomOnlyNumber(6);
        redisUtil.setEx("captcha:" + user.getEmail(), captcha, expire);
        MailBean bean = new MailBean()
                .setSubject("[Gabriel 阅读器]: 邮箱验证码")
                .setToAccount(user.getEmail())
                .setContent(StringGenerator.makeEmailCaptcha(user.getEmail(), configMap.get("captchaIndexUrl"), captcha, expire));
        if(mailUtil.sendMailAttachment(bean)) {
            return ResultMap.success();
        } else {
            return ResultMap.failed(4001, "发送邮件时出现异常");
        }
    }

    @Authentication
    @PostMapping("/verify")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResultMap<Void> verifyEmailCaptcha(HttpServletRequest request, String verify){
        User user = (User) request.getAttribute("user");
        String captcha = redisUtil.get("captcha:" + user.getEmail(), String.class);
        if (verify.equals(captcha)){
            user.setEmailBind(true);
            userService.updateUser(user);
            redisUtil.del("captcha:" + user.getEmail());
            return ResultMap.success();
        }
        return ResultMap.failed(9001, "验证码错误");
    }

    @RequestMapping("/forget")
    public ResultMap<Map<String, Object>> forgetPassword(String uniqueData){
        int expire = Integer.parseInt(configMap.get("forgetExpire"));
        User user = userService.queryByUniqueColumns(uniqueData).getData();
        if (user == null){
            return ResultMap.failed(1001, "用户不存在");
        }
        String verifyCode = StringGenerator.randomStr(8);
        redisUtil.setEx("forget:" + user.getUuid(), verifyCode, expire);
        MailBean bean = new MailBean()
                .setSubject("[Gabriel 阅读器]: 找回密码")
                .setToAccount(user.getEmail())
                .setContent(StringGenerator.makeEmailCaptcha(user.getEmail(), configMap.get("captchaIndexUrl"), verifyCode, expire));
        if(mailUtil.sendMailAttachment(bean)){
            Map<String, Object> result = new HashMap<>();
            result.put("uuid", user.getUuid());
            result.put("email", user.getEmail());
            return ResultMap.success(0, null, result);
        } else {
            return ResultMap.failed(4001, "发送邮件时出现异常");
        }
    }

    @PostMapping("/verify/{uuid}")
    public ResultMap<String> verifyForgetCode(@PathVariable String uuid, String captcha) throws UnSupportTypeException {
        String verifyCode = redisUtil.get("forget:" + uuid, String.class);
        if (verifyCode.equalsIgnoreCase(captcha)){
            long timestamp = System.currentTimeMillis();
            String verifyStr = StringGenerator.randomStr(8, true);
            Map<String, Object> lint = new HashMap<>();
            lint.put("timestamp", timestamp);
            lint.put("verifyStr", verifyStr);
            redisUtil.setEx("license:" + timestamp + ":" + verifyStr, uuid, 3600);
            String license = JWTUtil.createJWT(lint, 3600, configMap.get("jwtSecret"));
            return ResultMap.success(0, null, license);
        }
        return ResultMap.failed(9001, "验证码错误");
    }

    @RequestMapping("/password")
    public ResultMap<Void> resetPassword(String password, String token){
        try {
            DecodedJWT jwt = JWTUtil.verifyJWT(token, configMap.get("jwtSecret"));
            long timestamp = JWTUtil.getClaim(jwt, "timestamp", Long.class);
            String verifyStr = JWTUtil.getClaim(jwt, "verifyStr", String.class);
            String uuid = redisUtil.get("license:" + timestamp + ":" + verifyStr, String.class);
            User user = userService.queryUUIDExist(uuid).getData();
            user.setPassword(encryptor.generatePassword(uuid, password));
            return userService.updateUser(user);
        } catch (Exception e){
            return ResultMap.failed(9003, "临时授权码校验失败");
        }
    }
}
