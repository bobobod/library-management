<template>
  <div>
    <el-button
      icon="el-icon-plus"
      @click="
        {
          title = '添加图书';
          dialogVisible = true;
          flag = true;
        }
      "
      >添加图书</el-button
    >
    <el-button
      icon="el-icon-search"
      @click="
        {
          dialogVisible1 = true;
        }
      "
      >查询图书</el-button
    >
    <el-upload
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="onSuccess"
      :on-error="onError"
      :disabled="importDataDisabled"
      style="display: inline-flex;margin-left:8px;margin-right: 8px"
      action="/book/import"
    >
      <el-button :disabled="importDataDisabled" :icon="importDataBtnIcon">
        {{ importDataBtnText }}
      </el-button>
    </el-upload>
    <el-button @click="exportData" icon="el-icon-download">
      导出数据
    </el-button>
    <el-divider></el-divider>

    <el-dialog title="查询图书信息" :visible.sync="dialogVisible1" width="30%">
      <el-tag>图书名称： </el-tag>
      <el-input v-model="query.name" placeholder="请输入查询的图书名。。。" />
      <el-tag>作者： </el-tag>
      <el-input v-model="query.writer" placeholder="请输入查询的作者名。。。" />
      <el-tag>图书类别： </el-tag>
      <el-select v-model="query.cid" style="width: 200px" placeholder="请选择">
        <el-option
          v-for="item in cats"
          :key="item.cid"
          :label="item.cname"
          :value="item.cid"
        >
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="queryBook">确 定</el-button>
      </span>
    </el-dialog>
    <el-table
      :data="bookLists"
      border
      style="width: 100%"
      v-loading="loading1"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column prop="bname" label="图书名" width="100">
      </el-table-column>
      <el-table-column
        label="图书类型"
        width="100"
        prop="cid"
        :formatter="myFormatter"
      >
      </el-table-column>

      <el-table-column prop="bookId" label="图书卡号" width="100">
      </el-table-column>
      <el-table-column prop="writer" label="作者" width="100">
      </el-table-column>
      <el-table-column prop="press" label="出版社" width="100">
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100"> </el-table-column>
      <el-table-column prop="issue" label="出版日期" width="100">
      </el-table-column>
      <el-table-column prop="position" label="图书位置" width="100">
      </el-table-column>
      <el-table-column label="图书状态" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 0">正常</span>
          <span v-else>出借</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200px">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-edit"
            @click="toEdit(scope.row.bid)"
            >编辑</el-button
          >
          <el-button
            type="warning"
            size="small"
            icon="el-icon-delete-solid"
            @click="deleteBook(scope.row.bid)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      style="text-align: right"
      v-if="total > 0"
      @current-change="pageChange"
      :page-size="size"
      :total="total"
    >
    </el-pagination>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%">
      <el-form :model="book" label-position="right" label-width="200px">
        <el-form-item
          label="图书名称"
          :label-width="formLabelWidth"
          hide-required-asterisk
        >
          <el-input
            v-model="book.bname"
            autocomplete="off"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="图书卡号"
          :label-width="formLabelWidth"
          prop="bookId"
        >
          <el-input
            :disabled="check"
            v-model.number="book.bookId"
            style="width: 200px"
            placeholder="请放入图书标签纸"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="图书类别" :label-width="formLabelWidth">
          <el-select
            v-model="book.cid"
            style="width: 200px"
            placeholder="请选择"
          >
            <el-option value="">请选择</el-option>
            <el-option
              v-for="item in cats"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图书作者" :label-width="formLabelWidth">
          <el-input
            v-model="book.writer"
            style="width: 200px"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版社" :label-width="formLabelWidth">
          <el-input
            v-model="book.press"
            style="width: 200px"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="图书价格" :label-width="formLabelWidth">
          <el-input
            v-model="book.price"
            style="width: 200px"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版日期" :label-width="formLabelWidth">
          <el-date-picker
            v-model="book.issue"
            type="date"
            style="width: 200px"
            placeholder="选择出版日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="图书位置" :label-width="formLabelWidth">
          <el-input
            v-model="book.position"
            style="width: 200px"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button
          @click="
            {
              dialogVisible = false;
              initBook();
            }
          "
          >取 消</el-button
        >
        <el-button type="primary" :loading="loading" @click="operate">{{
          loading ? "提交中 ..." : "确 定"
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import util from "../../utils/utils";
export default {
  data() {
    return {
      query: {
        name: "",
        writer: "",
        cid: ""
      },
      title: "",
      flag: true,
      bookLists: [],
      cats: [],
      dialogVisible1: false,
      formLabelWidth: "100px",
      book: {
        bid: "",
        bookId: "",
        bName: "",
        cid: "",
        writer: "",
        press: "",
        price: "",
        issue: "",
        status: 0
      },
      dialogVisible: false,
      loading: false,
      loading1: false,
      current: 1,
      size: 4,
      total: 0,
      check: false,
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false
    };
  },
  mounted() {
    this.loading1 = true;
    this.getBookLists();
    this.getLeafCats();
  },
  methods: {
    onError(err, file, fileList) {
      console.log(err, file, fileList);

      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.$notify({
        title: "失败",
        message: "导入图书失败",
        type: "error"
      });
    },
    onSuccess(response, file, fileList) {
      console.log(response, file, fileList);

      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.$notify({
        title: "成功",
        message: "导入图书成功",
        type: "success"
      });
      this.getBookLists();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    exportData() {
      window.open("/book/export", "_parent");
    },
    queryBook() {
      this.getBookLists();
      this.dialogVisible1 = false;
      this.query = {
        name: "",
        writer: "",
        cid: ""
      };
    },
    toEdit(id) {
      this.title = "修改图书";
      this.check = true;
      this.flag = false;
      this.dialogVisible = true;
      this.book = this.bookLists.find(item => item.bid == id);
    },
    doEditBook() {
      let success = res => {
        console.log(res.data);
        this.loading = false;
        if (res.data.code == 200 && res) {
          this.getBookLists();
          console.log("修改图书成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
        } else {
          console.log("修改图书失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
        this.initBook();

        this.dialogVisible = false;
      };

      util.axiosMethod({
        url: "/book",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.book),
        callback: success
      });
    },
    initBook() {
      this.check = false;
      this.book = {
        bid: "",
        bookId: "",
        bName: "",
        cid: "",
        writer: "",
        press: "",
        price: "",
        issue: "",
        position: "",
        status: 0
      };
    },
    myFormatter(row, column) {
      let id = row[column.property];
      let cat = this.cats.find(item => item.cid === id);
      if (cat == undefined) return "";
      return cat.cname;
    },
    operate() {
      if (this.flag == true) this.doAddBook();
      else this.doEditBook();
    },
    pageChange(current) {
      console.log(current);
      this.current = current;
      this.getBookLists();
    },
    doAddBook() {
      this.loading = true;
      let success = res => {
        console.log(res.data);
        this.loading = false;
        if (res.data.code == 200 && res) {
          this.getBookLists();
          console.log("添加图书成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
        } else {
          console.log("添加图书失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
        this.initBook();

        this.dialogVisible = false;
      };

      util.axiosMethod({
        url: "/book",
        method: "post",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.book),
        callback: success
      });
    },
    getLeafCats() {
      let success = res => {
        if (res && res.status === 200) {
          this.cats = res.data.data;
        } else {
          console.log("查询失败");
        }
      };
      util.axiosMethod({
        url: "/category/leaf",
        method: "get",

        callback: success
      });
    },
    getBookLists() {
      let success = res => {
        if (res && res.data.code === 200) {
          this.bookLists = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          if (this.current == 1) {
            this.total = 0;
            console.log("查询失败");
          } else {
            this.current = this.current - 1;
            this.getBookLists();
          }
        }
        this.loading1 = false;
      };
      util.axiosMethod({
        url: "/book",
        method: "get",
        params: {
          current: this.current,
          size: this.size,
          bName: this.query.name,
          writer: this.query.writer,
          cId: this.query.cid
        },
        callback: success
      });
    },

    deleteBook(id) {
      let success = res => {
        console.log(res.data);

        if (res.data.code == 200 && res) {
          this.getBookLists();
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      this.$confirm("删除图书信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "danger"
      })
        .then(() => {
          util.axiosMethod({
            url: "/book",
            method: "delete",
            params: {
              id: id
            },
            callback: success
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作"
          });
        });
    }
  }
};
</script>

<style></style>
