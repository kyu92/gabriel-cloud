<template>
  <div id="reader-viewer" ref="view">

  </div>
</template>

<script>
import book from "@/api/book";
import "jszip";
import ePub from "epubjs";
export default {
  name: "EpubReader",
  props: ["uuid"],
  data(){
    return {
      book: undefined,
      rendition: undefined,
      navigation: undefined,
      themes: undefined,
      currentPage: 1,
      locations: undefined,
      spine: undefined,
      toc: undefined,
      currentLoc: undefined,
      currentSection: 0,
      meta: {
        pageCount: undefined,
        title: null,
        creator: null,
      },
      themesOptions: [
        {
          id: "0",
          name: "default",
          rules: {
            body: {
              "color": "#000",
              "background-color": "whitesmoke",
            }
          }
        },
        {
          id: "1",
          name: "白色",
          rules: {
            body: {
              "color": "#000",
              "background-color": "whitesmoke"
            }
          }
        },
        {
          id: "2",
          name: "浅棕色",
          rules: {
            body: {
              "color": "#000",
              "background-color": "#f9f4e9"
            }
          }
        },
        {
          id: "3",
          name: "护眼",
          rules: {
            body: {
              "color": "#000",
              "background-color": "#ceeaba"
            }
          }
        },
        {
          id: "4",
          name: "灰色",
          rules: {
            body: {
              "color": "#fff",
              "background-color": "darkgrey"
            }
          }
        },
        {
          id: "5",
          name: "夜间",
          rules: {
            body: {
              "color": "#fff",
              "background-color": "black"
            }
          }
        }
      ],
    }
  },
  methods: {
    getBook: async function(){
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      try {
        let blob = await book.getBook(this.uuid);
        let arrayBuffer = await blob.arrayBuffer();
        this.book = await ePub(arrayBuffer, {
          restore: false,
          spreads: false,
        });
        await this.book.ready;
        await this.book.locations.generate(750 * (this.$refs.view.offsetWidth / 375));
        this.locations = this.book.locations;
        window.locations = this.locations;
        this.navigation = this.book.navigation;
        this.toc = this.navigation.toc;
        this.$emit("toc", this.toc);
        this.spine = this.book.locations.spine;
        this.$emit("pageStatusChanged", this.currentPage, this.spine.length);
        // console.log(this.spine, this.book.navigation, this.book);
        this.loadBook();
        this.$emit("ready");
        loading.close();
      } catch (err){
        console.log(err);
        loading.close();
      }
    },
    loadBook(){
      document.reader = this;
      let ref = this.$refs.view;
      this.rendition = this.book.renderTo(this.$refs.view,  {
        width: ref.clientWidth,
        height: ref.clientHeight,
        flow: "paginated",
        manager: "continuous",
        script: "/inject/epub.js",
      });
      this.themes = this.rendition.themes;
      for (let theme of this.themesOptions){
        this.themes.register(theme.id, theme.rules);
      }
      this.rendition.display();
      this.rendition.on("rendered", section => {
        this.currentSection = section.index;
      })
      this.rendition.on("locationChanged", info => {
        this.currentPage = info.index + 1;
        this.$emit("locationChange", info);
        this.currentLoc = info.start;
        this.$emit("percentage", this.locations.percentageFromCfi(this.currentLoc).toFixed(2));
      });
    },
    nextPage(){
      this.rendition.next();
    },
    prevPage(){
      this.rendition.prev();
    },
    selectSection(sectionHref){
      this.rendition.display(sectionHref);
    },
    lastSection(){
      if (this.currentSection <= 0){
        return;
      }
      const section = this.spine.items[this.currentSection - 1];
      this.rendition.display(section.href);
      this.$emit("changePage", this.currentSection);
      this.currentSection --;
    },
    nextSection(){
      if (this.currentSection + 1 > this.spine.length){
        return;
      }
      const section = this.spine.items[this.currentSection + 1];
      if (section !== undefined) {
        this.rendition.display(section.href);
        this.currentSection ++;
        this.$emit("changePage", this.currentSection);
      }
    },
    changeProcess(process) {
      const location = process > 0 ? this.locations.cfiFromPercentage(process) : 0;
      this.rendition.display(location);
    },
    setBgColor(bgColor){
      if (!this.book){
        return;
      }
      const theme = this.themesOptions.find(value => value.rules.body["background-color"] === bgColor);
      this.themes.select(theme.id);
    },
    setFontStyle(fontFamily, fontSize){
      this.themes.fontSize(fontSize + "px");
    },
    saveProgress(){
      book.saveTxtProgress(this.uuid, this.currentLoc);
    },
    loadProgress(data){
      if (data){
        this.currentLoc = eval(data);
        if (this.currentLoc) {
          this.rendition.display(this.currentLoc);
        }
      }
    }
  },
  created() {
    this.getBook();
    this.$on("toggle", () => {
      this.$parent.$emit("toggleHeaderAndFooter");
    })
  }
}
</script>

<style scoped lang="less">
  #reader-viewer{
    height: 82vh;
  }
</style>