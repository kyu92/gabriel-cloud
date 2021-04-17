<template>
  <div class="book-box">
    <div class="last-open">
      <fieldset v-if="lastReadStr">
        <legend>{{ lastReadStr }}</legend>
      </fieldset>
    </div>
    <div @mouseenter="showControl=true" @mouseleave="showControl=false" @click.prevent="toRead">
      <transition
          name="slide"
          appear
          enter-active-class="animate__animated animate__fadeIn animate__faster"
          leave-active-class="animate__animated animate__fadeOut animate__faster">
        <div class="control" v-show="showControl">
          <i class="el-icon-delete" @click.prevent="remove(book, $event)" />
          <i class="el-icon-download" @click.prevent="download(book.uuid, $event)" />
        </div>
      </transition>
      <div class="display">
        <el-image :src="coverSrc" style="width: 100%; height: 100%">
          <div slot="error" class="image-slot no-img-slot" style="height: 100%">
            <span class="name">{{book.name}}.{{book.type}}</span>
          </div>
        </el-image>
      </div>
      <div class="title" :style="{fontSize}">{{displayName}}</div>
    </div>
  </div>
</template>

<script>
import book from "@/api/book";

export default {
  name: "BookBox",
  props: ["book", "remove"],
  data(){
    return {
      showControl: false,
      coverSrc: undefined,
    }
  },
  methods: {
    toRead(){
      this.$router.push({
        path: `/view/${this.book.uuid}`,
      });
    },
    download(uuid, $event){
      $event.stopPropagation();
      book.getUrl(uuid).then(({data: res}) => {
        res.data && window.open(res.data);
      });
    }
  },
  computed: {
    fontSize(){
      const length = this.displayName.length;
      if (length <= 10){
        return "16px";
      } else if (length <= 16){
        return "14px";
      } else if (length <= 24){
        return "12px";
      } else {
        return "10px";
      }
    },
    displayName(){
      const { name } = this.book;
      if (name.length > 26) {
        return name.substring(0, 25) + "...";
      }
      return name;
    },
    lastReadStr(){
      const { lastReadDate } = this.book;
      if (!lastReadDate){
        return "";
      }
      let last = new Date(lastReadDate), now = new Date();
      let time = Math.ceil((now.getTime() - last.getTime()) / 1000);
      if (time < 60){
        return `${time}秒前`;
      } else if (time < 3600) {
        return `${Math.round(time / 60)}分钟前`
      } else if (time < 86400) {
        return `${Math.round(time / 3600)}小时前`
      } else if (time > 86400 && time <= 8640000){
        return `${Math.round(time / 86400)}天前`
      } else {
        return "";
      }
    },
    loadCover(){
      return this.$store.state.userData.getCover;
    }
  },
  watch: {
    loadCover(val){
      if (val != null){
        this.coverSrc = `/BookServiceConsumer/cover/${this.book.uuid}`;
      }
    }
  },
  created() {
    if (this.loadCover != null){
      this.coverSrc = `/BookServiceConsumer/cover/${this.book.uuid}`;
    }
  }
}
</script>

<style scoped lang="less">
  .book-box{
    .last-open{
      width: 80%;
      height: 20px;
      padding: 0 10px;
      fieldset{
        border-left: none;
        border-right: none;
        border-bottom: none;
        border-top: 1px solid rgba(100, 100, 100, 0.4);
        legend{
          color: silver;
          font-weight: bold;
          font-size: 12px;
          text-align: center;
        }
      }
    }
    @media screen and (max-width: 768px){
      width: 100px;
      height: 150px;
      margin: 0 20px 10px 0;
      .display{
        height: 130px !important;
      }
      .title{
        font-size: 1.1rem !important;
      }
      .name{
        font-size: 1.3rem !important;
      }
    }
    width: 120px;
    height: 220px;
    float: left;
    margin: 0 30px 10px 0;
    position: relative;
    .control{
      width: 100%;
      height: 26px;
      position: absolute;
      display: flex;
      left: 0;
      margin: 0;
      background-color: rgba(80,80,80,0.4);
      flex-direction: row-reverse;
      justify-content: right;
      align-items: center;
      z-index: 10;
      i{
        cursor: pointer;
        font-weight: bold;
        margin-right: 10px;
      }
    }
    .display{
      width: 100%;
      height: 180px;
      //border: 1px solid rgba(120,120,120,0.4);
      box-shadow: 1px 1px 2px 1px rgba(80,80,80,0.3);
      cursor: pointer;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .title{
      font-size: 16px;
      text-align: center;
    }
    .no-img-slot {
      width: 100%;
      height: 100%;
      .name {
        font-size: 20px;
        user-select: none;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 10px;
        width: 90%;
        height: 100%;
        word-break: break-all;
      }
    }
  }
</style>