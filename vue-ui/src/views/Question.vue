<template>
  <div class="wrapper">
    <nav-bar :buttons="buttons" />
    <div class="title">答疑&帮助中心</div>
    <div class="quest-and-answer">
      <el-card class="quest-box" v-for="item in questions" :key="item.id">
        <div slot="header">
          <span>{{ item.title }}</span>
        </div>
        <div>
          <vue-markdown :source="item.answer"/>
        </div>
      </el-card>
    </div>
    <div class="put-question">
      <span @click.prevent="showPutQuest = true">疑难咨询</span>
    </div>
    <el-dialog :visible.sync="showPutQuest" destroy-on-close title="疑难咨询">
      <div v-if="showPutQuest">
        <el-form :model="otherQuestForm" ref="form" label-width="auto" :rules="rules">
          <el-row>
            <el-form-item label="标题" prop="title">
              <el-input type="text" v-model="otherQuestForm.title" placeholder="请输入标题"/>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="描述" prop="content">
              <el-input type="textarea" @input="handleInput" v-model="otherQuestForm.content" placeholder="请输入相关描述" rows="5"/>
            </el-form-item>
            <span
                style="float: right; transform: translateY(-15px)"
                v-if="otherQuestForm.content && otherQuestForm.content.length > 250">
              {{ otherQuestForm.content.length }}/300
            </span>
          </el-row>
          <el-row>
            <el-tooltip effect="light" content="请留下您的联系方式以便我们能够尽快给您反馈" placement="bottom-end">
              <el-form-item label="联系方式" prop="information">
                <el-input type="text" v-model="otherQuestForm.information" placeholder="请输入联系方式"/>
              </el-form-item>
            </el-tooltip>
          </el-row>
          <el-row>
            <el-form-item label="类型" prop="contactType">
              <el-select v-model="otherQuestForm.contactType" placeholder="请选择联系方式类型">
                <el-option :value="0" label="邮箱"/>
                <el-option :value="1" label="手机号"/>
              </el-select>
            </el-form-item>
          </el-row>
        </el-form>
        <div style="text-align: center">
          <el-button round plain type="success" @click.prevent="handleSubmit">提交</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import manager from "@/api/manager";
export default {
  name: "Question",
  components: {
    "nav-bar": () => import("@/components/NavBar"),
    "vue-markdown": () => import("vue-markdown")
  },
  data(){
    return {
      buttons: [
        {label: "首页", name: "index", path: "/"},
      ],
      questions: [],
      showPutQuest: false,
      otherQuestForm: {
        title: null,
        content: null,
        information: null,
        contactType: null
      },
      rules: {
        title: [
          {required: true, message: "请输入标题", trigger: "blur"}
        ],
        information: [
          {required: true, message: "请输入联系方式", trigger: "blur"}
        ],
        contactType: [
          {required: true, message: "请选择联系方式类型", trigger: "blur"}
        ]
      }
    }
  },
  methods: {
    listQA(){
      manager.listQA().then(({data: res}) => {
        this.questions = res.data;
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取答疑列表发生错误! <br />原因: ${err}`
        });
      });
    },
    handleSubmit(){
      this.$refs.form.validate().then(() => {
        const { title, content, information, contactType } = this.otherQuestForm;
        return manager.putFeedback(title, content, information, contactType);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `提交反馈成功`,
            type: "success"
          });
          this.showPutQuest = false;
          this.otherQuestForm = {
            title: null,
            content: null,
            information: null,
            contactType: null
          };
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err){
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `提交问题反馈发生错误! <br />原因: ${err}`
          });
        } else {
          this.$message({
            type: "error",
            message: "请填写所有必填项"
          });
        }
      });
    },
    handleInput(){
      if (this.otherQuestForm.content && this.otherQuestForm.content.length > 300){
        this.otherQuestForm.content = this.otherQuestForm.content.substring(0, 300);
      }
    }
  },
  created() {
    this.listQA();
  }
}
</script>

<style scoped lang="less">
  @import "../style/flex";
  .wrapper{
    margin-top: 8vh;
    .title{
      color: white;
      font-size: 30px;
      height: 120px;
      background: url("../assets/title-bg2.png") repeat-x;
      .flex-c-c();
    }
    .quest-and-answer{
      width: 60%;
      margin: 10px auto 0 auto;
      background-color: white;
      border-radius: 5px;
      box-shadow: 0 0 5px 2px rgba(100, 100, 100, 0.3);
      height: 72vh;
      overflow-y: scroll;
      &::-webkit-scrollbar{
        width: 0;
      }
      .quest-box{
        width: 95%;
        margin: 10px auto;
        word-break: break-all;
      }
    }
    .put-question{
      background-image: url("../assets/dialog.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
      width: 200px;
      height: 200px;
      user-select: none;
      .flex-c-c();
      position: absolute;
      right: 50px;
      bottom: 0;
      span{
        cursor: pointer;
      }
    }
  }
</style>