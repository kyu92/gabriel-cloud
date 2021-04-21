<template>
  <div class="un-login" v-if="!$store.state.userData.isLogin">
    <a @click.prevent="toLogin">登录</a>
    <a @click.prevent="toRegister">注册</a>
  </div>
  <div v-else>
    <el-dropdown @command="handleCommand">
      <div class="logged">
        <el-avatar :size="40" :src="avatar ? `data:image/jpg;base64,${avatar}` : '/images/anonymous.png'" />
        <span>{{ displayName }}</span>
      </div>
      <el-dropdown-menu slot="dropdown" class="user-menu">
        <el-dropdown-item
            v-for="(item, index) in $store.state.userMenu"
            :key="index"
            :icon="item.icon ? item.icon : 'el-icon-right'"
            v-if="$store.state.userData.permission <= item.permission"
            :command="item.command">
          <div class="menu-option">{{ item.label }}</div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <iframe :src="adminSrc" v-if="adminManager" ref="adminFrame" style="display: none"/>
  </div>
</template>

<script>
  import jwt from "@/common/jwt";
  import common from "@/api/common";
  export default {
    name: "UserDisplay",
    data(){
      return {
        reloadCount: 0,
        adminSrc: "https://admin.kyu92.top/login"
      }
    },
    methods: {
      toLogin(){
        this.$router.push("/login")
      },
      toRegister(){
        this.$router.push("/register")
      },
      handleCommand(command){
        switch (command){
          case "bookshelf": {
            this.$router.push("/bookshelf");
            break;
          }
          case "center": {
            this.$router.push("/personal");
            break;
          }
          case "manager": {
            // document.location.href = "http://admin.kyu92.top";
            let data = {
              target: "login",
              data: jwt.getToken()
            }
            window.frames[0].postMessage(data, "*");
            break;
          }
          case "logout": {
            this.$confirm('确认要注销登录吗?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              iconClass: 'el-icon-question',
            }).then(() => {
              if (this.adminManager){
                let data = {
                  target: "logout",
                }
                window.frames[0].postMessage(data, "*");
                let finishLogout = e =>{
                  if (e.data.target === "logoutFinish" && e.origin.indexOf("kyu92.top") > 0){
                    this.$store.commit("setLoginState", {
                      isLogin: false,
                      avatar: null,
                      username: null,
                      registerDate: null,
                      permission: null,
                      getCover: true,
                      hitokoto: true,
                      nick: null,
                      emailBind: false,
                      lastLoginDate: null
                    });
                    jwt.clearToken();
                    this.$router.push("/login");
                    window.removeEventListener("message", finishLogout);
                  }
                }
                window.addEventListener("message", finishLogout);
              } else {
                jwt.clearToken();
                this.$router.push("/login");
              }
            });
            break;
          }
        }
      }
    },
    computed: {
      avatar(){
        return this.$store.state.userData.avatar;
      },
      displayName(){
        let nick = this.$store.state.userData.nick;
        let username = this.$store.state.userData.username;
        return nick ? nick : username;
      },
      adminManager(){
        let permission = this.$store.state.userMenu.find(value => value.command === "manager").permission;
        return permission >= this.$store.state.userData.permission;
      }
    },
    created(){
      this.$store.commit("sortUserMenu");
    }
  }
</script>

<style scoped lang="less">
  .un-login{
    a{
      display: inline-block;
      margin-right: 15px;
    }
  }
  .logged{
    color: #00bcd4;
    font-weight: bold;
    user-select: none;
    font-family: 楷体, serif;
    font-size: 1.3rem;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 20px;
    span{
      margin-left: 10px;
    }
    @media screen and (max-width: 768px){
      font-size: 1.1rem;
      margin-right: 5px;
      span{
        margin-left: 5px !important;
      }
    }
  }
  .user-menu{
    width: 10rem;
    .menu-option{
      display: inline-block;
      font-size: 1rem;
    }
  }
</style>