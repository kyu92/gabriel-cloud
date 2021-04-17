<template>
  <div>
    <div class="background">
      <div class="stars">
        <div class="star" ref="star" v-for="index in starsCount" :key="index"></div>
      </div>
    </div>
    <div class="el-container">
      <div class="login-box">
        <div class="login">
          <div class="title">
            <span>用户登录</span>
          </div>
          <el-form ref="form" label-width="30px">
            <el-row>
              <el-col :span="16" :offset="4">
                <el-form-item>
                  <span slot="label"><i class="el-icon-user login-icon"></i></span>
                  <el-input placeholder="用户名/邮箱地址/手机号" v-model="username"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="16" :offset="4">
                <el-form-item>
                  <span slot="label"><i class="el-icon-lock login-icon"></i></span>
                  <el-input placeholder="密码" type="password" v-model="password"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="10" :offset="4">
                <el-form-item>
                  <span slot="label"><i class="el-icon-s-check login-icon"></i></span>
                  <el-input placeholder="验证码" v-model="captcha"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <div class="captcha-box">
                  <el-image class="captcha-img" :src="captchaStr" v-if="captchaStr" @click.prevent="getCaptcha">
                    <div slot="placeholder" class="image-slot">
                      <i class="el-icon-loading" />
                    </div>
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="16" :offset="4">
                <!--      第三方登录预留位          -->
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" style="text-align: center">
                <el-button type="primary" @click.prevent="login" :disabled="disable">登录</el-button>
                <el-button @click.prevent="$router.push('/register')" :disabled="disable">注册</el-button>
              </el-col>
            </el-row>
            <br />
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import user from "@/api/user";
import jwt from "@/common/jwt";
export default {
  name: "Login",
  data() {
    return {
      starsCount: 800, //星星数量
      distance: 800, //间距
      username: undefined,
      password: undefined,
      captcha: undefined,
      captchaStr: undefined,
      disable: false,
    }
  },
  methods: {
    getCaptcha(){
      user.getCaptcha().then(res => {
        if (res.data.successful){
          this.captchaStr = `data:image/jpeg;base64,${res.data.data}`;
        }
      }).catch(err => {
        this.$message.error(err);
      });
    },
    login(){
      this.disable = true;
      user.login(this.username, this.password, this.captcha).then(res => {
        if (res.data.successful){
          // login success
          let data = res.data.data;
          this.$notify({
            title: '成功',
            message: `登录成功!`,
            type: 'success',
            duration: 2000,
            onClose: () => {
              this.$store.commit("setLoginState", {
                isLogin: true,
                avatar: data.avatar,
                username: data.username,
                registerDate: data.registerDate,
                permission: data.permission,
                getCover: data.getCover,
                hitokoto: data.hitokoto,
                nick: data.nick,
                emailBind: data.emailBind,
                lastLoginDate: data.lastLoginDate
              });
              jwt.saveToken(data.token);
              this.disable = false;
              this.$router.push("/");
            }
          });
        } else {
          this.$notify({
            title: '警告',
            message: res.data.message,
            type: 'warning'
          });
          this.disable = false;
        }
        this.getCaptcha();
      }).catch(err => {
        this.$notify.error({
          title: '错误',
          dangerouslyUseHTMLString: true,
          message: `登录请求发生错误! <br />原因: ${err}`
        });
        this.getCaptcha();
        this.disable = false;
      });
    },
    handleKeyBoardEnter(e){
      if (e.keyCode === 13){
        this.login();
      }
    }
  },
  created() {
    this.getCaptcha();
  },
  mounted() {
    let stars = this.$refs.star;
    stars.forEach(item => {
      let speed = 0.2 + (Math.random());
      let thisDistance = this.distance + (Math.random() * 300);
      item.style.transformOrigin = `0 0 ${thisDistance}px`;
      item.style.transform = `translate3d(0, 0, -${thisDistance}px) rotateY(${Math.random() * 360}deg) rotateX(${Math.random() * -50}deg) scale(${speed}, ${speed})`;
    });
    if (this.$route.params.username){
      this.username = this.$route.params.username;
    }
    window.addEventListener("keydown", this.handleKeyBoardEnter);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.handleKeyBoardEnter);
  }
}
</script>

<style scoped lang="less">
  .background{
    position: absolute;
    left: 0;
    top: 0;
    z-index: -1;
    pointer-events: none;
    height: 100vh;
    width: 100%;
    //background: radial-gradient(200% 100% at bottom center, #f7f7b6, #ee9966, #1b2947);
    background: radial-gradient(220% 105% at top center, #1b2947 10%, #75517d 40%, #e96f92 65%, #f7f7b6) fixed;
    overflow: hidden;
  }

  @keyframes rotate {
    0% {
      transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
    }
    100% {
      transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(-360deg);
    }
  }
  .stars{
    transform: perspective(500px);
    transform-style: preserve-3d;
    position: absolute;
    left: 50%;
    animation: rotate 90s infinite linear;
    bottom: 0;
  }
  .star{
    width: 2px;
    height: 2px;
    background-color: #f7f7b8;
    position: absolute;
    left: 0;
    top: 0;
    backface-visibility: hidden;
  }

  .login-box {
    @media screen and (max-width: 768px){
      width: 90%;
    }
    @media screen and (min-width: 768px) and (max-width: 1025px){
      width: 50%;
      transform: translateY(80%);
    }
    margin: 0 auto;
    transform: translateY(50%);
    width: 30%;
    background-color: rgba(25, 119, 140, 0.6);
    border: solid 1px #13a1fc;
    z-index: 0;
    background-size: 1400% 300%;
    .login {
      background: linear-gradient(#13a1fc, #13a1fc) left top, linear-gradient(#13a1fc, #13a1fc) left top,
      linear-gradient(#13a1fc, #13a1fc) right top, linear-gradient(#13a1fc, #13a1fc) right top,
      linear-gradient(#13a1fc, #13a1fc) left bottom, linear-gradient(#13a1fc, #13a1fc) left bottom,
      linear-gradient(#13a1fc, #13a1fc) right bottom, linear-gradient(#13a1fc, #13a1fc) right bottom;
      background-repeat: no-repeat;
      background-size: 3px 20px, 20px 3px;
      height: 100%;
      padding: 3px 1px 1px 0;
      margin: -2px;
      border-radius: 3px;
      .captcha-box{
        width: 100%;
        height: 40px;
        border-radius: 5px;
        border: 1px dashed lightgrey;
        overflow: hidden;
        display: flex;
        justify-content: center;
        align-items: center;
        .captcha-img {
          cursor: pointer;
          .image-slot{
            height: 100%;
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background: rgba(255,255,255, 0.2);
          }
          height: 100%;
          width: 100%;
        }
      }
      .login-icon {
        font-size: 20px;
        color: white;
        font-weight: bold;
      }
      .title {
        text-align: center;
        margin: 8px 0 50px 0;
        span {
          font-size: 30px;
          color: #00b8ff;
          font-weight: bold;
          border-bottom: 1px solid whitesmoke;
        }
      }
    }
  }
</style>