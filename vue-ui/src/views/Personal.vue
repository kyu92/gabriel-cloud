<template>
  <div>
    <div v-show="crop" class="cropper-wrapper">
      <div class="cropper" id="crop">
        <img ref="avatar" :src="avatarData" alt="" />
      </div>
      <div class="widget">
        <div class="scale-slider">
<!--          <span class="demonstration">缩放</span>-->
          <el-slider v-model="scale" :min="50" :max="200" label="缩放" @input="handleScale" :format-tooltip="formatTooltip"/>
        </div>
        <el-button-group class="finish">
          <el-button icon="el-icon-finished" type="success" @click="finishCrop"></el-button>
          <el-button icon="el-icon-close" type="danger" @click="cancelCrop"></el-button>
        </el-button-group>
      </div>
    </div>
    <nav-bar :buttons="buttons" v-show="!crop" />
    <input type="file" @change="selectAvatar($event)" ref="avatarSelector" v-show="false" accept="image/*"/>
    <div class="content">
      <el-card>
        <div slot="header" class="settings-title">
          <span>个人中心</span>
        </div>
        <div class="settings">
          <el-form ref="userInfo" :model="settings">
            <el-form-item>
              <el-row class="line-wrapper">
                <el-col :span="24">头像</el-col>
                <el-col :span="24">
                  <div @click="handleUpload" style="display: inline-block">
                    <img
                        :src="settings.headPic"
                        class="head-pic"
                        alt="avatar"/>
                  </div>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item class="line-wrapper">
              <el-row :gutter="20">
                <el-col :span="12">
                  <span>昵称</span>
                  <el-input v-model="settings.nick" placeholder="请输入昵称" />
                </el-col>
                <el-col :span="8">
                  <span>性别</span>
                  <el-select v-model="settings.gender" placeholder="请选择性别" style="display: block">
                    <el-option label="男" :value="true" />
                    <el-option label="女" :value="false" />
                  </el-select>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item class="line-wrapper">
              <el-row :gutter="20">
                <div>邮箱地址</div>
                <el-col :span="10">
                  <el-input v-model="settings.email" placeholder="请输入邮箱" :disabled="!settings.editEmail"/>
                </el-col>
                <el-col :span="12">
                  <el-button @click="settings.editEmail = true" type="primary" plain>编辑</el-button>
                  <div @click="handleCheckEmail" style="display: inline">
                    <el-alert
                        class="tip"
                        title="邮箱地址未校验，请点击校验"
                        type="warning"
                        v-if="!checkEmail"
                        center
                        :closable="false"
                        show-icon />
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <div>手机号</div>
                <el-col :span="10">
                  <el-input v-model="settings.mobile" placeholder="请输入手机号" :disabled="!settings.editMobile"/>
                </el-col>
                <el-col :span="12">
                  <el-button @click="settings.editMobile = true" type="primary" plain>编辑</el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item class="line-wrapper">
              <div style="font-size: 19px">偏好设置</div>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div>
                    <span>加载封面</span>
                    <el-switch
                        style="display: block"
                        v-model="getCover"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-text="是"
                        inactive-text="否" />
                  </div>
                </el-col>
                <el-col :span="8">
                  <div>
                    <span>一言</span>
                    <el-switch
                        style="display: block"
                        v-model="hitokoto"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-text="是"
                        inactive-text="否" />
                  </div>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item class="line-wrapper">
              <div style="font-size: 19px">密码设置</div>
              <el-row :gutter="20">
                <el-col :span="10">
                  <el-input type="password" placeholder="原密码" v-model="settings.oldPassword" />
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 5px">
                <el-col :span="10">
                  <el-input type="password" placeholder="新密码" v-model="settings.newPassword" />
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 5px">
                <el-col :span="10">
                  <el-input type="password" placeholder="确认密码" v-model="settings.confirmPassword" />
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
          <div style="text-align: center">
            <el-button type="success" @click="saveSetting">保存设置</el-button>
          </div>
        </div>
      </el-card>
      <br />
    </div>
    <el-dialog :visible.sync="showCheckEmail" title="邮箱校验" @opened="handleOpenCheckDialog" v-dialog-drag>
      <el-form>
        <el-form-item label="验证码">
          <el-input placeholder="请输入6位数字验证码" v-model="verifyCode" />
        </el-form-item>
      </el-form>
      <div style="text-align: right">
        <el-button type="success" plain round @click="checkVerifyCode">确认</el-button>
      </div>
      <div class="tip">
        <span>系统已经向该邮箱发送了一封验证激活邮件，请查收邮件，进行验证激活。</span>
        <br />
        <span>如果没有收到验证邮件，您可以更换一个邮箱，或者
          <el-link class="resend" @click="resend" :disabled="disableResend">
            重新接收验证邮件
            <span v-if="disableResend">
              ({{ coolDown }})
            </span>
          </el-link>
        </span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Cropper from "cropperjs";
