<template>
  <div ref="chart"></div>
</template>

<script>
import { init } from "echarts/core"

export default {
  name: "Chart",
  props: ['option', "onClick"],
  data(){
    return {
      chart: undefined
    }
  },
  mounted() {
    let elem = this.$refs.chart;
    this.chart = init(elem);
    this.option && this.chart.setOption(this.option);
    this.onClick && this.chart.on("click", this.onClick);
  },
  watch: {
    option() {
      if (this.option && this.chart){
        this.chart.setOption(this.option);
      }
    }
  },
}
</script>
