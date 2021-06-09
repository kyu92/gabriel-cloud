<template>
  <router-view />
</template>

<script>

export default {
    name: 'app',
    methods: {
      showHitokoto(){
      }
    },
    created() {
      this.$store.dispatch("getSystemInfo");
      this.$store.dispatch("getData");
      // this.showHitokoto();
      // console.log(document.domain)
    },
}
</script>

<style lang="less">
  body{
    margin: 0;
    padding: 0;
    background-color: whitesmoke;
    &::-webkit-scrollbar{
       width: 8px;
    }
    &::-webkit-scrollbar-track{
      background-color: rgba(200,200,200,0.6);
      border: 1px solid rgba(100,100,100,0.6);
      border-radius: 5px;
    }
    &::-webkit-scrollbar-thumb{
      background: darkgray;
      border-radius: 5px;
      border: 1px solid rgba(100,100,100,0.6);
      &:hover{
        background-color: #00b8ff;
      }
    }
  }
</style>
