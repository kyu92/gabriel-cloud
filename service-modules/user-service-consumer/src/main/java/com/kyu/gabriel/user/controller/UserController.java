package com.kyu.gabriel.user.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.code.kaptcha.Constants;
import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.client.UserService;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.exception.UnSupportTypeException;
import com.kyu.gabriel.core.file.MinioUtil;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import com.kyu.gabriel.core.result.JWTUtil;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.core.security.Encryptor;
import com.kyu.gabriel.core.string.StringUtils;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RefreshScope
public class UserController {

    private final UserService userService;
    private final MinioUtil minioUtil;
    private final Encryptor encryptor = Encryptor.getInstance();
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;
    private final ConfigMap configMap;

    @Autowired
    public UserController(UserService userService,
                          ConfigMap configMap){
        this.configMap = configMap;
        this.userService = userService;
        this.minioUtil = MinioUtil.getInstance(configMap);
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
    }

    /**
     * 前端传递用户信息时所设置的必要项
     * @param result ResultMap
     * @param user
     * @throws IOException
     */
    private void setInfo(Map<String, Object> result, User user) throws IOException {
        if (StringUtils.isEmpty(user.getHeadPic())){
            result.put("avatar", null);
        } else {
            GetObjectResponse is = minioUtil.download(user.getUuid(), user.getHeadPic());
            byte []data = IOUtils.toByteArray(is);
            String imgStr = encoder.encodeToString(data);
            result.put("avatar", imgStr);
        }
        result.put("username", user.getUsername());
        result.put("registerDate", user.getRegisterDate());
        result.put("permission", user.getPermission());
        result.put("getCover", user.getGetCover());
        result.put("hitokoto", user.getHitokoto());
        result.put("emailBind", user.isEmailBind());
        result.put("nick", user.getNick());
        result.put("lastLoginDate", user.getLastLoginDate());
    }

    @RequestMapping("/login")
    @Logger(value = "用户登录", excludeResponseData = true)
    public ResultMap<Map<String, Object>> userLogin(HttpServletRequest request, String username, String password, String captcha){
        HttpSession session = request.getSession();
        String capText = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(capText)){
            return ResultMap.failed(9001, "验证码错误");
        }
        User user = userService.queryByUniqueColumns(username).getData();
        if (user == null){
            return ResultMap.failed(1001, "当前用户不存在，请检查用户名是否有误");
        }
        if (!encryptor.generatePassword(user.getUuid(), password).equals(user.getPassword())){
            return ResultMap.failed(1002, "输入的密码不正确，请重新尝试");
        }
        BanRecord record = userService.getUserBanInfo(user.getUuid()).getData();
        if (record != null){
            return ResultMap.failed(1004, "当前账号被封禁，理由:" + record.getReason() + ", 解封日期:" + record.getEndDate());
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("uuid", user.getUuid());
        try {
            String token = JWTUtil.createJWT(payload, Integer.parseInt(configMap.get("jwtExpire")), configMap.get("jwtSecret"));
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            setInfo(result, user);
            user.setLastLoginDate(new Date());
            userService.updateUser(user);
            return ResultMap.success(result);
        } catch (UnSupportTypeException e) {
            e.printStackTrace();
            return ResultMap.failed(9002, "token生成失败，请联系管理员");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMap.failed(2003, "头像读取过程发生异常");
        }
    }

