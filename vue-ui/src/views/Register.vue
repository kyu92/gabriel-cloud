<template>
  <div>
    <nav-bar :buttons="buttons"></nav-bar>
    <div class="content">
      <h1 class="title">用户注册</h1>
      <div class="register-box">
        <div class="register">
          <el-form ref="registerForm" :model="userForm" :rules="rules" label-width="auto">
            <el-row>
              <el-col>
                <el-form-item prop="username">
                  <span slot="label">用户名</span>
                  <el-input placeholder="请输入用户名" v-model="userForm.username"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item prop="password">
                  <span slot="label">密码</span>
                  <el-input type="password" v-model="userForm.password" placeholder="密码长度必须在6位以上"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item prop="confirmPassword">
                  <span slot="label">确认密码</span>
                  <el-input type="password" v-model="userForm.confirmPassword" placeholder="请再次输入密码"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item prop="email">
                  <span slot="label">电子邮件</span>
                  <el-input placeholder="请输入邮箱地址" v-model="userForm.email"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item prop="mobile">
                  <span slot="label">手机号</span>
                  <el-input placeholder="请输入手机号" v-model="userForm.mobile"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="17">
                <el-form-item prop="captcha">
                  <span slot="label">验证码</span>
                  <el-input placeholder="请输入验证码" v-model="userForm.captcha"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="captcha-box">
                  <el-image class="captcha-img" :src="captchaImg" v-if="captchaImg" @click.prevent="getCaptcha">
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
              <el-col style="text-align: center">
                <el-button type="primary" @click.prevent="doSubmit">注册</el-button>
                <el-button @click.prevent="handleReset">重置</el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="to-login">
          已有账号？
          <router-link to="/login">去登录</router-link>
        </div>
        <div class="forget">
          <router-link to="forget">忘记密码</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import user from "@/api/user";

export default {
  name: "Register",
  components: {
    "nav-bar": () => import("@/components/NavBar")
  },
  data(){
    return {
      buttons: [
        {label: '首页', name: 'index', path: '/'},
        {label: '答疑', name: 'question', path: '/question'},
      ],
      captchaImg: undefined,
      userForm: {
        username: undefined,
        password: undefined,
        confirmPassword: undefined,
        mobile: undefined,
        email: undefined,
        captcha: undefined,
      }
    }
  },
  computed: {
    rules(){
      return{
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {validator: this.validateUsername, trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, message: '密码长度过短', trigger: 'blur'},
          {max: 30, message: '密码长度过长', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请再次输入密码', trigger: 'blur'},
          {validator: this.validatePassword, trigger: 'blur'}
        ],
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {validator: this.validateEmail, trigger: 'blur'}
        ],
        mobile: [
          {validator: this.validateMobile, trigger: 'blur'}
        ],
        captcha: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    getCaptcha(){
      user.getCaptcha().then(res => {
        if (res.data.successful){
          this.captchaImg = `data:image/jpeg;base64,${res.data.data}`;
        }
      }).catch(err => {
        this.$message.error(err);
      });
    },
    validatePassword(rule, value, callback){
      if (this.userForm.password !== this.userForm.confirmPassword){
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    },
    validateEmail(rule, value, callback){
      if (!value){
        callback(new Error("请输入邮箱地址"));
      } else {
        if (!/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,4})+$/.test(value)){
          callback(new Error("请输入正确的邮箱地址"));
          return;
        }
        user.checkEmail(value).then(res => {
          if (res.data.successful){
            callback();
          } else {
            callback(new Error(res.data.message));
          }
        });
      }
    },
    validateMobile(rule, value, callback){
      if (!value){
        callback()
      } else {
        if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(value)){
          callback(new Error("请输入正确的手机号"));
          return;
        }
        user.checkMobilePhone(value).then(res => {
          if (res.data.successful){
            callback();
          } else {
            callback(new Error(res.data.message));
          }
        });
      }
    },
    validateUsername(rule, value, callback){
      if (!value){
        callback(new Error("请输入用户名"));
      } else {
        user.checkUsername(value).then(res => {
          if (!res.data.successful){
            callback(new Error(res.data.message));
          } else {
            callback();
          }
        });
      }
    },
    handleReset(){
      this.$refs["registerForm"].resetFields();
    },
    doSubmit(){
      this.$refs["registerForm"].validate(success => {
        if (success){
          let {username, password, mobile, email, captcha} = this.userForm
          user.register(username, password, email, mobile, captcha).then(res => {
            if (res.data.successful){
              this.$confirm("注册成功，是否立即登录?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "success",
              }).then(() => {
                this.$router.push({
                  name: "Login", params: {username: this.userForm.username}
                });
              });
              this.handleReset();
              this.getCaptcha();
            } else {
              this.$notify({
                title: "警告",
                message: res.data.message,
                type: "warning"
              });
              this.getCaptcha();
            }
          }).catch(err => {
            this.$notify.error({
              title: "错误",
              dangerouslyUseHTMLString: true,
              message: `注册请求发生错误! <br />原因: ${err}`
            });
            this.getCaptcha();
          });
        }
      });
    }
  },
  created() {
    if (this.$store.state.userData.isLogin){
      this.$router.push("/");
    }
    this.getCaptcha();
  }
}
</script>

<style scoped lang="less">
  .content{
    @media screen and (max-width: 768px){
      margin-top: 7vh;
    }
    margin-top: 8vh;
    .title{
      text-align: center;
      padding-top: 30px;
      color: #00b8ff;
      font-size: 30px;
    }
    .register-box{
      width: 30%;
      position: relative;
      margin: 0 auto;
      .register{
        padding-bottom: 30px;
      }
      a{
        color: #00bcd4;
        text-decoration: none;
      }
      .to-login{
        position: absolute;
        left: 10px;
        bottom: 0;
      }
      .forget{
        position: absolute;
        right: 10px;
        bottom: 0;
      }
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
    }
  }
</style>