import "cropperjs/dist/cropper.css";
import user from "@/api/user";

export default {
  name: "Personal",
  components: {
    "nav-bar": () => import("@/components/NavBar")
  },
  data(){
    return {
      buttons: [
        {label: "首页", name: "index", path: "/"},
        {label: "答疑", name: "question", path: "/question"},
      ],
      settings: {
        headPic: "/images/anonymous.png",
        nick: undefined,
        gender: undefined,
        email: undefined,
        editEmail: false,
        mobile: undefined,
        editMobile: false,
        getCover: true,
        hitokoto: true,
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
        changeAvatar: false,
      },
      avatarData: "",
      crop: false,
      cropper: undefined,
      scale: 100,
      showCheckEmail: false,
      verifyCode: undefined,
      coolDown: 120,
      disableResend: false,
      sendCount: 0,
    }
  },
  computed: {
    checkEmail: {
        get(){
          return this.$store.state.userData.emailBind;
        },
        set(isBind){
          this.$store.commit("bindEmail", isBind);
        }
    },
    getCover: {
      get(){
        return this.$store.state.userData.getCover;
      },
      set(value){
        this.$store.commit("setCoverGet", value);
      }
    },
    hitokoto: {
      get(){
        return this.$store.state.userData.hitokoto;
      },
      set(value){
        this.$store.commit("setHitokoto", value);
      }
    }
  },
  methods: {
    selectAvatar(event){
      let file = event.target.files[0];
      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.crop = true;
        this.avatarData = reader.result;
        this.cropper.replace(this.avatarData);
        document.getElementsByTagName("body")[0].style.overflow = "hidden";
      }
    },
    handleUpload(){
      this.$refs.avatarSelector.click();
    },
    init(){
      this.cropper = new Cropper(this.$refs.avatar, {
        viewMode: 1,
        dragMode: "none",
        initialAspectRatio: 1,
        aspectRatio: 1,
        background: true,
        autoCropArea: 0.6,
        zoomOnWheel: false,
        movable: true,
        zoomOnTouch: false,
      });
    },
    handleScale(){
      this.cropper.reset();
      this.cropper.scale(this.scale / 100);
    },
    formatTooltip(val) {
      return val / 100;
    },
    finishCrop(){
      this.crop = false;
      document.getElementsByTagName("body")[0].style.overflow = "visible";
      this.settings.headPic = this.cropper.getCroppedCanvas({
        imageSmoothingQuality: "high"
      }).toDataURL("image/jpeg");
      this.settings.changeAvatar = true;
    },
    cancelCrop(){
      this.crop = false;
      document.getElementsByTagName("body")[0].style.overflow = "visible";
    },
    getSettings(){
      user.getSettings().then(res => {
        let data = res.data;
        if (data.successful){
          this.settings.nick = data.data.nick;
          this.settings.gender = data.data.gender;
          this.settings.email = data.data.email;
          this.settings.mobile = data.data.mobile;
        }
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取个人信息发生错误! <br />原因: ${err}`
        });
      });
    },
    getAvatar(){
      user.getAvatar().then(res => {
        let data = res.data;
        if (data.successful){
          if (data.data){
            this.settings.headPic = `data:image/jpg;base64,${data.data}`;
          }
        } else {
          this.$notify({
            title: "警告",
            message: data.message,
            type: "warning"
          });
        }
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取头像时发生错误! <br />原因: ${err}`
        });
      })
    },
    saveSetting(){
      let data = {};
      if (this.settings.headPic && this.settings.headPic !== "/images/anonymous.png"){
        data.avatarBase64 = this.settings.headPic.replace(/^data:image\/jpeg;base64,/, "");
      }
      if (!/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,4})+$/.test(this.settings.email)){
        this.$notify({
          title: "警告",
          message: "请输入正确的邮箱地址",
          type: "warning"
        });
        return;
      }
      if (this.settings.oldPassword && this.settings.newPassword && this.settings.confirmPassword){
        if (this.settings.newPassword !== this.settings.confirmPassword){
          this.$notify({
            title: "警告",
            message: "两次输入的密码不一致",
            type: "warning"
          });
          return;
        } else {
          data.oldPassword = this.settings.oldPassword;
          data.password = this.settings.newPassword;
        }
      }
      if (this.settings.nick){
        data.nick = this.settings.nick;
      }
      data.changeAvatar = this.settings.changeAvatar;
      data.gender = this.settings.gender;
      data.email = this.settings.email;
      if (this.settings.mobile){
        if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(this.settings.mobile)){
          this.$notify({
            title: "警告",
            message: "请输入正确的手机号",
            type: "warning"
          });
          return;
        }
        data.mobile = this.settings.mobile;
      }
      data.getCover = this.getCover;
      data.hitokoto = this.hitokoto;
      user.saveSetting(data).then(res => {
        if (res.data.successful){
          this.$notify({
            title: "提示",
            message: "设置信息已保存",
            type: "success",
            onClose: () => {
              this.$store.dispatch("getData");
            }
          });
          this.settings.changeAvatar = false;
        } else {
          this.$notify({
            title: "警告",
            message: res.data.message,
            type: "warning"
          });
        }
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `保存设置信息时发生错误! <br />原因: ${err}`
        });
      });
    },
    handleCheckEmail(){
      console.log("show")
      this.showCheckEmail = true;
    },
    sendVerifyCode(){
      user.verifyEmail().then(res => {
        if (res.data.successful){
          this.$notify({
            title: "提示",
            message: `验证码已发送至您的邮箱: ${this.settings.email}`,
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
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `提交邮箱验证码时发生错误! <br />原因: ${err}`
        });
      });
    },
    handleOpenCheckDialog(){
      if (this.sendCount > 0){
        return;
      }
      this.sendVerifyCode();
      this.sendCount ++;
    },
    resend(){
      this.sendVerifyCode();
      this.disableResend = true;
      this.sendCount ++;
      let timer = setInterval(() => {
        if (this.coolDown > 0){
          this.coolDown --;
        } else {
          this.coolDown = 120;
          this.disableResend = false;
          clearInterval(timer);
        }
      }, 1000);
    },
    checkVerifyCode() {
      user.checkVerifyCode(this.verifyCode).then(res => {
        if(res.data.successful){
          this.showCheckEmail = false;
          this.checkEmail = true;
          this.$notify({
            title: "提示",
            message: "邮箱校验通过！",
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
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `提交验证码时发生错误! <br />原因: ${err}`
        });
      });
    }
  },
  mounted(){
    this.init();
    this.getSettings();
    this.getAvatar();
  }
}
</script>