    @RequestMapping("/register")
    @Logger("用户注册")
    public ResultMap<Void> userRegister(HttpServletRequest request, String username, String password, String email, String mobile, String captcha){
        HttpSession session = request.getSession();
        String capText = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(capText)){
            return ResultMap.failed(9001, "验证码错误");
        }
        if (username == null || password == null || email == null){
            return ResultMap.failed(1003, "用户名或密码或邮箱地址不能为空");
        }
        String uuid = UUID.randomUUID().toString();
        User user = new User(uuid, username, encryptor.generatePassword(uuid, password), email, mobile, new Date());
        return userService.addUser(user);
    }

    @RequestMapping("/info")
    public ResultMap<Map<String, Object>> getDataByToken(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            DecodedJWT jwt = JWTUtil.verifyJWT(token, configMap.get("jwtSecret"));
            String uuid = JWTUtil.getClaim(jwt, "uuid", String.class);
            User user = userService.queryUUIDExist(uuid).getData();
            if (user != null){
                Map<String, Object> result = new HashMap<>();
                setInfo(result, user);
                user.setLastLoginDate(new Date());
                userService.updateUser(user);
                return ResultMap.success(result);
            }
            return ResultMap.failed(1001, "查询用户不存在");
        } catch (JWTDecodeException e){
            return ResultMap.failed(9003, "token校验失败或已过期，请重新登录");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMap.failed(2003, "头像读取过程发生异常");
        }
    }

    @RequestMapping("/info/{uuid}")
    @Authentication(0)
    public ResultMap<Map<String, Object>> getUserInfo(@PathVariable String uuid){
        User user = userService.queryUUIDExist(uuid).getData();
        if (user != null){
            try {
                Map<String, Object> data = new HashMap<>();
                setInfo(data, user);
                return ResultMap.success(data);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultMap.failed(2003, "头像读取过程发生异常");
            }
        }
        return ResultMap.failed(1001, "用户不存在");
    }

    @RequestMapping("/query/username/{username}")
    public ResultMap<Void> queryUsernameExist(@PathVariable String username){
        ResultMap<User> res = userService.queryUsernameExist(username);
        if (res.getData() != null){
            return ResultMap.failed(2002, "该用户名已被注册");
        }
        return ResultMap.success();
    }

    @RequestMapping("/query/email/{email}")
    public ResultMap<Void> queryEmailExist(@PathVariable String email){
        ResultMap<User> res = userService.queryEmailExist(email);
        if (res.getData() != null){
            return ResultMap.failed(2002, "该邮箱地址已被注册");
        }
        return ResultMap.success();
    }

    @RequestMapping("/query/mobile/{mobile}")
    public ResultMap<Void> queryMobileExist(@PathVariable String mobile){
        ResultMap<User> res = userService.queryMobileExist(mobile);
        if (res.getData() != null){
            return ResultMap.failed(2002, "该手机号已被注册");
        }
        return ResultMap.success();
    }

    @Authentication
    @RequestMapping("/settings")
    public ResultMap<Map<String, Object>> getSettingInfo(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        result.put("nick", user.getNick());
        result.put("gender", user.isGender());
        result.put("email", user.getEmail());
        result.put("mobile", user.getMobile());
        return ResultMap.success(result);
    }

    @Authentication
    @RequestMapping("/avatar")
    public ResultMap<String> getAvatar(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        if (StringUtils.isEmpty(user.getHeadPic())){
            return ResultMap.success();
        }
        GetObjectResponse is = minioUtil.download(user.getUuid(), user.getHeadPic());
        try {
            byte []data = IOUtils.toByteArray(is);
            String imgStr = encoder.encodeToString(data);
            return ResultMap.success(0, null, imgStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultMap.failed(2003, "IO异常");
    }

    @Authentication(0)
    @RequestMapping("/avatar/{uuid}")
    public ResultMap<String> getUserAvatar(@PathVariable String uuid){
        User user = userService.queryUUIDExist(uuid).getData();
        if (user == null){
            return ResultMap.failed(1001, "用户不存在");
        }
        InputStream is = minioUtil.download(uuid, user.getHeadPic());
        try {
            byte []data = IOUtils.toByteArray(is);
            String imgStr = encoder.encodeToString(data);
            return ResultMap.success(0, null, imgStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMap.failed(2003, "IO异常");
    }

    @Authentication
    @RequestMapping("/settings/save")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Logger(value = "保存用户设置", exclude = "avatarBase64")
    public ResultMap<Void> saveSettings(boolean changeAvatar, String avatarBase64, String oldPassword, String password, String nick, boolean gender, String email, String mobile, boolean getCover, boolean hitokoto, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        String oldAvatar = user.getHeadPic();
        if (changeAvatar){
            user.setHeadPic("avatar/" + System.currentTimeMillis() + ".jpg");
        }
        if (!user.getEmail().equals(email)){
            if (userService.queryEmailExist(email).getData() != null){
                return ResultMap.failed(2002, "该邮箱地址已被注册");
            }
            user.setEmail(email);
            user.setEmailBind(false);
        }
        user.setNick(nick);
        user.setGender(gender);
        user.setGetCover(getCover);
        user.setHitokoto(hitokoto);
        if (!StringUtils.isEmpty(mobile) && ! user.getMobile().equals(mobile)){
            if (userService.queryMobileExist(mobile).getData() != null){
                return ResultMap.failed(2002, "该手机号已被注册");
            }
            user.setMobile(mobile);
        }
        if (!StringUtils.isEmpty(oldPassword) || !StringUtils.isEmpty(password)){
            if (user.getPassword().equals(encryptor.generatePassword(user.getUuid(), oldPassword))){
                user.setPassword(password);
            } else {
                return ResultMap.failed(1002, "原密码不正确，请重试");
            }
        }
        ResultMap<Void> result = userService.updateUser(user);
        if (result.isSuccessful()){
            try {
                if (changeAvatar) {
                    if (oldAvatar != null) {
                        minioUtil.remove(user.getUuid(), oldAvatar);
                    }
                    byte []img = decoder.decode(avatarBase64);
                    ByteArrayInputStream is = new ByteArrayInputStream(img);
                    minioUtil.upload(user.getUuid(), user.getHeadPic(), is, "image/jpeg");
                }
                return ResultMap.success();
            } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
                e.printStackTrace();
                return ResultMap.failed(2003, "保存图片时发生异常");
            }
        }
        return result;
    }
}
