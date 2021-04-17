<template>
  <div class="footer">
    <el-button class="footer-button" size="mini" type="text" icon="el-icon-upload2" @click.prevent="toTop">回到顶部</el-button>
  </div>
</template>

<script>
export default {
  name: "FooterBar",
  props: ["toTop"]
}
</script>

<style scoped lang="less">
  .footer{
    height: 5vh;
    width: 100%;
    position: absolute;
    background-color: white;
    left: 0;
    bottom: 0;
    border-top: 1px solid darkgray;
    display: flex;
    justify-content: left;
    align-items: center;
    .footer-button{
      margin-left: 30px;
      font-size: 12px;
    }
  }
</style>