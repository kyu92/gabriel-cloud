<template>
  <div class="toggle-filter-button" @click="toggle">
    <i class="el-icon-caret-bottom" v-if="!toggleProp"></i>
    <i class="el-icon-caret-top" v-else></i>
  </div>
</template>

<script>
export default {
  name: "ToggleBtn",
  props: ["toggle", "toggleProp"],
}
</script>

<style scoped lang="less">
.toggle-filter-button{
  width: 100%;
  height: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: whitesmoke;
  font-size: 20px;
  cursor: pointer;
  user-select: none;
  border-radius: 5px;
  border: 1px solid rgba(150, 150, 150, 0.3);
  box-shadow: 1px 1px 2px 1px darkgray;
}
</style>
