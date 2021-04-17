<template>
  <div class="viewer" :style="bgStyle">
    <div :style="{width: '100vw', height: pageHeight}">
      <div class="page-btn prev" v-if="showHeaderAndFooter">
        <i class="el-icon-caret-left"/>
      </div>
      <div class="page-btn prev shadow" @click.prevent="prevPage"></div>
      <div class="page-btn next" v-if="showHeaderAndFooter">
        <i class="el-icon-caret-right" />
      </div>
      <div class="page-btn next shadow" @click.prevent="nextPage"></div>
    </div>
    <transition
        appear
        enter-active-class="animate__animated animate__slideInDown animate__faster"
        leave-active-class="animate__animated animate__slideOutUp animate__faster">
      <div class="header" v-show="showHeaderAndFooter">
        <div class="page-header">
          <el-page-header @back="$router.push('/bookshelf')"  :content="bookName" />
        </div>
        <div class="section-selector">
          <el-divider direction="vertical" />
          <el-button @click="handleShowSection" :disabled="type !=='epub'">目录</el-button>
        </div>
        <div class="bg-color" v-if="type !== 'pdf'">
          <el-divider direction="vertical" />
          <span>配色: </span>
          <div class="color-selector white" @click="setBgColor('whitesmoke', 'black')"></div>
          <div class="color-selector skin" @click="setBgColor('#f9f4e9', 'black')"></div>
          <div class="color-selector moss" @click="setBgColor('#ceeaba', 'black')"></div>
          <div class="color-selector grey" @click="setBgColor('darkgrey', 'white')"></div>
          <div class="color-selector black" @click="setBgColor('black', 'white')"></div>
        </div>
        <div class="font-style" v-if="type !== 'pdf'">
          <el-divider direction="vertical" />
          <span>显示: </span>
          <el-dropdown
              class="dropdown"
              split-button
              type="primary"
              size="mini"
              v-if="type === 'txt'"
              @command="handleFontFamilyCommand">
            字体: {{ currentFont }}
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="宋体">宋体</el-dropdown-item>
              <el-dropdown-item command="楷体">楷体</el-dropdown-item>
              <el-dropdown-item command="仿宋">仿宋</el-dropdown-item>
              <el-dropdown-item command="黑体">黑体</el-dropdown-item>
              <el-dropdown-item command="隶书">隶书</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown
              class="dropdown"
              split-button
              type="primary"
              size="mini"
              @command="handleFontSizeCommand">
            字号: {{ currentSize }}
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="10">10</el-dropdown-item>
              <el-dropdown-item :command="12">12</el-dropdown-item>
              <el-dropdown-item :command="14">14</el-dropdown-item>
              <el-dropdown-item :command="16">16</el-dropdown-item>
              <el-dropdown-item :command="18">18</el-dropdown-item>
              <el-dropdown-item :command="20">20</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="right-control">
          <span class="full-screen">
            <i class="el-icon-full-screen" v-if="!isFullScreen" @click="fullScreen"/>
            <i class="el-icon-crop" v-else @click="fullScreen"/>
          </span>
        </div>
      </div>
    </transition>
    <div class="content" @click="toggleHeaderAndFooter">
      <div class="content-data">
        <div class="content-viewer">
          <epub-reader
              ref="reader"
              :uuid="uuid"
              @toc="handleToc"
              @ready="setProgress"
              @changePage="handlePageChange"
              @percentage="handlePercentage"
              @pageStatusChanged="handlePageStatusChanged"
              @locationChange="handleLocationChange"
              v-if="type === 'epub'"/>
          <txt-reader
              ref="reader"
              @ready="setProgress"
              @pageStatusChanged="handlePageStatusChanged"
              @changePage="handlePageChange"
              @percentage="handlePercentage"
              :uuid="uuid"
              v-if="type === 'txt'"/>
          <pdf-reader
              ref="reader"
              @ready="setProgress"
              @pageStatusChanged="handlePageStatusChanged"
              @changePage="handlePageChange"
              @percentage="handlePercentage"
              :uuid="uuid"
              v-if="type === 'pdf'"/>
        </div>
      </div>
    </div>
    <transition
        appear
        enter-active-class="animate__animated animate__slideInUp animate__faster"
        leave-active-class="animate__animated animate__slideOutDown animate__faster">
      <div class="footer" v-show="showHeaderAndFooter">
        <div style="width: 400px; display: flex; align-items: center; justify-content: center">
          <i class="el-icon-location-information process" @click="showProcess = true" />
          <span class="prev-btn">
            <i class="el-icon-d-arrow-left" @click.prevent="prevSection" />
            <i class="el-icon-arrow-left" @click.prevent="prevPage" />
          </span>
