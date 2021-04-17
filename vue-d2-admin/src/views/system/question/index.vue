<template>
  <d2-container type="full">
    <div>
      <el-button plain type="success" @click.prevent="showAddDialog = true">新增</el-button>
    </div>
    <br />
    <div>
      <message-box v-for="item in questions" :key="item.id" :quest="item" :remove="remove" />
    </div>
    <el-dialog :visible.sync="showAddDialog" v-dialog-drag :close-on-click-modal="false" title="新增答疑项">
      <div class="answer" v-if="showAddDialog">
        标题:
        <div class="title">
          <el-input v-model="quest.title" type="text" placeholder="请输入标题" />
        </div>
        <div class="content">
          <d2-mde ref="editor" :value="quest.answer" :config="config"/>
        </div>
        <div class="submit">
          <el-button @click.prevent="handleSubmit" type="success" plain round>提交</el-button>
        </div>
      </div>
    </el-dialog>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager"

export default {
  name: "index",
  components: {
    "message-box": () => import("@/components/MessageBox"),
  },
  data(){
    return {
      showAddDialog: false,
      questions: [],
      quest: {
        title: null,
        answer: null,
      },
      config: {
        renderingConfig: {
          codeSyntaxHighlighting: true
        },
        autofocus: true,
        showIcons: ["code", "table"],
        status: true,
        placeholder: '请使用 Markdown 格式书写 ;-)，代码片段黏贴时请注意使用高亮语法。',
        // status: ["autosave", "lines", "words"],
      }
    }
  },
  methods: {
    handleSubmit(){
      this.quest.answer = this.$refs.editor.mde.value();
      this.addQA();
    },
    remove(id){
      this.$confirm("确定要执行删除操作吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        return manager.deleteQA(id);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除答疑项成功`,
            type: "success"
          });
          const index = this.questions.findIndex(val => val.id === id);
          this.questions.splice(index, 1);
          this.showAddDialog = false;
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel"){
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `删除答疑列表发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    listAllQA(){
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
    addQA(){
      const { title, answer } = this.quest;
      if (title && answer){
        manager.addQA(title, answer).then(({data: res}) => {
          if (res.successful){
            this.$notify({
              title: "成功",
              message: `添加答疑项成功`,
              type: "success"
            });
            this.listAllQA();
            this.showAddDialog = false;
          } else {
            this.$notify({
              title: "警告",
              message: res.message,
              type: "warning"
            });
          }
        }).catch(err => {
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `添加答疑列表发生错误! <br />原因: ${err}`
          });
        });
      }
    },
  },
  created() {
    this.listAllQA();
  }
}
</script>

<style scoped lang="less">
.answer{
  height: 500px;
  position: relative;
  .title{
    margin-bottom: 20px;
  }
  .submit{
    text-align: center;
    display: inline-flex;
    justify-content: center;
    width: 100%;
    position: absolute;
    bottom: -10px;
  }
}
</style>
