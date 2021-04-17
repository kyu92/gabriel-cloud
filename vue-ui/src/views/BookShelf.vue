<template>
  <div>
    <nav-bar :buttons="buttons" />
    <div class="content">
      <div class="title">
        <span class="text">我的书籍</span>
        <div class="control">
          <el-button class="control-btn" type="warning" round icon="el-icon-upload" @click.prevent="showUploadDialog=true">上传书籍</el-button>
          <el-button class="control-btn" type="danger" round icon="el-icon-delete-solid" @click.prevent="clearBooks">清空书籍</el-button>
        </div>
      </div>
      <hr />
      <el-row class="books">
        <book-box v-for="book in books" :book="book" :key="book.uuid" :remove="handleRemove" />
      </el-row>
    </div>
    <footer-bar :to-top="toTop"/>
    <el-dialog :visible.sync="showUploadDialog"
               title="上传书籍"
               destroy-on-close
               :close-on-click-modal="false"
               :fullscreen="!isLandscape"
               :before-close="handleDialogClose"
               v-dialog-drag>
      <div v-if="showUploadDialog" class="upload-box">
        <el-upload
            class="book-upload"
            drag
            ref="upload"
            action="/BookServiceConsumer/upload"
            list-type="picture"
            :auto-upload="false"
            v-if="isLandscape"
            :limit="8"
            :on-change="onChange"
            :on-success="onSuccess"
            :on-error="onError"
            :on-exceed="onExceed"
            :headers="upload.uploadHeaders"
            :file-list="upload.fileList"
            accept=".txt,.epub,.pdf"
            multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传epub/txt/pdf文件，且单个文件不超过100MB</div>
        </el-upload>
        <el-upload
            v-else
            ref="upload"
            class="book-upload"
            action="/BookServiceConsumer/upload"
            multiple
            :auto-upload="false"
            :on-change="onChange"
            :on-success="onSuccess"
            :on-error="onError"
            :on-exceed="onExceed"
            :headers="upload.uploadHeaders"
            :file-list="upload.fileList"
            accept=".txt,.epub,.pdf"
            :limit="8">
          <el-button size="mini" type="primary">点击上传</el-button>
          <div class="el-upload__tip" slot="tip">只能上传epub/txt/pdf文件，且单个文件不超过100MB</div>
        </el-upload>
        <el-button type="success" plain round class="upload-btn" @click.prevent="submitUpload">上传</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import book from "@/api/book";
