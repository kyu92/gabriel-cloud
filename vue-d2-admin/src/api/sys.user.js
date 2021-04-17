import axios from "axios";
import jwt from "../../../vue-ui/src/common/jwt";

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
  getDataByToken(){
    return service.request({
      url: `/info`,
      method: "GET"
    });
  },
  getAvatar(uuid){
    return service.get(`/avatar/${uuid}`);
  },
  getUserInfo(uuid){
    return service.get(`/info/${uuid}`);
  }
}
