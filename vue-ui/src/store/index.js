import Vue from 'vue'
import Vuex from 'vuex'
import utils from "@/common/utils";
import jwt from "@/common/jwt";
import user from "@/api/user";
import manager from "@/api/manager";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userData: {
      isLogin: false,
      avatar: undefined,
      username: undefined,
      registerDate: undefined,
      permission: undefined,
      getCover: undefined,
      hitokoto: undefined,
      nick: undefined,
      emailBind: undefined,
      lastLoginDate: null,
    },
    userMenu: [
      {label: "进入书架", path: "#", style: "", icon: "el-icon-collection", command: "bookshelf", permission: 9, order: 0},
      {label: "退出登录", path: "#", style: "", icon: "el-icon-switch-button", command: "logout", permission: 9, order: 999},
      {label: "个人中心", path: "#", style: "", icon: "el-icon-user-solid", command: "center", permission: 9, order: 1},
      {label: "管理后台", path: "#", style: "", icon: "el-icon-s-platform", command: "manager", permission: 0, order: 2},
    ],
    navBarButton: [
      {label: '首页', name: 'index', path: '#'},
      {label: '关于', name: 'about', path: '#about'},
      {label: '展示', name: 'display', path: '#display'},
      {label: '答疑', name: 'question', path: '/question'},
    ],
    systemInfo: {
      adminLoginUrl: null,
    }
  },
  mutations: {
    sortUserMenu({userMenu}){
      userMenu = utils.orderByProperty(userMenu, "order");
    },
    setLoginState({userData}, {isLogin, avatar, username, registerDate, permission, getCover, hitokoto, nick, emailBind, lastLogin}){
      userData.username = username;
      userData.isLogin = isLogin;
      userData.avatar = avatar;
      userData.registerDate = registerDate;
      userData.permission = permission;
      userData.getCover = getCover;
      userData.hitokoto = hitokoto;
      userData.nick = nick;
      userData.emailBind = emailBind;
      userData.lastLoginDate = lastLogin;
    },
    bindEmail({userData}, isBind){
      userData.emailBind = isBind;
    },
    setCoverGet({userData}, value){
      userData.getCover = value;
    },
    setHitokoto({userData}, value){
      userData.hitokoto = value;
    },
    setSystemInfo({systemInfo}, data){
      systemInfo.adminLoginUrl = data.adminLoginUrl;
    }
  },
  actions: {
    getData(context){
      let token = jwt.getToken();
      jwt.init(token);
      if (!jwt.isExpired()){
        user.getDataByToken().then(res => {
          if (res.data.successful || res.data.statusCode === 2003){
            let data = res.data.data;
            context.commit("setLoginState", {
              isLogin: true,
              avatar: data.avatar,
              username: data.username,
              registerDate: data.registerDate,
              permission: data.permission,
              getCover: data.getCover,
              hitokoto: data.hitokoto,
              nick: data.nick,
              emailBind: data.emailBind,
              lastLoginDate: data.lastLoginDate,
            });
          } else {
            console.log(res.data.message);
          }
        });
      }
    },
    getSystemInfo({commit}){
      manager.getSystemInfo().then(({data: res}) => {
        commit("setSystemInfo", res.data);
      }).catch(err => {
        console.error(err);
      });
    }
  },
  modules: {
  }
})
