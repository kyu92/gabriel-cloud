import axios from "axios";
import jwt from "@/common/jwt";

const service = axios.create({
    timeout: 3000,
    baseURL: "/UserServiceConsumer",
    headers: {
        "Authorization": `${jwt.getToken()}`,
        "Content-Type": 'application/x-www-form-urlencoded',
    }
});

export default {
    getCaptcha(){
        let timestamp = new Date().getTime();
        return service.request({
           url: `/captcha?t=${timestamp}`,
           method: "GET",
        });
    },
    register(username, password, email, mobile, captcha){
        let form = new URLSearchParams();
        form.append("username", username);
        form.append("password", password);
        form.append("email", email);
        form.append("captcha", captcha);
        if (mobile){
            form.append("mobile", mobile);
        }
        return service.request({
           url: `/register`,
           data: form,
           method: "POST"
        });
    },
    login(username, password, captcha){
        let form = new URLSearchParams();
        form.append("username", username);
        form.append("password", password);
        form.append("captcha", captcha);
        return service.request({
            url: `/login`,
            data: form,
            method: "POST"
        });
    },
    checkUsername(username){
        return service.request({
            url: `/query/username/${username}`,
            method: "POST"
        });
    },
    checkEmail(email){
        return service.request({
            url: `/query/email/${email}`,
            method: "POST"
        });
    },
    checkMobilePhone(mobile){
        return service.request({
            url: `/query/mobile/${mobile}`,
            method: "POST"
        });
    },
    getDataByToken(){
        return service.request({
           url: `/info`,
            method: "GET"
        });
    },
    getSettings(){
        return service.get("/settings");
    },
    getAvatar(){
        return service.get("/avatar");
    },
    saveSetting(data){
        let form = new URLSearchParams(data);
        return service.request({
            url: "/settings/save",
            data: form,
            method: "POST"
        });
    },
    verifyEmail(){
        return service.post("/captcha");
    },
    checkVerifyCode(verifyCode){
        let form = new URLSearchParams();
        form.append("verify", verifyCode);
        return service.post("/verify", form);
    },
    getForgetVerifyCode(uniqueData){
        let form = new URLSearchParams();
        form.append("uniqueData", uniqueData);
            return service.post("/forget", form, {timeout: 10000,});
    },
    verifyForgetCode(captcha, uuid){
        let form = new URLSearchParams();
        form.append("captcha", captcha);
        return service.post(`/verify/${uuid}`, form);
    },
    resetPassword(password, token){
        let form = new URLSearchParams();
        form.append("password", password);
        form.append("token", token);
        return service.post("/password", form);
    }
}