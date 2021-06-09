<template>
  <d2-container type="full">
    <el-form label-width="auto" v-model="configForm">
      <fieldset>
        <legend>腾讯位置服务WebService</legend>
        <el-form-item label="是否启用">
          <el-switch v-model="configForm.webService.use" active-color="#13ce66" inactive-color="#ff4949"/>
        </el-form-item>
        <el-row>
          <el-form-item label="应用Key" v-if="configForm.webService.use">
            <el-col :span="8">
              <el-input v-model="configForm.webService.key" type="text" placeholder="腾讯位置服务应用Key"/>
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="应用SK" v-if="configForm.webService.use">
            <el-col :span="8">
              <el-input v-model="configForm.webService.sk" type="text" placeholder="腾讯位置服务应用SecretKey"/>
            </el-col>
          </el-form-item>
        </el-row>
      </fieldset>
      <fieldset>
        <legend>储存中心配置</legend>
        <el-row>
          <el-form-item label="endpoint">
            <el-col :span="8">
              <el-input v-model="configForm.minio.endpoint" type="text" placeholder="minio连接地址"/>
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="accessKey">
            <el-col :span="8">
              <el-input v-model="configForm.minio.accessKey" type="text" placeholder="minio用户名"/>
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="secretKey">
            <el-col :span="8">
              <el-input v-model="configForm.minio.secretKey" type="text" placeholder="minio密码"/>
            </el-col>
          </el-form-item>
        </el-row>
      </fieldset>
      <fieldset>
        <legend>JWT配置</legend>
        <el-row>
          <el-form-item label="令牌有效期">
            <el-col :span="8">
              <el-input-number :min="60" v-model="configForm.jwt.expire" placeholder="jwt有效期(秒)"/> 秒
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="令牌秘钥">
            <el-col :span="8">
              <el-input type="text" v-model="configForm.jwt.secret" placeholder="jwt秘钥"/>
            </el-col>
          </el-form-item>
        </el-row>
      </fieldset>
      <fieldset>
        <legend>系统设置</legend>
        <el-row>
          <el-form-item label="邮箱认证验证码有效期">
            <el-col :span="8">
              <el-input-number :min="60" v-model="configForm.system.captchaExpire" placeholder="验证码有效期(秒)"/> 秒
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="忘记密码验证码有效期">
            <el-col :span="8">
              <el-input-number :min="60" v-model="configForm.system.forgetExpire" placeholder="验证码有效期(秒)"/> 秒
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="邮件logo跳转链接">
            <el-col :span="8">
              <el-input type="text" v-model="configForm.system.captchaIndexUrl" placeholder="跳转链接"/>
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="管理员登录链接">
            <el-col :span="8">
              <el-input type="text" v-model="configForm.system.adminLoginUrl" placeholder="登录链接"/>
            </el-col>
          </el-form-item>
        </el-row>
      </fieldset>
    </el-form>
    <div style="text-align: center">
      <el-button type="success" @click.prevent="save">提交</el-button>
      <el-button @click.prevent="loadConfig">重置</el-button>
    </div>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager"
export default {
  name: "index",
  data(){
    return {
      configForm: {
        webService: {
          use: true,
          key: "",
          sk: "",
        },
        minio: {
          endpoint: "",
          accessKey: "",
          secretKey: "",
        },
        jwt: {
          expire: 60,
          secret: "",
        },
        system: {
          captchaExpire: 300,
          captchaIndexUrl: null,
          forgetExpire: 900,
          adminLoginUrl: null,
        }
      }
    }
  },
  methods: {
    loadConfig(){
      manager.loadConfig().then(({data: res}) => {
        const { data } = res;
        this.configForm.webService.use = data.queryLocation === "true";
        this.configForm.webService.key = data.webServiceKey;
        this.configForm.webService.sk = data.webServiceSK;
        this.configForm.minio.endpoint = data.minioEndpoint;
        this.configForm.minio.accessKey = data.minioAccessKey;
        this.configForm.minio.secretKey = data.minioSecretKey;
        this.configForm.jwt.expire = parseInt(data.jwtExpire);
        this.configForm.jwt.secret = data.jwtSecret;
        this.system.captchaExpire = data.captchaExpire;
        this.system.captchaIndexUrl = data.captchaIndexUrl;
        this.system.forgetExpire = data.forgetExpire;
        this.system.adminLoginUrl = data.adminLoginUrl;
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `加载全局配置发生错误! <br />原因: ${err}`
        });
      });
    },
    save(){
      manager.saveConfig(this.configForm).then(({data: res}) => {
        this.$notify({
          title: "成功",
          message: `保存配置成功!`,
          type: "success"
        });
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `保存全局配置发生错误! <br />原因: ${err}`
        });
      });
    }
  },
  created(){
    this.loadConfig();
  }
}
</script>

<style scoped lang="less">
  fieldset {
    border-bottom: none;
    border-left: none;
    border-right: none;
    border-top: 1px solid rgba(100, 100, 100, 0.3);
    legend {
      padding: 0 10px;
      font-size: 14px;
    }
  }
</style>
