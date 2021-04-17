<template>
  <div :style="style">
    <span class="long"></span>
    <span class="short"></span>
  </div>
</template>

<script>
export default {
  name: "DoubleLine",
  props: ['width'],
  data(){
    return {
      style: {
        display: "inline-block",
        width: this.width
      }
    }
  }
}
</script>

<style scoped lang="less">
  .long{
    display: inline-block;
    width: 100%;
    border-bottom: 2px solid #00b8ff;
  }
  .short{
    display: inline-block;
    width: 60%;
    border-bottom: 2px solid #00b8ff;
  }
</style>