import jwt from "@/common/jwt";
import { inside } from "@/api/hitokoto";
import { Loading } from "element-ui";
export default {
  name: "BookShelf",
  components: {
    "nav-bar": () => import("@/components/NavBar"),
    "footer-bar": () => import("@/components/FooterBar"),
    "book-box": () => import("@/components/BookBox")
  },
  data(){
    return {
      buttons: [
        {label: '首页', name: 'index', path: '/'},
        {label: '答疑', name: 'question', path: '/question'},
      ],
      upload: {
        uploadHeaders: {
          Authorization: jwt.getToken()
        },
        fileList: undefined
      },
      showUploadDialog: false,
      books: [ // {name: 标题, type: 电子书格式, uuid: 唯一识别码}
      ]
    }
  },
  computed: {
    media(){
      let xs = window.matchMedia("screen and (max-width: 768px)");
      let sm = window.matchMedia("screen and (min-width: 768px) and (max-width: 1025px)");
      let md = window.matchMedia("screen and (min-width: 1200px)");
      if (xs.matches){
        return "xs";
      } else if (sm.matches){
        return "sm";
      } else if (md.matches){
        return "md";
      } else {
        return "unknown";
      }
    },
    isLandscape(){
      return this.media === 'md' || this.media === 'sm';
    }
  },
  methods: {
    toTop(){
      let elem = document.getElementsByClassName("books")[0];
      let distance = elem.scrollTop;
      let step = distance / 30;
      let timer;
      (function smoothUp() {
        if (distance > 0) {
          distance -= step;
          elem.scrollTop = distance;
          timer = setTimeout(smoothUp, 5)
        } else {
          elem.scrollTop = 0;
          clearTimeout(timer);
        }
      })();
    },
    handleRemove(target, $event){
      $event.stopPropagation()
      this.$confirm(`确定要删除${target.name}吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        book.remove(target.uuid).then(res => {
          if (res.data.successful){
            this.$notify({
              title: "提示",
              type: "success",
              message: "删除成功"
            });
            this.books.splice(this.books.indexOf(target), 1);
          } else {
            this.$notify({
              title: '警告',
              message: res.data.message,
              type: 'warning'
            });
          }
        }).catch(err => {
          this.$notify.error({
            title: '错误',
            dangerouslyUseHTMLString: true,
            message: `删除书籍时发生错误! <br />原因: ${err}`
          });
        });
      });
    },
    clearBooks(){
      this.$confirm(`确定要清空书架吗？<br /><span style="color: red">注意:<b>该操作不可逆</b></span>`, '警告', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        book.clear().then(res => {
          if (res.data.successful){
            this.$notify({
              title: "提示",
              type: "success",
              message: "删除成功"
            });
            let count = this.books.length;
            this.books.splice(0, count);
            this.$message(`删除了${count}本书籍`);
          } else {
            this.$notify({
              title: '警告',
              message: res.data.message,
              type: 'warning'
            });
          }
        }).catch(err => {
          this.$notify.error({
            title: '错误',
            dangerouslyUseHTMLString: true,
            message: `删除书籍时发生错误! <br />原因: ${err}`
          });
        });
      });
    },
    getBooks(){
      let loadingInstance = Loading.service(document.querySelector(".books"));
      book.list().then(res => {
        if (res.data.successful){
          this.books = res.data.data;
        } else {
          this.$notify({
            title: '警告',
            message: res.data.message,
            type: 'warning'
          });
        }
        loadingInstance.close();
      }).catch(err => {
        this.$notify.error({
          title: '错误',
          dangerouslyUseHTMLString: true,
          message: `获取书架信息发生错误! <br />原因: ${err}`
        });
      });
      loadingInstance.close();
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleDialogClose(done){
      this.getBooks();
      done();
    },
    onExceed(){
      this.$notify({
        title: '警告',
        message: "同时最多只能上传的数量为8",
        type: 'warning'
      });
    },
    onError(err, file, fileList){
      this.$notify.error({
        title: '错误',
        dangerouslyUseHTMLString: true,
        message: `上传书籍${file.name}时发生错误! <br />原因: ${err}`
      });
      fileList.push(file);
    },
    onChange(file, fileList){
      console.log(file);
      if (file.status === "ready" && file.size > 50 * 1024 * 1024){
        this.$notify({
          title: '警告',
          message: `${file.name}的文件体积过大，请上传小于50M的文件`,
          type: 'warning'
        });
        fileList.splice(fileList.indexOf(file), 1);
      }
    },
    onSuccess(res, file, fileList){
      console.log(res);
      this.$notify({
        title: '提示',
        message: `${file.name}上传完成！`,
        type: 'success'
      });
      fileList.splice(fileList.indexOf(file), 1);
    },
    getHitokoto(){
      setTimeout(() => {
        if (this.$store.state.userData.hitokoto){
          inside(({content, from}) => {
            let msg = from ? `${content}       —— ${from}` : content;
            console.log(msg);
            this.$message({
              showClose: true,
              iconClass: "el-icon-discount",
              message: msg
            });
          });
        }
      }, 500);
    }
  },
  created() {
    this.getBooks();
  },
  mounted() {
    this.getHitokoto();
  }
}
</script>

<style scoped lang="less">
  @media screen and (max-width: 768px){
    .content {
      margin-top: 7vh !important;
      .title {
        .text {
          font-size: 2rem !important;
        }
        .control {
          z-index: 5;
          float: none !important;
          .control-btn{
            font-size: 0.8rem !important;
            padding: 5px 20px;
          }
        }
      }
      .books {
        height: 63vh !important;
      }
    }
    .upload-box{
      width: 90% !important;
    }
  }
  .content{
    margin: 8vh auto 0 auto;
    width: 80%;
    hr{
      background-color: rgba(120, 120, 120, 0.3);
      height: 1px;
      border: none;
      margin: 20px 0;
    }
    .title{
      margin: 12vh 10% 20px 30px;
      .text{
        font-size: 30px;
        font-weight: bold;
        color: grey;
        font-family: 楷体, serif;
        user-select: none;
      }
      .control{
        float: right;
      }
    }
    .books{
      width: 90%;
      height: 69vh;
      margin: 0 auto 8vh auto;
      padding: 5px 0 0 10px;
      overflow-y: scroll;
      &::-webkit-scrollbar{
        width: 0;
      }
    }
  }
  .upload-box{
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    flex-direction: column;
    .upload-btn{
      padding: 10px 40px;
      margin-top: 20px;
    }
  }
</style>