<style scoped lang="less">
  .content{
    @media screen and (max-width: 768px){
      margin: 10vh auto 0 auto;
    }
    width: 80%;
    margin: 11vh auto 0 auto;
    .settings-title{
      font-size: 24px;
    }
    .settings{
      .line-wrapper{
        border-bottom: 1px solid rgba(100, 100, 100, 0.3);
        padding: 0 0 20px 20px;
        .head-pic{
          width: 120px;
          height: 120px;
          border: 1px solid rgba(100, 100, 100, 0.3);
          background: rgba(200, 200, 200, 0.8);
          border-radius: 50%;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 10px;
          cursor: pointer;
        }
        .tip{
          height: 30px;
          display: inline;
          cursor: pointer;
          margin-left: 30px;
        }
      }
    }
  }
  .cropper-wrapper {
    width: 100%;
    height: 100vh;
    z-index: 1000;
    background-color: rgba(100, 100, 100, 0.5);
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    .cropper {
      position: relative;
      width: 60%;
      height: 80vh;
      z-index: 1000;
    }
    .widget {
      width: 60%;
      position: absolute;
      height: 10vh;
      bottom: 0;
      .scale-slider{
        width: 50%;
        display: inline-block;
      }
      .finish{
        float: right;
      }
    }
  }
  .tip{
    font-size: 12px;
    color: darkgray;
    .resend{
      text-decoration: cornflowerblue underline;
      color: #0fb2fc;
    }
  }
</style>