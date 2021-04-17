<template>
  <div>
    <nav-bar :buttons="buttons" />
    <div class="content">
      <h1 class="title">找回密码</h1>
      <el-steps finish-status="success" :active="step">
        <el-step title="确认用户名"></el-step>
        <el-step title="验证邮箱"></el-step>
        <el-step title="重置密码"></el-step>
      </el-steps>
      <div v-if="step === 0" class="step1">
        <el-form label-width="120px">
          <el-form-item label="唯一用户信息">
            <el-row>
              <el-col>
                <el-input type="text" placeholder="用户名/手机号/邮箱地址" clearable v-model="forgetForm.uniqueData" />
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
        <div style="text-align: center; margin-bottom: 20px">
          <el-alert title="请输入正确的信息，以便我们找到您的用户" type="warning" :closable="false" />
        </div>
        <el-button round plain type="primary" @click="finishStep1">下一步</el-button>
      </div>
      <div v-if="step === 1" class="step2">
        <div class="tip">
          <span class="text">
            验证码已发送至您的邮箱:
          </span>
          <el-link type="primary" class="link">{{ forgetForm.email }}</el-link>
        </div>
        <el-form label-width="80px">
          <el-form-item label="验证码">
            <el-input type="text" placeholder="8位验证码" clearable v-model="forgetForm.verifyCode" />
          </el-form-item>
        </el-form>
        <el-button round plain type="primary" @click="finishStep2">下一步</el-button>
      </div>
      <div v-if="step === 2" class="step3">
        <el-form>
          <el-form-item label="新密码">
            <el-input type="password" placeholder="请输入密码" v-model="forgetForm.password" />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input type="password" placeholder="请再次输入密码" v-model="forgetForm.confirm" />
          </el-form-item>
        </el-form>
        <el-button round plain type="primary" @click="finishStep3">完成</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import user from "@/api/user";
import { Loading } from "element-ui";

export default {
  name: "Forget",
  components: {
    "nav-bar": () => import("@/components/NavBar")
  },
  data(){
    return {
      buttons: [
        {label: '首页', name: 'index', path: '/'},
        {label: '答疑', name: 'question', path: '/question'},
      ],
      step: 0,
      forgetForm: {
        uniqueData: undefined,
        email: undefined,
        uuid: undefined,
        token: undefined,
        verifyCode: undefined,
        password: undefined,
        confirm: undefined,
      }
    }
  },
  methods: {
    finishStep1(){
      if (!this.forgetForm.uniqueData){
        this.$alert("请输入您的用户信息", "错误", {
          confirmButtonText: "确定",
          type: "error"
        });
        return;
      }
      let loadingInstance = Loading.service(document.querySelector(".content"));
      user.getForgetVerifyCode(this.forgetForm.uniqueData).then(res => {
        loadingInstance.close();
        if (res.data.successful){
          let data = res.data.data;
          this.forgetForm.uuid = data.uuid;
          this.forgetForm.email = data.email;
          this.step ++;
          this.$notify({
            title: "提示",
            message: `验证码已发送到您的邮箱: ${data.email}`,
            type: "success"
          });
        } else {
          this.$notify({
            title: "警告",
            message: res.data.message,
            type: "warning"
          });
        }
      }).catch(err => {
        loadingInstance.close();let loadingInstance = Loading.service(document.querySelector(".content"));
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `验证请求发生错误! <br />原因: ${err}`
        });
      });
    },
    finishStep2(){
      if (!this.forgetForm.verifyCode){
        this.$alert("请输入验证码", "错误", {
          confirmButtonText: "确定",
          type: "error"
        });
        return;
      }
      let loadingInstance = Loading.service(document.querySelector(".content"));
      user.verifyForgetCode(this.forgetForm.verifyCode, this.forgetForm.uuid).then(res => {
        loadingInstance.close();
        if (res.data.successful){
          let license = res.data.data;
          sessionStorage.setItem("license", license);
          this.step ++;
        } else {
          this.$notify({
            title: "警告",
            message: res.data.message,
            type: "warning"
          });
        }
      }).catch(err => {
        loadingInstance.close();
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `校验验证码发生错误! <br />原因: ${err}`
        });
      });
    },
    finishStep3(){
      if (!this.forgetForm.password && !this.forgetForm.confirm){
        this.$alert("请输入密码", "错误", {
          confirmButtonText: "确定",
          type: "error"
        });
        return;
      }
      if(this.forgetForm.password !== this.forgetForm.confirm){
        this.$alert("两次输入的密码不一致", "错误", {
          confirmButtonText: "确定",
          type: "error"
        });
        return;
      }
      let license = sessionStorage.getItem("license");
      let loadingInstance = Loading.service(document.querySelector(".content"));
      user.resetPassword(this.forgetForm.password, license).then(res => {
        loadingInstance.close();
        if(res.data.successful) {
          this.$confirm("密码重置成功，是否立即登录?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "success",
          }).then(() => {
            this.$router.push({
              name: "Login", params: {username: this.forgetForm.email}
            });
          });
        } else {
          this.$notify({
            title: "警告",
            message: res.data.message,
            type: "warning"
          });
        }
      }).catch(err => {
        loadingInstance.close();
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `重置密码请求发生错误! <br />原因: ${err}`
        });
      });
    }
  }
}
</script>

<style scoped lang="less">
  .form-box(@marginTop: 30px){
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-top: @marginTop;
  }

  .content{
    @media screen and (max-width: 768px){
      margin: 9vh auto 0 auto;
      width: 90%;
    }
    width: 40%;
    margin: 10vh auto 0 auto;
    .title{
      text-align: center;
      font-size: 28px;
    }
    .step1{
      .form-box();
    }
    .step2{
      .form-box();
      .tip{
        width: 60%;
        border: 1px solid black;
        background-color: rgba(18, 255, 18, 0.3);
        border-radius: 10px;
        font-size: 18px;
        margin: 5px 0 20px 0;
        text-align: center;
        .text{
          font-size: 18px;
        }
        .link{
          font-size: 18px;
        }
      }
    }
    .step3{
      .form-box();
    }
  }
</style>