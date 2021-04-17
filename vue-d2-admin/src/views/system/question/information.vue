<template>
  <d2-container type="full">
    <transition name="slide"
                appear
                enter-active-class="animate__animated animate__bounceIn animate__faster"
                leave-active-class="animate__animated animate__bounceOut animate__faster">
      <div class="book-filter" v-show="showFilter">
        <el-form label-width="auto" size="mini" inline :model="form">
          <el-form-item label="关键词">
            <el-input type="text" v-model="form.keyword" placeholder="请输入关键词(模糊搜索)" />
          </el-form-item>
          <el-button-group>
            <el-button size="mini" type="primary" @click.prevent="handleFilter">筛选</el-button>
            <el-button size="mini" @click.prevent="handleReset">重置</el-button>
          </el-button-group>
        </el-form>
      </div>
    </transition>
    <toggle-btn :toggle-prop="showFilter" :toggle="() => showFilter = !showFilter" />
    <div class="table">
      <el-button-group style="padding: 5px">
        <el-button
          :disabled="disableMultiBtn"
          type="danger"
          size="mini"
          @click.prevent="handleDelete($refs.feedbackTable.selection.map(val => val.id))">
          删除
        </el-button>
        <el-button
          :disabled="disableMultiBtn"
          @click.prevent="handleStatus($refs.feedbackTable.selection.map(val => val.id))"
          type="primary"
          size="mini">
          状态
        </el-button>
      </el-button-group>
      <el-table :data="feedbacks"
                border
                size="small"
                height="95%"
                fit
                ref="feedbackTable"
                show-header
                @selection-change="handleSelectionChange"
                current-row-key="id"
                v-loading="loading"
                highlight-current-row>
        <el-table-column type="selection" align="center" width="50" />
        <el-table-column type="index" label="序号" width="50" sortable resizable align="center" />
        <el-table-column prop="title" label="标题" width="150" resizable align="center" />
        <el-table-column prop="description" label="描述" width="300" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.description, '描述')">
              <span v-if="!scope.row.description">无</span>
              <span v-else-if="scope.row.description.length > 20">{{ scope.row.description.substring(0, 20) + "..." }}</span>
              <span v-else>{{ scope.row.description }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="information" label="联系方式" width="200" resizable align="center" />
        <el-table-column prop="putDate" label="反馈时间" width="200" resizable align="center" />
        <el-table-column prop="contactType" label="联系方式类型" width="150" resizable align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.contactType === 0">邮箱</el-tag>
            <el-tag type="success" v-else-if="scope.row.contactType === 1">手机号</el-tag>
            <el-tag type="danger" v-else>未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" resizable align="center">
          <template slot-scope="scope">
            <el-tag type="info" v-if="scope.row.status === 0">待处理</el-tag>
            <el-tag v-else-if="scope.row.status === 1">处理中</el-tag>
            <el-tag type="success" v-else>已处理</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" width="150" resizable align="center" label="操作">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-delete" size="mini" @click.prevent="handleDelete([scope.row.id])">删除</el-button>
            <el-button type="text" icon="el-icon-edit" size="mini" @click.prevent="handleStatus([scope.row.id])">设置</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin: 20px 0; float: right">
      <el-pagination
        :hide-on-single-page="false"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="form.page"
        :page-sizes="pageSizes"
        :page-size="form.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="showSetStatusDialog" destroy-on-close title="设置状态" v-dialog-drag>
      <div v-if="showSetStatusDialog" style="text-align: center">
        <el-button type="info" @click="handleSubmitStatus(0)">未处理</el-button>
        <el-button type="primary" @click="handleSubmitStatus(1)">处理中</el-button>
        <el-button type="success" @click="handleSubmitStatus(2)">已处理</el-button>
      </div>
    </el-dialog>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager"
export default {
  name: "information",
  components: {
    "toggle-btn": () => import("@/components/ToggleBtn")
  },
  data(){
    return {
      showFilter: true,
      loading: false,
      feedbacks: [],
      form: {
        page: 1,
        limit: 10,
        keyword: null,
      },
      pageSizes: [10, 20, 30, 50, 100, 200],
      total: 500,
      disableMultiBtn: true,
      showSetStatusDialog: false,
      ids: [],
    }
  },
  methods: {
    listFeedback(){
      this.loading = true;
      manager.listFeedback(this.form).then(({data: res}) => {
        this.total = res.data.total;
        this.feedbacks = res.data.records;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取反馈列表发生错误! <br />原因: ${err}`
        });
      });
    },
    handleSelectionChange(selection){
      this.disableMultiBtn = !(selection && selection.length > 0);
    },
    handleSizeChange(val){
      this.form.limit = val;
      this.listFeedback();
    },
    handleCurrentChange(val){
      this.form.page = val;
      this.listFeedback();
    },
    handleReset(){
      this.form = {
        page: 1,
        limit: 10,
        keyword: null,
      };
      this.listFeedback();
    },
    handleFilter(){
      this.form.page = 1;
      this.listFeedback();
    },
    handleDelete(ids){
      const msg = ids.length > 1 ? `确定要删除这${ids.length}条记录吗？` : "确定要删除该记录吗？";
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        return manager.deleteFeedback(ids);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除成功, 共删除了${res.data}条数据`,
            type: "success"
          });
          this.listFeedback();
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
            message: `删除反馈数据发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    handleStatus(ids){
      this.showSetStatusDialog = true;
      this.ids = ids;
    },
    handleSubmitStatus(statusCode){
      manager.setFeedbackStatus(this.ids, statusCode).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `修改成功`,
            type: "success"
          });
          this.listFeedback();
          this.ids = [];
          this.showSetStatusDialog = false;
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
          message: `设置反馈状态发生错误! <br />原因: ${err}`
        });
      });
    },
    showAll(message, title="提示"){
      if (!message){
        return;
      }
      this.$alert(`<div style="word-break: break-all">${message}</div>`, title, {
        iconClass: "el-icon-info",
        dangerouslyUseHTMLString: true,
      });
    },
  },
  created(){
    this.listFeedback();
  }
}
</script>

<style scoped lang="less">
  .table{
    width: 100%;
    height: 58vh;
    margin-top: 20px;
    background-color: white;
  }
</style>
