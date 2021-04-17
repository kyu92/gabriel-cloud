<template>
  <div class="content-wrapper" ref="wrapper">
    <div class="content-viewer">
      <div class="viewer">
        <canvas ref="viewer"></canvas>
        <div class="text-layer" ref="textLayer" :style="{width: this.page.width + 'px', height: this.page.height + 'px'}"></div>
      </div>
<!--      <div ref="viewer"></div>-->
    </div>
  </div>
</template>

<script>
import book from "@/api/book";
import * as PDFJS from "pdfjs-dist";
import { TextLayerBuilder } from "pdfjs-dist/web/pdf_viewer";
import "pdfjs-dist/web/pdf_viewer.css"

export default {
  name: "PdfReader",
  props: ["uuid"],
  data(){
    return {
      file: null,
      pdfSrc: null,
      currentPage: 1,
      pageCount: 0,
      page: {
        width: null,
        height: null,
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
      book.getBook(this.uuid).then(blob => {
        this.file = window.URL.createObjectURL(blob);
        return this.file;
      }).then(url => {
        return PDFJS.getDocument(url).promise;
      }).then(pdf => {
        this.pdfSrc = pdf;
        this.pageCount = pdf.numPages;
        this.$emit("ready");
        return pdf.getPage(this.currentPage);
      }).then(page => {
        this.$emit("pageStatusChanged", this.currentPage, this.pageCount);
        this.renderPage(page, this.$refs.viewer);
        loading.close();
      }).catch(err => {
        console.log(err);
        loading.close();
      });
    },
    nextPage(){
      this.currentPage ++;
      if (this.currentPage >= this.pageCount){
        this.currentPage = this.pageCount;
      }
      this.pdfSrc.getPage(this.currentPage).then(page => {
        this.renderPage(page, this.$refs.viewer);
        this.$emit("changePage", this.currentPage);
      });
    },
    prevPage(){
      this.currentPage --;
      if (this.currentPage <= 1){
        this.currentPage = 1;
      }
      this.pdfSrc.getPage(this.currentPage).then(page => {
        this.renderPage(page, this.$refs.viewer);
        this.$emit("changePage", this.currentPage);
      });
    },
    firstPage(){
      this.currentPage = 1;
      this.$emit("changePage", this.currentPage);
    },
    lastPage(){
      this.currentPage = this.pageCount;
      this.$emit("changePage", this.currentPage);
    },
    changeProcess(percent){
      this.currentPage = Math.ceil(this.pageCount * percent);
      this.$emit("changePage", this.currentPage);
    },
    loadProgress(data) {
      console.log(data);
      if (data && !isNaN(data)){
        this.currentPage = parseInt(data);
      }
    },
    saveProgress() {
      book.saveTxtProgress(this.uuid, this.currentPage);
    },
    renderPage(page, canvas){
      // 获取pdf尺寸
      this.$refs.textLayer.innerHTML="";
      let viewport = page.getViewport(1);
      // 获取需要渲染的元素
      let context = canvas.getContext('2d');
      canvas.height = viewport.height;
      this.page.height = canvas.height;
      canvas.width = viewport.width;
      this.page.width = canvas.width;
      let renderContext = {
        canvasContext: context,
        viewport: viewport,
      };
      page.render(renderContext)
      //     .then(() => {
      //   return page.getTextContent();
      // }).then(textContent => {
      //   console.log(textContent);
      //   const textLayerDiv = this.$refs.textLayer;
      //   let textLayer = new TextLayerBuilder({
      //     textLayerDiv: textLayerDiv,
      //     pageIndex: page.pageIndex,
      //     viewport: viewport
      //   });
      //   textLayer.setTextContent(textContent);
      //   textLayer.render();
      // });
    }
  },
  watch: {
    currentPage(val) {
      const process = (val / this.pageCount).toFixed(2);
      this.$emit("percentage", process);
    }
  },
  created() {
    this.getContent();
  }
}
</script>

<style scoped lang="less">
.content-wrapper{
  width: 100%;
  height: 100%;
  display: inline-flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  transform: translateY(-10vh);
  .content-viewer{
    width: 100%;
    height: 100%;
    display: inline-flex;
    margin: 0;
    padding: 0;
    border: none;
    justify-content: center;
    .viewer{
      display: inline-block;
      position: relative;
      .text-layer{
        position: absolute;
        left: 0;
        top: 0;
        span{
          position: absolute;
        }
      }
    }
  }
}
</style>