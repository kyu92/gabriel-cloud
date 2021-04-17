<template>
  <d2-container type="full">
    <transition name="slide"
                appear
                enter-active-class="animate__animated animate__bounceIn animate__faster"
                leave-active-class="animate__animated animate__bounceOut animate__faster">
      <div v-show="showFilter">
        <el-form label-width="auto" v-model="banForm" size="mini" inline>
          <el-row>
            <el-form-item label="用户名">
              <el-input v-model="banForm.username" placeholder="被封禁用户名(模糊搜索)" />
            </el-form-item>
            <el-form-item label="UUID">
              <el-input v-model="banForm.uuid" placeholder="被封禁用户识别码(精确搜索)" />
            </el-form-item>
            <el-form-item label="理由">
              <el-input v-model="banForm.reason" placeholder="封禁理由(模糊搜索)" />
            </el-form-item>
            <el-form-item label="操作者">
              <el-input v-model="banForm.operator" placeholder="封禁者(UUID或用户名)" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="解封日期范围">
              <el-date-picker
                v-model="relieveDate"
                @change="handleDateChange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="是否撤销">
              <el-switch active-text="是" inactive-text="否" active-color="red" inactive-color="green" v-model="banForm.deleteFlag" />
            </el-form-item>
            <el-button-group>
              <el-button size="mini" type="primary" @click.prevent="getBanList">筛选</el-button>
              <el-button size="mini" @click.prevent="handleReset">重置</el-button>
            </el-button-group>
          </el-row>
        </el-form>
      </div>
    </transition>
    <toggle-btn :toggle="() => showFilter = !showFilter" toggle-prop="showFilter"/>
    <div class="ban-list">
      <div style="background-color: white; padding: 10px">
        <el-button-group>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click.prevent="handleMultiDelete" :disabled="disableMultiBtn">删除</el-button>
          <el-button size="mini" type="warning" icon="el-icon-close" @click.prevent="handleMultiRevoke" :disabled="disableMultiBtn">撤销</el-button>
        </el-button-group>
      </div>
      <el-table :data="banRecords"
                height="100%"
                border
                size="small"
                fit
                ref="banTable"
                show-header
                @selection-change="handleSelectionChange"
                current-row-key="uuid"
                v-loading="loading"
                highlight-current-row>
        <el-table-column type="selection" align="center" width="50" />
        <el-table-column type="index" label="序号" width="50" sortable resizable align="center" />
        <el-table-column prop="uuid" label="唯一识别码" min-width="250" resizable align="center" />
        <el-table-column prop="bannedUsername" label="用户名" min-width="120" resizable align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.bannedUsername">{{ scope.row.bannedUsername }}</span>
            <span style="color: red" v-else>用户已被删除</span>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="理由" min-width="300" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click="showReason(scope.row)">
              <span v-if="!scope.row.reason">无</span>
              <span v-else>{{ scope.row.reason.length > 20 ? scope.row.reason.substring(0, 20) + "..." : scope.row.reason }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="封禁日期" min-width="160" resizable align="center" />
        <el-table-column prop="endDate" label="解封日期" min-width="160" resizable align="center" />
        <el-table-column prop="operator" label="操作者" min-width="250" resizable align="center" />
        <el-table-column prop="operatorUsername" label="操作者用户名" min-width="100" resizable align="center" />
        <el-table-column prop="deleteFlag" label="是否撤销" min-width="120" resizable align="center">
          <template slot-scope="scope">
            <el-switch
              :value="scope.row.deleteFlag"
              active-color="red"
              inactive-color="green"
              active-text="是"
              @click.prevent="handleRevoke(scope.row)"
              inactive-text="否" />
          </template>
        </el-table-column>
        <el-table-column prop="deleteDate" label="撤销日期" min-width="160" resizable align="center" />
        <el-table-column label="操作" min-width="200" resizable align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-close"
              @click.prevent="handleRevoke(scope.row)"
              :disabled="scope.row.deleteFlag">
              撤销
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click.prevent="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; float: right">
        <el-pagination
          :hide-on-single-page="false"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="banForm.page"
          :page-sizes="pageSizes"
          :page-size="banForm.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager"
export default {
  name: "register",
  components: {
    "toggle-btn": () => import("@/components/ToggleBtn")
  },
  data(){
    return {
      showFilter: true,
      banRecords: [],
      total: 0,
      relieveDate: null,
      banForm: {
        page: 1,
        limit: 10,
        username: undefined,
        uuid: undefined,
        operator: undefined,
        reason: undefined,
        relieveDateStart: undefined,
        relieveDateEnd: undefined,
      },
      pageSizes: [10, 20, 30, 50, 100, 200],
      loading: true,
      disableMultiBtn: true,
    }
  },
  methods: {
    getBanList(){
      this.loading = true;
      manager.listBanRecord(this.banForm).then(({data: res}) => {
        this.total = res.data.total;
        this.banRecords = res.data.records;
        this.loading = false;
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取封禁记录发生错误! <br />原因: ${err}`
        });
        this.loading = false;
      });
    },
    handleDateChange(){
      this.banForm.relieveDateStart = val[0].toLocaleDateString().replaceAll("/", "-");
      this.banForm.relieveDateEnd = val[1].toLocaleDateString().replaceAll("/", "-");
    },
    handleReset(){
      this.banForm = {
        page: 1,
        limit: 10,
        username: undefined,
        uuid: undefined,
        operator: undefined,
        reason: undefined,
        relieveDateStart: undefined,
        relieveDateEnd: undefined,
      };
      this.relieveDate = null;
      this.getBanList();
    },
    showReason({reason}){
      if (reason){
        this.$alert(reason, "封禁理由", {
          confirmButtonText: "确定",
        });
      }
    },
    handleSizeChange(val){
      this.banForm.limit = val;
      this.getBanList();
    },
    handleCurrentChange(val){
      this.banForm.page = val;
      this.getBanList();
    },
    handleDelete(row){
      this.$confirm("此操作将永久删除该记录, 是否继续?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return manager.deleteBan(row.id);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除成功`,
            type: "success"
          });
          this.handleReset();
          this.getBanList();
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel") {
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `删除封禁记录发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleMultiDelete(){
      let target = this.$refs.banTable.selection.map(value => value.id);
      this.$confirm("此操作将永久删除这些记录, 是否继续?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return manager.deleteBanBatch(target);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `批量删除成功, 共${target.length}条数据`,
            type: "success"
          });
          this.handleReset();
          this.getBanList();
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel") {
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `批量删除封禁记录发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleRevoke(row){
      this.$confirm("撤销可能会解除用户的封禁状态, 是否继续?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return manager.relieve(row.id);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `撤销成功`,
            type: "success"
          });
          row.deleteFlag = true;
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel") {
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `撤销封禁记录发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleMultiRevoke(){
      let target = this.$refs.banTable.selection.filter(value => !value.deleteFlag).map(value => value.id);
      this.$confirm("撤销可能会解除用户的封禁状态, 是否继续?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return manager.relieveBatch(target);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `批量撤销成功, 共${target.length}条数据`,
            type: "success"
          });
          this.handleReset();
          this.getBanList();
        } else {
          this.$notify({
            title: "警告",
            message: res.message,
            type: "warning"
          });
        }
      }).catch(err => {
        if (err !== "cancel") {
          this.$notify.error({
            title: "错误",
            dangerouslyUseHTMLString: true,
            message: `批量撤销封禁记录发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleSelectionChange(selection){
      this.disableMultiBtn = !(selection && selection.length > 0);
    }
  },
  created() {
    this.getBanList();
  }
}
</script>

<style scoped lang="less">
  .ban-list{
    margin-top: 20px;
    width: 100%;
    height: 80%;
  }
</style>
