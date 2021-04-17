<template>
  <d2-container type="full">
    <div class="wrapper">
      <div style="background-color: white; padding: 10px">
        <el-button-group>
          <el-button size="mini" type="warning" icon="el-icon-delete" @click.prevent="handleDelete" :disabled="disableBtn">删除</el-button>
          <el-button size="mini" type="danger" icon="el-icon-close" @click.prevent="showClearWarning = true">清空</el-button>
        </el-button-group>
      </div>
      <el-table :data="logs"
                height="100%"
                border
                size="small"
                fit
                ref="logsTable"
                show-header
                @selection-change="handleSelectionChange"
                current-row-key="id"
                v-loading="loading"
                highlight-current-row>
        <el-table-column type="selection" align="center" width="50" />
        <el-table-column type="index" label="序号" width="50" sortable resizable align="center" />
        <el-table-column prop="title" label="标题" min-width="120" resizable align="center" />
        <el-table-column prop="fullName" label="类名" min-width="200" resizable align="center" />
        <el-table-column prop="method" label="方法名" min-width="150" resizable align="center" />
        <el-table-column prop="params" label="参数" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.params), '请求参数'">
              <span v-if="scope.row.params.length < 25">{{ scope.row.params }}</span>
              <span v-else>{{ scope.row.params.substring(0, 25) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="请求地址" min-width="150" resizable align="center">
          <template slot-scope="scope">
            <span v-if="!scope.row.path">无</span>
            <span v-else>{{ scope.row.path}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="callerIp" label="IP" min-width="150" resizable align="center" />
        <el-table-column prop="callerLocation" label="地址" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.callerLocation, 'IP所在地')">
              <span v-if="!scope.row.callerLocation">无</span>
              <span v-else-if="scope.row.callerLocation.length < 25">{{ scope.row.callerLocation }}</span>
              <span v-else>{{ scope.row.callerLocation.substring(0, 25) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="coordinate" label="坐标" min-width="100" resizable align="center">
          <template slot-scope="scope">
            <span v-if="!scope.row.lat || !scope.row.lon">无</span>
            <span v-else>{{ scope.row.lat }},{{ scope.row.lon }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="callerUuid" label="调用者Uid" min-width="250" resizable align="center" />
        <el-table-column prop="userAgent" label="UA" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.userAgent, '浏览器UA')">
              <span v-if="!scope.row.userAgent">无</span>
              <span v-else-if="scope.row.userAgent.length < 25">{{ scope.row.userAgent }}</span>
              <span v-else>{{ scope.row.userAgent.substring(0, 25) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="response" label="响应内容" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.response), '接口响应内容'">
              <span v-if="!scope.row.response">无</span>
              <span v-else-if="scope.row.response.length < 25">{{ scope.row.response }}</span>
              <span v-else>{{ scope.row.response.substring(0, 25) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="success" label="是否成功" min-width="80" resizable align="center">
          <template slot-scope="scope">
            <el-checkbox :value="scope.row.success" />
          </template>
        </el-table-column>
        <el-table-column prop="exception" label="异常" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.exception), '异常信息'">
              <span v-if="!scope.row.exception">无</span>
              <span v-else-if="scope.row.exception.length < 25">{{ scope.row.exception }}</span>
              <span v-else>{{ scope.row.exception.substring(0, 25) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="callDate" label="调用时间" min-width="200" resizable align="center" />
      </el-table>
      <div style="margin: 20px 0; float: right">
        <el-pagination
          :hide-on-single-page="false"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="pageSizes"
          :page-size="limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
    <el-dialog
      :visible.sync="showClearWarning"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :center="true"
      @close="handleWarningClose"
      @open="handleWarningOpen"
      destroy-on-close title="警告">
      <div v-if="showClearWarning">
        <div style="width: 100%;display: flex; justify-content: left; align-items: center">
          <div class="icon">
            <i class="el-icon-warning"/>
          </div>
          <span style="color: red; font-weight: bold">该操作会永久清空当前系统日志，且无法恢复，确定要这么做吗？</span>
        </div>
        <div slot="footer" style="text-align: center">
          <el-button :disabled="disableConfirm" type="danger" plain @click.prevent="handleClear">
            确定<span v-if="time > 0 && disableConfirm">({{ time }})</span>
          </el-button>
          <el-button @click.prevent="showClearWarning = false" plain type="success">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager";
export default {
  name: "log",
  data(){
    return {
      logs: [],
      loading: false,
      disableBtn: true,
      page: 1,
      limit: 10,
      pageSizes: [10, 20, 30, 50, 100, 200],
      total: 100,
      showClearWarning: false,
      timer: undefined,
      disableConfirm: true,
      time: 5,
    }
  },
  methods: {
    handleWarningOpen(){
      clearInterval(this.timer);
      this.disableConfirm = true;
      this.timer = setInterval(() => {
        if (this.time > 1){
          this.time --;
        } else {
          this.disableConfirm = false;
          clearInterval(this.timer);
          this.timer = undefined;
          this.time = 5;
        }
      }, 1000);
    },
    handleWarningClose(){
      if (this.timer){
        clearInterval(this.timer);
        this.timer = undefined;
      }
      this.time = 5;
    },
    handleSelectionChange(selection){
      this.disableBtn = !(selection && selection.length > 0);
    },
    handleDelete(){
      let ids = this.$refs.logsTable.selection.map(val => val.id);
      this.$confirm("确定要删除这些记录么？", "警告", {
        type: "warning",
        cancelButtonText: "取消",
        confirmButtonText: "确定",
      }).then(() => {
        return manager.deleteLogs(ids);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除日志成功，共删除了${res.data}条数据`,
            type: "success"
          });
          this.logs = this.logs.filter(val => ids.indexOf(val.id) < 0);
          this.total -= ids.length;
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel"){
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `删除日志发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleClear(){
      manager.clearLogs().then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `清空日志成功，共清理了${res.data}条数据`,
            type: "success"
          });
          this.showClearWarning = false;
          this.getLogs();
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `清空日志发生错误! <br />原因: ${err}`
        });
      });
    },
    handleSizeChange(val){
      this.limit = val;
      this.getLogs();
    },
    handleCurrentChange(val){
      this.page = val;
      this.getLogs();
    },
    getLogs(){
      this.loading = true;
      manager.listSysLog(this.page, this.limit).then(({data: res}) => {
        this.total = res.data.total;
        this.logs = res.data.records;
        this.loading = false;
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取系统日志发生错误! <br />原因: ${err}`
        });
        this.loading = false;
      });
    },
    showAll(message, title="提示"){
      if (!message){
        return;
      }
      this.$alert(`<div style="word-break: break-all">${message}</div>`, title, {
        iconClass: "el-icon-info",
        dangerouslyUseHTMLString: true
      });
    }
  },
  created() {
    this.getLogs();
  }
}
</script>

<style scoped lang="less">
  .wrapper{
    width: 100%;
    height: 68vh;
  }
  .icon{
    width: 100px;
    height: 100px;
    display: inline-flex;
    font-size: 50px;
    color: red;
    justify-content: center;
    align-items: center;
  }
</style>
