<template>
  <div class="box">
    <span class="title">{{ quest.title }}</span>
    <i class="_blank"/>
    <el-divider direction="vertical"/>
    <div class="control">
      <el-button type="text" icon="el-icon-delete" @click.prevent="remove(quest.id)">删除</el-button>
      <el-button type="text" icon="el-icon-edit" @click.prevent="edit">编辑</el-button>
    </div>
    <el-dialog :visible.sync="showDetail" v-dialog-drag :close-on-click-modal="false" title="编辑答疑项">
      <div class="answer">
        标题:
        <div class="title">
          <el-input v-model="quest.title" type="text" />
        </div>
        <div class="content">
          <d2-mde ref="editor" :value="quest.answer" :config="config"/>
        </div>
        <div class="submit">
          <el-button @click.prevent="updateQA" type="success" plain round>提交</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import manager from "@/api/sys.manager";
export default {
  name: "MessageBox",
  props: ["quest", "remove"],
  data(){
    return {
      showDetail: false,
      isReadOnly: false,
      answer: null,
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
  computed: {
    content(){
      return this.$refs.editor.mde.value();
    }
  },
  methods: {
    edit(){
      this.showDetail = true;
    },
    updateQA(){
      const { id, title } = this.quest;
      manager.updateQA(id, title, this.content).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `修改答疑项成功`,
            type: "success"
          });
          this.showDetail = false;
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
          message: `修改答疑列表发生错误! <br />原因: ${err}`
        });
      });
    }
  }
}
</script>

<style scoped lang="less">
  .box{
    height: 30px;
    background-color: white;
    border: 1px solid rgba(200, 200, 200, 0.3);
    border-radius: 5px;
    margin-right: 10px;
    display: inline-flex;
    justify-content: left;
    align-items: center;
    padding: 10px;
    box-shadow: 1px 1px 8px 2px rgba(100, 100, 100, 0.3);
    ._blank{
      margin-left: 20px;
    }
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
  }
</style>

<!--<style lang="less">-->
<!--.content{-->
<!--  .CodeMirror{-->
<!--    height: auto !important;-->
<!--    min-height: 200px !important;-->
<!--  }-->
<!--}-->
<!--</style>-->
