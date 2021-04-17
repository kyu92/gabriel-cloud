<template>
  <div class="info-box">
    <div class="icon" :style="{ color }">
      <d2-icon :name="icon"/>
    </div>
    <div class="info">
      <span>{{ title }}</span>
      <br />
      <b :style="{ color }">{{ data }}</b>
    </div>
  </div>
</template>

<script>
export default {
  name: "InfoBox",
  props: ["icon", "color", "data", "title"]
}
</script>

<style scoped lang="less">
.info-box {
  width: 15vw;
  height: 15vh;
  display: inline-block;
  margin-right: 15px;
  border-radius: 5px;
  box-shadow: 2px 1px 3px 1px rgba(100, 100, 100, 0.5);
  background-color: white;
  .info{
    width: 60%;
    height: 30%;
    font-size: 16px;
    float: right;
    margin-top: 20px;
    b{
      width: 80%;
      font-size: 30px;
      color: green;
      margin-left: 20px;
      display: inline-block;
      word-break: break-all;
    }
  }
  .icon {
    width: 40%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 40px;
    color: green;
    float: left;
  }
}
</style>