<!--          <input class="page-selector" type="number" v-model="currentPage" :min="1" :max="totalPage" readonly />-->
          {{ currentPage }} / {{ totalPage }}
          <span class="next-btn">
            <i class="el-icon-arrow-right" @click.prevent="nextPage" />
            <i class="el-icon-d-arrow-right" @click.prevent="nextSection" />
          </span>
        </div>
      </div>
    </transition>
    <el-drawer
        title="进度"
        :visible.sync="showProcess"
        direction="btt">
      <div style="padding: 0 50px">
        <span>阅读进度</span>
        <el-slider v-model="process" :format-tooltip="formatTooltip" show-input @change="changeProcess"/>
      </div>
    </el-drawer>
    <el-drawer
        title="目录"
        :visible.sync="showSection"
        custom-class="toc"
        direction="ltr">
      <div class="section" v-for="(item, index) in toc" @click.prevent="handleSelectSection(item)" :class="{'selected': index === currentTocIndex}">
        {{ item.label.length > 20 ? item.label.substring(0, 20) + "..." : item.label }}
        <el-divider />
      </div>
    </el-drawer>
  </div>
</template>

<script>
// import book from "@/api/book";
export default {
  name: "Viewer",
  components: {
    "epub-reader": () => import("@/components/EpubReader"),
    "txt-reader": () => import("@/components/TxtReader"),
    "pdf-reader": () => import("@/components/PdfReader"),
  },
  data(){
    return {
      showHeaderAndFooter: true,
      uuid: "",
      type: "",
      currentPage: 0,
      totalPage: 0,
      showProcess: false,
      showSection: false,
      process: 1,
      bgStyle: {
        backgroundColor: "whitesmoke",
      },
      currentFont: "宋体",
      currentSize: 14,
      bookName: null,
      progress: undefined,
      pageHeight: null,
      isFullScreen: false,
      toc: null,
      currentTocIndex: 0,
    }
  },
  methods: {
    toggleHeaderAndFooter(){
      this.showHeaderAndFooter = !this.showHeaderAndFooter;
    },
    formatTooltip(val) {
      return `${val}%`;
    },
    nextPage(){
      this.$refs.reader.nextPage();
    },
    nextSection(){
      switch (this.type){
        case "txt": {
          this.$refs.reader.lastPage();
          break;
        }
        case "epub": {
          this.$refs.reader.nextSection();
          break;
        }
        case "pdf": {
          this.$refs.reader.lastPage();
          break;
        }
      }
    },
    prevPage(){
      this.$refs.reader.prevPage();
    },
    prevSection(){
      switch (this.type){
        case "txt": {
          this.$refs.reader.firstPage();
          break;
        }
        case "epub": {
          this.$refs.reader.lastSection();
          break;
        }
        case "pdf": {
          this.$refs.reader.firstPage();
          break;
        }
      }
    },
    changeProcess(){
      let process = this.process / 100.0;
      this.$refs.reader.changeProcess(process);
    },
    setBgColor(bgColor, fontColor){
      this.bgStyle.backgroundColor = bgColor;
      this.$refs.reader.setBgColor(bgColor, fontColor);
    },
    handleFontFamilyCommand(fontFamily){
      this.currentFont = fontFamily;
      this.$refs.reader.setFontStyle(this.currentFont, this.currentSize);
    },
    handleFontSizeCommand(size){
      this.currentSize = size;
      this.$refs.reader.setFontStyle(this.currentFont, this.currentSize);
    },
    handleShowSection(){
      this.showSection = true;
    },
    getBookInfo: async function(){
      try {
        const { getInfo } = (await import("@/api/book")).default;
        const { data: res } = await getInfo(this.uuid);
        this.bookName = res.data.name;
        this.progress = res.data.progress;
        this.type = res.data.suffix.substring(1).toLocaleLowerCase();
        document.title = this.bookName;
      } catch (err) {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取书籍信息时发生错误! <br />原因: ${err}`
        });
      }
    },
    setProgress(){
      this.$refs.reader.loadProgress(this.progress);
    },
    handlePageChange(pageIndex){
      this.currentPage = pageIndex;
    },
    saveReaderProgress(){
      this.$refs.reader.saveProgress();
    },
    handlePageStatusChanged(page, pageCount){
      this.currentPage = page;
      this.totalPage = pageCount;
    },
    fullScreen(){
      // 进入全屏模式
      function launchFullscreen(element) {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.webkitRequestFullscreen) {
          element.webkitRequestFullscreen()
        } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen()
        }
      }

      // 退出全屏模式
      function exitFullscreen() {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.webkitExitFullscreen) {
          document.webkitExitFullscreen()
        }
      }
      if (this.isFullScreen) {
        exitFullscreen()
      } else {
        launchFullscreen(document.documentElement)
      }
      this.isFullScreen = !this.isFullScreen
    },
    handleToc(toc){
      this.toc = toc;
    },
    handleSelectSection(section){
      this.$refs.reader.selectSection(section.href);
      this.currentTocIndex = this.toc.findIndex(val => val === section);
    },
    handleLocationChange(info){
      this.currentTocIndex = info.index;
      this.currentPage = info.index + 1;
    },
    handleKeyDown(e){
      const { keyCode } = e;
      if (keyCode === 65 || keyCode === 37){
        this.prevPage();
      } else if(keyCode === 39 || keyCode === 68){
        this.nextPage();
      }
    },
    handlePercentage(process){
      this.process = process * 100;
    }
  },
  created(){
    const {uuid} = this.$route.params;
    this.uuid = uuid;
    this.getBookInfo();
    this.$on("toggleHeaderAndFooter", this.toggleHeaderAndFooter);
    window.addEventListener("beforeunload", this.saveReaderProgress);
    window.addEventListener("keydown", this.handleKeyDown);
  },
  beforeDestroy() {
    this.saveReaderProgress()
    window.removeEventListener("beforeunload", this.saveReaderProgress);
    window.removeEventListener("keydown", this.handleKeyDown);
  },
  mounted() {
    this.pageHeight = document.body.scrollHeight;
  }
}
</script>

<style lang="less">
 .toc{
   .el-drawer__body{
     overflow-y: scroll;
     &::-webkit-scrollbar{
       width: 0;
     }
   }
 }
</style>

<style scoped lang="less">
  @import "../style/flex";
  .section{
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
    .flex-l-c();
    flex-direction: column;
  }
  .section.selected{
    .section;
    color: #0fb2fc;
    text-shadow: 0 0 3px rgb(238, 140, 20);
  }
  .viewer{
    height: 100vh;
    .page-btn{
      width: 80px;
      height: 100%;
      user-select: none;
      background-color: rgba(100, 100, 100, 0.3);
      display: flex;
      justify-content: center;
      align-items: center;
      font-weight: bold;
      font-size: 50px;
      position: fixed;
    }
    .prev{
      left: 0;
    }
    .next{
      right: 0;
    }
    .shadow{
      width: 80px;
      height: 100%;
      background-color: transparent;
      position: absolute;
      cursor: pointer;
    }
    .header{
      width: 100%;
      height: 9vh;
      background-color: white;
      box-shadow: 0 -2px 5px 3px rgba(100, 100, 100, 0.6);
      position: fixed;
      top: 0;
      left: 0;
      z-index: 1;
      .page-header{
        height: 100%;
        float: left;
        display: inline-flex;
        justify-content: center;
        align-items: center;
      }
      .bg-color{
        height: 100%;
        display: inline-flex;
        float: left;
        justify-content: left;
        align-items: center;
        .color-selector{
          width: 20px;
          height: 20px;
          border-radius: 50%;
          cursor: pointer;
          border: silver solid 1px;
          user-select: none;
          margin-left: 10px;
          transition: border-top-color 1s, border-right-color 1s, border-left-color 1s, border-bottom-color 1s;
          &:hover{
            border: red solid 1px;
          }
        }
        .black{
          background-color: black;
        }
        .white{
          background-color: white;
        }
        .skin{
          background-color: #f9f4e9;
        }
        .moss{
          background-color: #ceeaba;
        }
        .grey{
          background-color: darkgrey;
        }
      }
      .section-selector{
        height: 100%;
        float: left;
        display: inline-flex;
        justify-content: center;
        align-items: center;
      }
      .font-style{
        height: 100%;
        margin: 0 20px;
        float: left;
        display: inline-flex;
        justify-content: left;
        align-items: center;
        .dropdown{
          margin-left: 10px;
        }
      }
      .right-control{
        float: right;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: left;
        margin-right: 20px;
        .full-screen{
          font-size: 30px;
          cursor: pointer;
          user-select: none;
        }
      }
    }
    .content{
      width: 80%;
      height: 100vh;
      position: absolute;
      left: 10%;
      top: 0;
      .content-data{
        width: 100%;
        height: 82%;
        position: absolute;
        top: 9%;
        .content-viewer{
          padding: 10px;
          height: 95%;
          width: 100%;
        }
      }
    }
    .footer{
      width: 100%;
      height: 9vh;
      position: fixed;
      background-color: white;
      box-shadow: 0 2px 5px 3px rgba(100, 100, 100, 0.6);
      left: 0;
      bottom: 0;
      z-index: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: row;
      .process{
        font-size: 30px;
        font-weight: bold;
        margin-right: 30px;
        cursor: pointer;
      }
      .prev-btn{
        cursor: pointer;
        font-size: 30px;
        font-weight: bold;
      }
      .next-btn{
        cursor: pointer;
        font-size: 30px;
        font-weight: bold;
      }
      .page-selector{
        width: 60px;
        height: 30px;
        text-align: center;
        border-radius: 10px;
        border: 1px solid rgb(150, 250, 203, 0.6);
        font-size: 15px;
        input{
          font-size: 15px;
        }
      }
    }
  }
</style>