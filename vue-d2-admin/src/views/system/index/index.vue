<template>
  <d2-container>
    <!--    首页信息    -->
    <div class="statistics">
      <info-box color="green" title="用户数:" :data="userCount" icon="address-card" />
      <info-box color="red" title="书籍数:" :data="bookCount" icon="book" />
      <chart class="reg-chart" :option="regOption"/>
    </div>
    <div class="service">
      <div class="title">
        <a href="http://localhost:81/nacos" target="_blank">
          <d2-icon name="institution"/>
          微服务列表
        </a>
      </div>
      <chart class="service-chart" :option="serviceOption" :on-click="handleClick"/>
    </div>
    <el-drawer
      :visible.sync="showServiceDetail"
      destroy-on-close
      size="40%"
      direction="rtl">
      <div slot="title">服务模块实例: {{ currentService }}</div>
      <div v-if="showServiceDetail">
        <el-collapse accordion>
          <el-collapse-item
            v-for="(item, index) in currentInstanceList"
            :key="item.host + item.port"
            :title="currentService + index"
            :name="item.host + item.port">
            <ul>
              <li>实例Id: {{ item.instanceId ? item.instanceId : "空" }}</li>
              <li>地址: {{ item.host }}</li>
              <li>端口: {{ item.port }}</li>
              <li>协议: {{ item.scheme ? item.scheme : "空" }}</li>
              <li>加密: {{ item.secure ? "是" : "否" }}</li>
            </ul>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-drawer>
  </d2-container>
</template>

<script>
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { LineChart, BarChart } from "echarts/charts";
import {
  GridComponent,
  TitleComponent,
  PolarComponent,
  LegendComponent
} from "echarts/components"
import manager from "@/api/sys.manager";
use([
  CanvasRenderer,
  LineChart,
  BarChart,
  GridComponent,
  TitleComponent,
  PolarComponent,
  LegendComponent
]);

export default {
  name: "index",
  data(){
    return{
      userCount: 0,
      bookCount: 0,
      regData: [0, 0, 0, 0, 0, 0, 0],
      serviceNameList: new Set(),
      serviceHostList: new Set(),
      serviceData: {},
      serviceSeries: [],
      showServiceDetail: false,
      currentService: undefined,
      currentInstanceList: [],
    }
  },
  methods: {
    getRegData(){
      manager.getIndexInfo().then(({data: register}) => {
        this.userCount = register.data.userCount;
        this.bookCount = register.data.bookCount;
        this.regOption = register.data.registerCount;
      }).catch(err => {
        console.log(err);
      });
    },
    getServiceData(){
      manager.getCloudModules().then(({ data: service }) => {
        const data = service.data;
        this.serviceData = data;
        for (let serviceName of Object.keys(data)){
          this.serviceNameList.add(serviceName);
          data[serviceName].forEach(val => this.serviceHostList.add(val.host));
        }
        this.serviceSeries = this.genSeries();
        // console.log(this.serviceData, this.serviceNameList, this.serviceHostList, this.serviceOption)
      }).catch(err => {
        console.log(err);
      });
    },
    genSeries(){
      let series = [];
      let hostMap = {};
      for (let serviceName of this.serviceNameList){
        if (!hostMap.hasOwnProperty(serviceName)){
          hostMap[serviceName] = {};
        }
        for (let service of this.serviceData[serviceName]){
          if (hostMap[serviceName].hasOwnProperty(service.host)){
            hostMap[serviceName][service.host] ++;
          } else {
            hostMap[serviceName][service.host] = 1;
          }
        }
      }
      for (let serviceHost of this.serviceHostList){
        let instance = [];
        for (let serviceName of this.serviceNameList){
          instance.push(hostMap[serviceName][serviceHost] || 0);
        }
        series.push({
          type: 'bar',
          data: instance,
          coordinateSystem: 'polar',
          name: serviceHost,
          stack: 'a',
          emphasis: {
            focus: 'series'
          },
        });
      }
      return series;
    },
    handleClick(e){
      // console.log("click", e, this.serviceData);
      this.showServiceDetail = true;
      this.currentService = e.name;
      this.currentInstanceList = this.serviceData[e.name];
    }
  },
  components: {
    "info-box": () => import("@/components/InfoBox"),
    "chart": () => import("@/components/Chart"),
  },
  computed: {
    regOption: {
      get(){
        return {
          title: {
            text: "最近一周注册量",
            left: "center",
          },
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: this.regData,
            type: 'line',
            smooth: true,
            label: {
              show: true,
            }
          }]
        }
      },
      set(val){
        this.regData = val;
      }
    },
    serviceOption: {
      get(){
        return {
          angleAxis: {
            type: 'category',
            data: [...this.serviceNameList],
            axisLabel: {
              show: true,
              fontSize: 10
            },
          },
          radiusAxis: {
            maxInterval: 1,
            splitLine: {
              show: true,
              lineStyle: {
                color: "#a0a0a0"
              }
            }
          },
          polar: {
          },
          series: this.serviceSeries,
          legend: {
            show: true,
            data: [...this.serviceHostList]
          }
        }
      },
    }
  },
  created(){
    this.getRegData();
    this.getServiceData();
  }
}
</script>

<style lang="less" scoped>
  .statistics {
    width: 50%;
    display: inline-block;
    float: left;
    .reg-chart{
      margin-top: 20px;
      width: 100%;
      height: 60vh;
    }
  }
  .service{
    width: 50%;
    display: inline-block;
    height: 100%;
    float: right;
    user-select: none;
    .title{
      width: 100%;
      height: 10%;
      a{
        color: #0fb2fc;
        font-size: 20px;
      }
    }
    .service-chart{
      height: 90%;
      width: 100%;
    }
  }
</style>
