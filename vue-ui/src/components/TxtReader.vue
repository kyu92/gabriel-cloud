<template>
  <div class="book-wrapper">
    <div v-html="content" ref="content" class="content" :style="viewerStyle">
    </div>
  </div>
</template>

<script>
import book from "@/api/book";

export default {
  name: "TxtReader",
  props: ["uuid"],
  data(){
    return {
      content: null,
      pageCount: 1,
      currentPage: 1,
      viewerStyle: {
        color: "black",
        backgroundColor: "whitesmoke",
        fontFamily: "宋体",
        fontSize: "14px",
      },
      contentInfo: {
        offsetWidth: null,
        offsetHeight: null,
        scrollTop: null,
      }
    }
  },
  methods: {
    getContent(){
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      book.getBook(this.uuid).then(res => {
        return new Promise(resolve => {
          let reader = new FileReader();
          reader.onload = e => {
            resolve(reader.result);
          }
          reader.readAsText(res);
        });
      }).then(txt => {
        this.content = txt
            .replace(/(\S+)\r\n/gi, "<p>$1</p>")
            .replace(/^\s.$/g, "<br>")
            .replace(/ /g, "&nbsp;")
            .replace(/\n/g, "<br>");
      }).then(() => {
        this.calcPageCount();
        this.contentInfo.offsetWidth = this.$refs.content.offsetWidth;
        this.contentInfo.offsetHeight = this.$refs.content.offsetHeight;
        this.$emit("ready");
        loading.close();
      }).catch(err => {
        console.log(err);
        loading.close();
      });
    },
    calcPageCount(){
      this.pageCount = Math.ceil(parseInt(this.$refs.content.scrollHeight) / parseInt(this.$refs.content.offsetHeight));
      this.$emit("pageStatusChanged", this.currentPage, this.pageCount);
    },
    showPage(){
      const { content } = this.$refs;
      this.$emit("changePage", this.currentPage);
      const loc = (this.currentPage - 1) * (content.offsetHeight - 10);
      content.scrollTop = loc;
      this.contentInfo.scrollTop = loc;
    },
    nextPage(){
      this.currentPage ++;
      if (this.currentPage >= this.pageCount){
        this.currentPage = this.pageCount;
      }
      this.showPage();
    },
    prevPage(){
      this.currentPage --;
      if (this.currentPage <= 1){
        this.currentPage = 1;
      }
      this.showPage();
    },
    firstPage(){
      this.currentPage = 1;
      this.showPage();
    },
    lastPage(){
      this.currentPage = this.pageCount;
      this.showPage();
    },
    changeProcess(percent){
      let height = this.$refs.content.scrollHeight * percent;
      this.currentPage = Math.ceil(height / this.$refs.content.offsetHeight);
      this.$emit("changePage", this.currentPage);
      this.$refs.content.scrollTop = height;
    },
    setBgColor(color, fontColor){
      this.viewerStyle.backgroundColor = color;
      this.viewerStyle.color = fontColor;
    },
    setFontStyle(font, fontSize){
      this.viewerStyle.fontFamily = font;
      this.viewerStyle.fontSize = fontSize + "px";
      this.calcPageCount();
    },
    saveProgress(){
      let info = {};
      info.offsetWidth = this.contentInfo.offsetWidth;
      info.offsetHeight = this.contentInfo.offsetHeight;
      info.progressData = this.contentInfo.scrollTop;
      book.saveTxtProgress(this.uuid, info);
    },
    loadProgress(data){
      if (!data){
        return;
      }
      let info = JSON.parse(data);
      let width = this.$refs.content.offsetWidth;
      let area = info.offsetWidth * info.progressData;
      this.$refs.content.scrollTop = Math.ceil(area / width);
      this.currentPage = Math.ceil(this.$refs.content.scrollTop / this.$refs.content.offsetHeight);
      if (this.currentPage <= 0){
        this.currentPage = 1;
      }
      if (this.currentPage >= this.pageCount){
        this.currentPage = this.pageCount;
      }
      this.$emit("changePage", this.currentPage);
    }
  },
  watch: {
    currentPage(val){
      const process = (val / this.pageCount).toFixed(2);
      this.$emit("percentage", process);
    }
  },
  mounted() {
    this.getContent();
  }
}
</script>

<style scoped lang="less">
  .book-wrapper{
    width: 100%;
    height: 100%;
    .content{
      width: 100%;
      height: 100%;
      overflow-y: hidden;
      word-break: break-all;
      line-height: 20px;
      padding: 3px;
    }
  }
</style>