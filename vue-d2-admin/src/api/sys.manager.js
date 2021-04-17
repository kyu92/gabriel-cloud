import axios from "axios";
import jwt from "../../../vue-ui/src/common/jwt";
import qs from "qs";

const service = axios.create({
  timeout: 3000,
  baseURL: "/ManagerServiceConsumer",
  headers: {
    "Authorization": `${jwt.getToken()}`,
    "Content-Type": 'application/x-www-form-urlencoded',
  }
});


export default {
  getIndexInfo(){
    return service.get("/info");
  },
  getCloudModules(){
    // return service.get("/service");
    return axios.get("/test.json");
  },
  /**
   * 获取用户列表
   * @param {Object} formData
   * @returns {Promise<any>}
   */
  getUserList(formData){
    let form = new URLSearchParams();
    for (let key in formData){
      if (!formData.hasOwnProperty(key)){
        continue;
      }
      if (formData[key] !== "" && formData[key] != null){
        form.append(key, formData[key]);
      }
    }
    return service.post("/user/list", form);
  },
  /**
   * 重置密码
   * @param {string} uuid
   */
  resetPassword(uuid){
    let form = new URLSearchParams({uuid});
    return service.post("/user/reset", form);
  },
  /**
   * 设置用户封禁状态
   * @param {String} uuid
   * @param {boolean} status
   * @param {String} reason
   * @param {number} endDate
   */
  changBanStatus(uuid, status, reason, endDate){
    let form = new URLSearchParams({uuid, status, endDate});
    if (reason){
      form.append("reason", reason);
    }
    return service.post("/user/ban", form);
  },
  /**
   * 清除用户全部数据，彻底删除用户
   * @param {String} uuid
   * @returns {Promise<any>}
   */
  deleteUserAndData(uuid){
    let form = new URLSearchParams({uuid});
    return service.post("/user/delete", form, {timeout: 5000});
  },
  /**
   * 列出封禁记录
   * @param {Object} formData
   */
  listBanRecord(formData){
    let form = new URLSearchParams();
    for (let key in formData){
      if (!formData.hasOwnProperty(key)){
        continue;
      }
      if (formData[key]){
        form.append(key, formData[key]);
      }
    }
    return service.post("/ban/list", form);
  },
  /**
   * 撤销封禁记录
   * @param {Number} id
   */
  relieve(id){
    return service.post(`/ban/relieve/${id}`);
  },
  /**
   * 批量撤销封禁记录
   * @param {Array<Number>}ids
   */
  relieveBatch(ids){
    let form = new FormData();
    form.append("ids", ids);
    return service.post("/ban/relieve/batch", ids, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  /**
   * 删除封禁记录
   * @param {Number} id
   */
  deleteBan(id){
    return service.post(`/ban/delete/${id}`)
  },
  /**
   * 批量删除封禁记录
   * @param {Array<Number>} ids
   */
  deleteBanBatch(ids){
    return service.post("/ban/delete/batch", ids, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  listSysLog(page, limit){
    let form = new URLSearchParams({page, limit});
    return service.post("/log/list", form);
  },
  clearLogs(){
    return service.post("/log/clear")
  },
  /**
   * 批量删除日志
   * @param {Number[]} ids
   */
  deleteLogs(ids){
    return service.post("/log/delete", ids, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  /**
   * 根据条件列出书籍
   * @param {Object} formData
   */
  listBooks(formData){
    let form = new URLSearchParams();
    for (let key in formData){
      if (!formData.hasOwnProperty(key)){
        continue;
      }
      if (formData[key]){
        form.append(key, formData[key]);
      }
    }
    return service.post("/book/list", form, {timeout: 5000});
  },
  /**
   * 获取封面
   * @param {String} ownerUid
   * @param {String} coverName
   * @returns {Promise<any>}
   */
  getCover(ownerUid, coverName){
    return service.get(`/book/cover?ownerUid=${ownerUid}&coverName=${coverName}`, {responseType: 'blob'});
  },
  /**
   * 删除书籍
   * @param {String} uuid
   * @returns {Promise<any>}
   */
  deleteBook(uuid){
    let form = new URLSearchParams({uuid});
    return service.post("/book/delete", form);
  },
  /**
   * 修改封面
   * @param {String} uuid
   * @param {String} base64
   * @returns {Promise<any>}
   */
  changeCover(uuid, base64){
    let form = new URLSearchParams({base64});
    return service.post(`/book/cover/${uuid}`, form);
  },
  /**
   * 批量删除书籍
   * @param {String[]} uuidArr
   * @returns {Promise<any>}
   */
  deleteBookBatch(uuidArr){
    return service.post("/book/delete/batch", uuidArr, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  /**
   * 获取书籍下载链接
   * @param {String} uuid
   */
  getBookDownloadUrl(uuid){
    return service.get(`/book/download/${uuid}`);
  },
  /**
   * 保存全局配置修改
   * @param {Object} webService
   * @param {Object} minio
   * @param {Object} jwt
   */
  saveConfig({webService, minio, jwt}){
    let form = {}
    form["queryLocation"] = webService.use;
    form["webServiceKey"] = webService.key;
    form["webServiceSK"] = webService.sk;
    form["minioEndpoint"] = minio.endpoint;
    form["minioAccessKey"] = minio.accessKey;
    form["minioSecretKey"] = minio.secretKey;
    form["jwtExpire"] = jwt.expire;
    form["jwtSecret"] = jwt.secret;
    return service.post("/config/save", form, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  /**
   * 读取全局配置
   * @returns {Promise<any>}
   */
  loadConfig(){
    return service.get("/config");
  },
  /**
   * 列出所有答疑项
   * @returns {Promise<any>}
   */
  listQA(){
    return service.get("/quest/list");
  },
  /**
   * 删除答疑项
   * @param {number} id
   * @returns {Promise<any>}
   */
  deleteQA(id){
    return service.post(`/quest/delete/${id}`);
  },
  /**
   * 更改编辑答疑项
   * @param {number} id
   * @param {string} title
   * @param {string} content
   * @returns {Promise<any>}
   */
  updateQA(id, title, content){
    let form = new URLSearchParams({title, content});
    return service.post(`/quest/update/${id}`, form);
  },
  /**
   * 添加新的答疑项
   * @param {string} title
   * @param {string} content
   * @returns {Promise<any>}
   */
  addQA(title, content){
    let form = new URLSearchParams({title, content});
    return service.post("/quest/add", form);
  },
  /**
   * 获取反馈列表
   * @param {object} data
   * @returns {Promise<any>}
   */
  listFeedback(data){
    const { page, limit, keyword } = data;
    let form = new URLSearchParams({page, limit});
    if (keyword){
      form.append("keyword", keyword);
    }
    return service.post("/quest/feedback/list", form);
  },
  /**
   * 删除用户反馈
   * @param {number[]} ids
   * @returns {Promise<any>}
   */
  deleteFeedback(ids){
    return service.post("/quest/feedback/delete", ids, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  },
  /**
   * 设置反馈状态
   * @param {number[]} ids
   * @param {number} status
   * @returns {Promise<any>}
   */
  setFeedbackStatus(ids, status){
    return service.post(`/quest/feedback/status/${status}`, ids, {headers: {"Content-Type": "application/json;charset=UTF-8"}});
  }
}
