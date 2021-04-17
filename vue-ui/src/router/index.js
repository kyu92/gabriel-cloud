import Vue from "vue"
import VueRouter from "vue-router"
import jwt from "@/common/jwt";

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    name: "Index",
    meta: {
      title: "Gabriel Web阅读器 | 首页",
      needLogin: false,
    },
    component: () => import(/* webpackChunkName: "group-index" */ "@/views/Index")
  },
  {
    path: "/login",
    name: "Login",
    meta: {
      title: "用户登录",
      needLogin: false,
    },
    component: () => import(/* webpackChunkName: "group-login" */ "@/views/Login")
  },
  {
    path: "/register",
    name: "Register",
    meta: {
      title: "用户注册",
      needLogin: false,
    },
    component: () => import(/* webpackChunkName: "group-login" */ "@/views/Register")
  },
  {
    path: "/bookshelf",
    name: "BookShelf",
    meta: {
      title: "Gabriel - 书架",
      needLogin: true,
    },
    component: () => import(/* webpackChunkName: "group-bookshelf" */ "@/views/BookShelf")
  },
  {
    path: "/personal",
    name: "Personal",
    meta: {
      title: "Gabriel - 个人中心",
      needLogin: true,
    },
    component: () => import(/* webpackChunkName: "group-personal" */ "@/views/Personal")
  },
  {
    path: "/forget",
    name: "Forget",
    meta: {
      title: "找回密码",
      needLogin: false,
    },
    component: () => import(/* webpackChunkName: "group-login" */ "@/views/Forget")
  },
  {
    path: "/view/:uuid",
    name: "Viewer",
    meta: {
      title: "Gabriel - 阅读页面",
      needLogin: true,
    },
    component: () => import(/* webpackChunkName: "group-bookshelf" */ "@/views/Viewer")
  },
  {
    path: "/question",
    name: "Question",
    meta: {
      title: "Gabriel - 答疑中心",
      needLogin: false
    },
    component: () => import(/* webpackChunkName: "group-login" */ "@/views/Question")
  }
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  if (to.meta.needLogin){
    let token = jwt.getToken();
    jwt.init(token);
    if (!jwt.isExpired()){
      next();
      return;
    }
    router.push("/").then().catch(err => console.error(err));
    return;
  }
  next();
})

export default router
