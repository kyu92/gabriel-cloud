<template>
  <div class="nav-bar">
    <div class="logo">Gabriel</div>
    <ul class="bar-button" v-if="buttons.length > 0">
      <li v-for="(item, index) in buttons" :name="item.name" :key="index">
        <router-link :to="item.path" v-if="item.path.charAt(0) === '/'">{{ item.label }}</router-link>
        <a :href="item.path" v-else>{{ item.label }}</a>
      </li>
    </ul>
    <user-display class="user-info" />
  </div>
</template>

<script>
  export default {
    name: "NavBar",
    data(){
      return {
        activeName: "index",
      }
    },
    components: {
      "user-display": () => import("@/components/UserDisplay")
    },
    props: ["buttons"],
    methods: {

    },
  }
</script>

<style scoped lang="less">
  .button-style(@size: 1.3rem){
    color: #00bcd4;
    font-weight: bold;
    cursor: pointer;
    user-select: none;
    font-family: 楷体, serif;
    font-size: @size;
  }
  @import "../style/xs/navbar";
  @import "../style/sm/navbar";
  .nav-bar{
    height: 8vh;
    width: 100%;
    z-index: 999;
    background-color: white;
    box-shadow: 3px 1px 6px #8e8e8e;
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
    align-content: center;
    .logo{
      color: #0fb2fc;
      font-size: 2.2rem;
      margin: 0 30px;
      font-weight: bold;
      user-select: none;
      cursor: pointer;
      transition: color 800ms;
      &:hover{
        color: #00FFFF;
        transition: color 800ms;
      }
    }
    .bar-button{
      display: flex;
      list-style: none;
      li{
        margin: 0 20px;
        &:first-child{
          margin-left: 0;
        }
        a{
          .button-style();
          text-decoration: none;
        }
      }
    }
    .user-info{
      position: absolute;
      right: 0;
      .button-style();
    }
  }
</style>