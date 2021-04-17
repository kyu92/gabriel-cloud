<template>

</template>

<script>
import localeMixin from '@/locales/mixin.js'
import jwt from "../../../../../vue-ui/src/common/jwt";
export default {
  mixins: [
    localeMixin
  ],
  data () {
    return {

    }
  },
  mounted () {
    window.addEventListener("message", this.messageListener);
  },
  beforeDestroy () {
    window.removeEventListener("message", this.messageListener);
  },
  methods: {
    messageListener(e){
      if (e.origin.indexOf("kyu92.top") > 0){
        switch (e.data.target){
          case "login": {
            let token = e.data.data;
            if (this.checkToken(token)){
              window.open(location.origin);
            }
            break
          }
          case "logout": {
            this.$store.dispatch("d2admin/account/logout");
            window.parent.postMessage({target: "logoutFinish"}, "*");
            break;
          }
        }
      }
    },
    checkToken(token){
      jwt.init(token);
      if (!jwt.isExpired()){
        jwt.saveToken(token);
        return true;
      } else {
        return false;
      }
    }
  }
}
</script>

<style lang="less">

</style>
