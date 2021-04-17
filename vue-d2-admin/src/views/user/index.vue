<template>
  <d2-container type="full">
    <transition name="slide"
                appear
                enter-active-class="animate__animated animate__bounceIn animate__faster"
                leave-active-class="animate__animated animate__bounceOut animate__faster">
      <div class="filter-form" v-show="showFilter">
        <el-form label-width="auto" v-model="userForm" size="mini" inline>
          <el-row>
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" placeholder="请输入用户名(模糊搜索)" />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nick" placeholder="请输入昵称(模糊搜索)" />
            </el-form-item>
            <el-form-item label="邮箱地址">
              <el-input v-model="userForm.email" placeholder="请输入邮箱地址(精确搜索)" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userForm.mobile" placeholder="请输入手机号(精确搜索)" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="注册日期">
              <el-date-picker
                v-model="registerDate"
                @change="handleRegDateChange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="登录日期">
              <el-date-picker
                v-model="loginDate"
                @change="handleLoginDateChange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="是否禁用">
              <el-switch active-text="是" inactive-text="否" active-color="red" inactive-color="green" v-model="userForm.banned" />
            </el-form-item>
            <el-button-group>
              <el-button size="mini" type="primary" @click.prevent="getUserData">筛选</el-button>
              <el-button size="mini" @click.prevent="handleReset">重置</el-button>
            </el-button-group>
          </el-row>
        </el-form>
      </div>
    </transition>
    <toggle-btn :toggle="toggleFilterForm" :toggle-prop="showFilter" />
    <div class="user-table">
      <el-table
        :data="userData"
        height="100%"
        border
        size="small"
        fit
        show-header
        v-loading="loading"
        highlight-current-row>
        <el-table-column type="index" label="序号" width="50" sortable resizable align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" resizable align="center" />
        <el-table-column prop="nick" label="昵称" min-width="120" resizable align="center" :formatter="(row, column, cellValue) => cellValue || '无' " />
        <el-table-column prop="uuid" label="唯一识别码" min-width="250" resizable align="center" />
        <el-table-column prop="mobile" label="手机号" width="120" resizable align="center" :formatter="(row, column, cellValue) => cellValue || '无' " />
        <el-table-column prop="email" label="邮箱地址" min-width="180" resizable align="center" />
        <el-table-column prop="registerDate" label="注册日期" min-width="180" resizable align="center" :formatter="(row, column, cellValue) => new Date(cellValue).toLocaleString()" />
        <el-table-column prop="lastLoginDate" label="上次登录日期" min-width="180" resizable align="center" :formatter="(row, column, cellValue) => new Date(cellValue).toLocaleString()" />
        <el-table-column label="封禁" min-width="120" align="center" resizable>
          <template slot-scope="scope">
            <el-switch :value="scope.row.banned" active-color="red" inactive-color="green" active-text="是" inactive-text="否" disabled />
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="300" fixed="right" resizable align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-more" @click.prevent="handleShowDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-refresh" @click.prevent="handleResetPassword(scope.row)" >重置密码</el-button>
            <el-button size="mini" type="text" :icon="scope.row.banned ? 'el-icon-unlock' : 'el-icon-lock'" @click.prevent="handleBanUser(scope.row)">
              {{ scope.row.banned ? "解封" : "封禁" }}
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click.prevent="handleDeleteUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page-container">
      <el-pagination
        :hide-on-single-page="false"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="userForm.page"
        :page-sizes="pageSizes"
        :page-size="userForm.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="showDetail" destroy-on-close title="用户详情" :before-close="done => {done(); this.currentUserDetail = undefined;}">
      <div v-if="showDetail">
        <div class="avatar">
          <el-avatar :size="100" :src="currentUserDetail.avatar ? `data:image/jpeg;base64,${currentUserDetail.avatar}` : '/image/anonymous.png'" />
        </div>
        <el-form label-width="auto" inline size="mini" >
          <el-form-item label="用户名:">
            <el-input :value="currentUserDetail.username" readonly />
          </el-form-item>
          <el-form-item label="昵称:">
            <el-input :value="currentUserDetail.nick || '无'" readonly />
          </el-form-item>
          <el-form-item label="注册日期:">
            <el-input :value="new Date(currentUserDetail.registerDate).toLocaleDateString()" readonly />
          </el-form-item>
          <el-form-item label="上次登录:">
            <el-input :value="new Date(currentUserDetail.lastLoginDate).toLocaleDateString()" readonly />
          </el-form-item>
          <div style="display: flex; align-items: center; justify-content: left">
            <div style="margin-right: 20px">权限值:</div>
            <el-progress type="dashboard" :percentage="(10 - currentUserDetail.permission) * 10" :color="colors"></el-progress>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="showBanUserDialog"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      destroy-on-close title="封禁用户">
      <div v-if="showBanUserDialog" style="padding: 0 30px">
        <el-form label-width="auto">
          <el-form-item label="用户名">
            <el-input type="text" disabled :value="currentUser.username" />
          </el-form-item>
          <el-form-item label="唯一识别码">
            <el-input type="text" disabled :value="currentUser.uuid" />
          </el-form-item>
          <el-form-item label="封禁理由">
            <el-input type="text" v-model="banForm.reason" placeholder="请输入封禁理由" />
          </el-form-item>
          <el-form-item label="解封日期">
            <el-date-picker
              v-model="banForm.endDate"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="danger" plain round @click.prevent="submitBanForm">确定</el-button>
          <el-button type="primary" plain round @click.prevent="cancelBan">取消</el-button>
        </div>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="showDeleteUserWarningTip"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      destroy-on-close title="删除用户">
      <div class="warning-box" v-if="showDeleteUserWarningTip">
        <div class="icon">
          <i class="fa fa-warning"/>
        </div>
        <div class="text">
          <p>
            <span style="color: red; font-weight: bolder;">该操作无法撤销</span>
          </p>
          <p>
            并且会删除用户<b>{{currentUser.username}}</b>以及该用户下所有的书籍
          </p>
          <p>
            若您已经知晓该操作的后果和风险，并且确定要执行该操作，请在下面输入: <br />
            <span style="color: red; font-weight: bold">"{{ confirmText }}"</span>
          </p>
          <el-input placeholder="请输入上述内容" v-model="inputText" />
        </div>
        <div style="text-align: right; margin-top: 20px">
          <el-button type="danger" @click.prevent="submitDeleteUser">确定</el-button>
          <el-button type="primary" @click.prevent="cancelDeleteUser">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager";
