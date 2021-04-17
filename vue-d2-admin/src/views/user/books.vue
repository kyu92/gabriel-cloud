<template>
  <d2-container type="full">
    <transition name="slide"
                appear
                enter-active-class="animate__animated animate__bounceIn animate__faster"
                leave-active-class="animate__animated animate__bounceOut animate__faster">
      <div class="book-filter" v-show="showFilter">
        <el-form label-width="auto" size="mini" inline v-model="bookForm">
          <el-form-item label="书名">
            <el-input type="text" v-model="bookForm.name" placeholder="请输入书名(模糊搜索)" />
          </el-form-item>
          <el-form-item label="文件类型">
            <el-select v-model="bookForm.type" placeholder="请选择类型" filterable >
              <el-option v-for="item in types" :key="item" :label="item" :value="'.' + item"/>
            </el-select>
          </el-form-item>
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="createDate"
              @change="handleDateChange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-button-group>
            <el-button size="mini" type="primary" @click.prevent="listBooks">筛选</el-button>
            <el-button size="mini" @click.prevent="handleReset">重置</el-button>
          </el-button-group>
        </el-form>
      </div>
    </transition>
    <toggle-btn :toggle-prop="showFilter" :toggle="() => showFilter = !showFilter" />
    <div class="books-table">
      <el-button-group style="margin: 10px">
        <el-button size="mini" icon="el-icon-delete" type="danger" :disabled="disableMultiBtn" @click.prevent="deleteBatch">删除</el-button>
      </el-button-group>
      <el-table :data="books"
                height="100%"
                border
                size="small"
                fit
                stripe
                ref="booksTable"
                show-header
                @selection-change="handleSelectionChange"
                current-row-key="index"
                v-loading="loading"
                highlight-current-row>
        <el-table-column type="selection" align="center" width="50" />
        <el-table-column type="index" label="序号" width="50" sortable resizable align="center" />
        <el-table-column prop="name" label="书名" width="220" resizable align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer" @click.prevent="showAll(scope.row.name, '书名')">
              <span v-if="scope.row.name.length < 20">{{ scope.row.name }}</span>
              <span v-else>{{ scope.row.name.substring(0, 20) + "..." }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="所属用户" width="150" resizable align="center" />
        <el-table-column prop="type" label="类型" width="110" sortable resizable align="center" :filters="filters" :filter-method="handleFilter">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type.toLowerCase() === '.txt'" type="info">TXT</el-tag>
            <el-tag v-if="scope.row.type.toLowerCase() === '.pdf'" type="danger">PDF</el-tag>
            <el-tag v-if="scope.row.type.toLowerCase() === '.epub'">EPUB</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cover" label="封面" width="120" resizable align="center">
          <template slot-scope="scope">
            <span v-if="!scope.row.cover">无</span>
            <el-button size="mini" type="success" plain round v-else @click.prevent="showCover(scope.row)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="上传日期" width="150" resizable align="center" />
        <el-table-column prop="uuid" label="书籍唯一识别码" width="250" resizable align="center" />
        <el-table-column prop="ownerUid" label="所属用户唯一码" width="250" resizable align="center" />
        <el-table-column label="操作" width="250" resizable align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click.prevent="deleteBook(scope.row)">
              删除
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-picture-outline"
              @click.prevent="changeCover(scope.row)">
              修改封面
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-download"
              @click.prevent="download(scope.row)">
              下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row style="margin: 20px 0; float: right">
        <el-pagination
          :hide-on-single-page="false"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="bookForm.page"
          :page-sizes="pageSizes"
          :page-size="bookForm.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-row>
    </div>
    <el-dialog
      :visible.sync="showChangeCoverDialog"
      destroy-on-close
      v-dialog-drag
      title="修改封面"
      :before-close="clearCropRecord"
      :close-on-click-modal="false">
      <div v-if="showChangeCoverDialog" class="chang-box">
        <div class="cropper-wrapper">
          <img :src="coverData" v-show="coverData" ref="crop" alt=""/>
        </div>
        <div class="preview-wrapper">
          <el-button-group style="margin-left: 20px">
            <el-button @click.prevent="$refs.select.click">选择图片</el-button>
            <el-button type="primary" @click.prevent="confirmCropper" >确定裁切</el-button>
            <el-button type="success" @click.prevent="handleChangeCover">上传</el-button>
          </el-button-group>
          <div class="preview" v-if="cropperData">
            预览:
            <img :src="cropperData" alt="preview" />
          </div>
        </div>
        <input type="file" ref="select" @change="handleSelect" accept="image/*" style="display: none;"/>
      </div>
    </el-dialog>
  </d2-container>
</template>

<script>
import manager from "@/api/sys.manager"
import Cropper from "cropperjs";
import "cropperjs/dist/cropper.css"

export default {
  name: "books",
  components: {
    "toggle-btn": () => import("@/components/ToggleBtn")
  },
  data(){
    return {
      bookForm: {
        page: 1,
        limit: 10,
        name: undefined,
        type: undefined,
        createDateStart: undefined,
        createDateEnd: undefined,
      },
      createDate: null,
      loading: false,
      books: [],
      pageSizes: [10, 20, 30, 50, 100, 200],
      total: 100,
      disableMultiBtn: true,
      showFilter: true,
      types: ["txt", "epub", "pdf"],
      showChangeCoverDialog: false,
      cropper: null,
      coverData: null,
      cropperData: null,
      currentRecord: null,
    }
  },
  methods: {
    listBooks(){
      this.loading = true;
      manager.listBooks(this.bookForm).then(({data: res}) => {
        this.books = res.data.records;
        this.total = res.data.total;
        this.loading = false;
      }).catch(err => {
        console.log(err);
        this.loading = false;
      });
    },
    handleSelectionChange(selection){
      this.disableMultiBtn = !(selection && selection.length > 0);
    },
    showAll(message, title="提示"){
      if (!message){
        return;
      }
      this.$alert(message, title, {
        iconClass: "el-icon-info"
      });
    },
    handleFilter(val, row){
      return row.type === val;
    },
    showCover({cover, ownerUid}){
      if (!cover){
        return;
      }
      manager.getCover(ownerUid, cover).then(({data: res}) => {
        return window.URL.createObjectURL(res)
      }).then(res => {
        let box = document.createElement("div");
        box.classList.add("cover-display-box");
        box.onclick = () => document.body.removeChild(box);
        let image = document.createElement("img");
        image.src = res;
        image.style.height = "100vh";
        box.appendChild(image);
        image.onclick = e => e.stopPropagation();
        document.body.appendChild(box);
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取封面图片发生错误! <br />原因: ${err}`
        });
      });
    },
    handleSizeChange(val){
      this.bookForm.limit = val;
      this.listBooks();
    },
    handleCurrentChange(val){
      this.bookForm.page = val;
      this.listBooks();
    },
    handleDateChange(val){
      this.bookForm.createDateStart = val[0].toLocaleDateString().replaceAll("/", "-");
      this.bookForm.createDateEnd = val[1].toLocaleDateString().replaceAll("/", "-");
    },
    handleReset(){
      this.bookForm = {
        page: 1,
        limit: 10,
        name: undefined,
        type: undefined,
        createDateStart: undefined,
        createDateEnd: undefined,
      };
      this.createDate = null;
      this.listBooks();
    },
    deleteBook({uuid, ownerName, name}){
      this.$confirm(`确定要删除${ownerName}的${name}吗？`, "警告",{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return manager.deleteBook(uuid);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除成功`,
            type: "success"
          });
          this.listBooks();
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
            message: `删除书籍请求发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    deleteBatch(){
      let target = this.$refs.booksTable.selection.map(val => val.uuid);
      this.$confirm(`确定要删除所选的共${target.length}本书籍吗？`, "警告",{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return manager.deleteBookBatch(target);
      }).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `删除成功, 共计删除了${res.data}本书籍`,
            type: "success"
          });
          this.listBooks();
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
            message: `批量删除书籍请求发生错误! <br />原因: ${err}`
          });
        }
      });
    },
    download({uuid}){
      manager.getBookDownloadUrl(uuid).then(({data: res}) => {
        window.open(res.data);
      }).catch(err => {
        this.$notify.error({
          title: "错误",
          dangerouslyUseHTMLString: true,
          message: `获取下载链接发生错误! <br />原因: ${err}`
        });
      });
    },
    changeCover(row){
      this.showChangeCoverDialog = true;
      this.currentRecord = row;
    },
    handleSelect(event){
      let file = event.target.files[0];
      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.coverData = null;
        this.crop = true;
        this.coverData = reader.result;
        if (!this.cropper){
          this.cropper = new Cropper(this.$refs.crop, {
            viewMode: 1,
            dragMode: "none",
            initialAspectRatio: 1,
            aspectRatio: 10 / 16,
            background: true,
            autoCropArea: 0.8,
            zoomOnWheel: false,
            movable: true,
            zoomOnTouch: false,
          });
        }
        this.$refs.select.value = null;
        this.cropper.replace(this.coverData);
      }
    },
    confirmCropper(){
      this.cropperData = this.cropper.getCroppedCanvas({
        imageSmoothingQuality: "high"
      }).toDataURL("image/jpeg");
    },
    clearCropRecord(done){
      this.coverData = null;
      this.cropperData = null;
      this.cropper = null;
      this.currentRecord = null;
      done();
    },
    handleChangeCover(){
      const { uuid } = this.currentRecord;
      if (!this.cropperData){
        this.confirmCropper();
      }
      const base64 = this.cropperData.replace(/data:image\/.*?;base64,/, "");
      manager.changeCover(uuid, base64).then(({data: res}) => {
        if (res.successful){
          this.$notify({
            title: "成功",
            message: `修改成功`,
            type: "success"
          });
          this.currentRecord.cover = res.data;
          this.clearCropRecord(() => this.showChangeCoverDialog = false);
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
          message: `上传封面发生错误! <br />原因: ${err}`
        });
      });
    }
  },
  computed: {
    filters(){
      let type = new Set(this.books.map(val => val.type));
      let filters = [];
      for(let each of type){
        filters.push({text: each.substring(1), value: each});
      }
      return filters;
    },
  },
  created(){
    this.listBooks();
  }
}
</script>

<style scoped lang="less">
  .books-table{
    margin-top: 20px;
    background-color: white;
    height: 60vh;
    width: 100%;
  }
  .chang-box{
    height: 400px;
    .cropper-wrapper{
      width: 60%;
      height: 100%;
      display: inline-block;
      float: left;
      img{
        height: 100%;
      }
    }
    .preview-wrapper{
      float: right;
      display: inline-block;
      width: 40%;
      height: 100%;
      .preview{
        width: 100%;
        display: inline-flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        margin-top: 20px;
        img{
          width: 50%;
        }
      }
    }
  }
</style>

<style lang="less">
  .cover-display-box{
    user-select: none;
    display: flex;
    width: 100vw;
    height: 100vh;
    position: absolute;
    top: 0;
    left: 0;
    align-items: center;
    justify-content: center;
    background-color: rgba(100, 100, 100, 0.5);
    z-index: 9999;
  }
</style>