import user from "@/api/sys.user";
export default {
  name: "index",
  components: {
    "toggle-btn": () => import("@/components/ToggleBtn")
  },
  data(){
    return {
      showFilter: true,
      userForm: {
        username: "",
        email: "",
        mobile: "",
        nick: "",
        regDateStart: null,
        regDateEnd: null,
        loginDateStart: null,
        loginDateEnd: null,
        banned: null,
        page: 1,
        limit: 10,
      },
      loading: true,
      registerDate: null,
      loginDate: null,
      pageSizes: [10, 20, 30, 50, 100, 200],
      total: 500,
      userData: [],
      showDetail: false,
      currentUser: undefined,
      showBanUserDialog: false,
      banForm: {
        endDate: null,
        reason: null,
      },
      showDeleteUserWarningTip: false,
      confirmText: undefined,
      inputText: undefined,
      currentUserDetail: undefined,
      colors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ]
    }
  },
  methods: {
    toggleFilterForm(){
      this.showFilter = !this.showFilter;
    },
    handleReset(){
      this.userForm = {
        username: "",
        email: "",
        mobile: "",
        nick: "",
        registerDate: null,
        loginDate: null,
        banned: null,
        page: 1,
        limit: 10,
      };
      this.registerDate = null;
      this.loginDate = null;
      this.getUserData();
    },
    getUserData(){
      manager.getUserList(this.userForm).then(({data: res}) => {
        let data = res.data;
        this.total = data.total;
        this.userData = data.records;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
      });
    },
    handleSizeChange(val){
      this.userForm.limit = val;
      this.getUserData();
    },
    handleCurrentChange(val){
      this.userForm.page = val;
      this.getUserData();
    },
    handleLoginDateChange(val){
      this.userForm.loginDateStart = val[0].toLocaleDateString().replaceAll("/", "-");
      this.userForm.loginDateEnd = val[1].toLocaleDateString().replaceAll("/", "-");
    },
    handleRegDateChange(val){
      this.userForm.regDateStart = val[0].toLocaleDateString().replaceAll("/", "-");
      this.userForm.regDateEnd = val[1].toLocaleDateString().replaceAll("/", "-");
    },
    handleShowDetail(row){
      user.getUserInfo(row.uuid).then(({data: res}) => {
        if (res.successful){
          this.showDetail = true;
          this.currentUserDetail = res.data;
          console.log(this.currentUserDetail);
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
          message: `获取用户详情发生错误! <br />原因: ${err}`
        });
      });
      console.log(this.currentUser);
    },
    handleResetPassword(row){
      let uuid = row.uuid;
      this.$confirm('新密码将由后台随机生成，是否强制重置密码?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // reset
        manager.resetPassword(uuid).then(({data: res}) => {
          if (res.successful){
            this.$notify({
              title: "成功",
              message: `用户${row.username}的密码已重置，新密码为<a href="javascript:void(0);">${res.data}</a>`,
              duration: 0,
              dangerouslyUseHTMLString: true,
              type: "success"
            });
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
            message: `重置请求发生错误! <br />原因: ${err}`
          });
        });
      })
    },
    handleBanUser(row){
      let { banned } = row;
      if (banned){
        this.$confirm(`确定要解封用户${row.username}吗?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          return manager.changBanStatus(row.uuid, false, "", new Date().getTime())
        }).then(({data: res}) => {
          if (res.successful){
            this.$notify({
              title: "成功",
              message: `用户${row.username}已解封`,
              type: "success"
            });
            row.banned = false;
          } else {
            this.$notify({
              title: "警告",
              message: res.message,
              type: "warning"
            });
          }
        }).catch(err => {
          if (err && err !== "cancel"){
            this.$notify.error({
              title: "错误",
              dangerouslyUseHTMLString: true,
              message: `解封请求发生错误! <br />原因: ${err}`
            });
          }
        });
      } else {
        this.currentUser = row;
        this.showBanUserDialog = true;
      }
    },
    submitBanForm(){
      // console.log(this.banForm)
      if (!this.banForm.endDate){
        this.$notify({
          title: "警告",
          message: "请输入解封日期",
          type: "warning"
        });
        return;
      }
      manager.changBanStatus(this.currentUser.uuid, true, this.banForm.reason, this.banForm.endDate.getTime()).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `用户${this.currentUser.username}已被封禁，解封时间：${this.banForm.endDate.toLocaleDateString()}`,
            type: "success"
          });
          this.showBanUserDialog = false;
          this.currentUser.banned = true;
          this.banForm.reason = null;
          this.banForm.endDate = null;
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
          message: `封禁请求发生错误! <br />原因: ${err}`
        });
      });
    },
    cancelBan(){
      this.showBanUserDialog = false;
      this.currentUser = undefined;
      this.banForm.reason = null;
      this.banForm.endDate = null;
    },
    handleDeleteUser(row){
      this.showDeleteUserWarningTip = true;
      this.currentUser = row;
      this.confirmText = `我已经知晓本操作的风险和后果且确定要执行删除用户${this.currentUser.username}的全部数据`;
    },
    submitDeleteUser(){
      if (this.inputText !== this.confirmText){
        this.$notify.error({
          title: "错误",
          message: "输入的内容有误!"
        });
        this.showDeleteUserWarningTip = false;
        this.currentUser = undefined;
        this.inputText = null;
        return;
      }
      manager.deleteUserAndData(this.currentUser.uuid).then(({data: res}) => {
        this.$notify({
          title: "成功",
          message: `用户${this.currentUser.username}已删除`,
          type: "success"
        });
        this.showDeleteUserWarningTip = false;
        let index = this.userData.indexOf(this.currentUser);
        this.userData.splice(index, 1);
        this.currentUser = undefined;
        this.inputText = null;
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `删除请求发生错误! <br />原因: ${err}`
        });
      });
    },
    cancelDeleteUser(){
      this.showDeleteUserWarningTip = false;
      this.currentUser = undefined;
      this.inputText = null;
    }
  },
  created() {
    this.getUserData();
  }
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
  .user-table{
    margin-top: 20px;
    width: 100%;
    height: 80%;
  }
  .page-container{
    float: right;
    margin-top: 20px;
  }
  .warning-box{
    padding: 0 30px;
    .icon{
      width: 20%;
      height: 150px;
      color: red;
      user-select: none;
      font-size: 50px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      float: left;
    }
    .text{
      display: inline-block;
      width: 80%;
      user-select: none;
    }
  }
  .avatar{
    width: 150px;
    display: inline-flex;
    float: left;
    height: 200px;
    justify-content: center;
    align-items: center;
  }
</style>
