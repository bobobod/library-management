<template>
  <section>
    <!-- 查询界面 -->
    <el-form :inline="true">
      <el-form-item label="学号">
        <el-input
          v-model="queryReader.sno"
          placeholder="学号"
          class="filter-item"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名(支持模糊查询）">
        <el-input
          v-model="queryReader.sname"
          placeholder="姓名"
          class="filter-item"
        ></el-input>
      </el-form-item>
      <el-button class="dalfBut" @click="query()">查询</el-button>
    </el-form>
    <el-divider></el-divider>
    <el-table
      :data="readerList"
      border
      style="width: 100%"
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column fixed prop="sid" label="编号" width="80">
      </el-table-column>
      <el-table-column prop="sno" label="学号" width="100"> </el-table-column>
      <el-table-column prop="sname" label="姓名" width="100"> </el-table-column>

      <el-table-column label="性别" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.sex == 0">男</span>
          <span v-else>女</span>
        </template>
      </el-table-column>

      <el-table-column prop="tel" label="电话" width="100"> </el-table-column>
      <el-table-column
        prop="cardId"
        label="读者智能卡号"
        width="120"
      ></el-table-column>
      <el-table-column prop="balance" label="余额(元)" width="100">
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 0">正常</span>
          <span v-else>挂失</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="290">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-edit"
            @click="toEdit(scope.row.sid)"
            >编辑</el-button
          >
          <el-button
            type="warning"
            size="small"
            icon="el-icon-delete-solid"
            @click="deleteReader(scope.row.sid)"
            >删除</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete-solid"
            @click="lostCard(scope.row.sid)"
            >挂失/解挂</el-button
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
    <el-dialog title="更新读者信息" :visible.sync="dialogVisible" width="30%">
      <el-form
        :model="reader"
        :inline="true"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="学号" prop="sno">
          <el-input v-model="reader.sno"></el-input> </el-form-item
        ><br />
        <el-form-item label="姓名" prop="sname">
          <el-input v-model="reader.sname"></el-input> </el-form-item
        ><br />
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="reader.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group> </el-form-item
        ><br />
        <el-form-item label="电话" prop="tel">
          <el-input v-model="reader.tel"></el-input> </el-form-item
        ><br />
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateReader">确 定</el-button>
      </span>
    </el-dialog>
  </section>
</template>

<script>
import util from "../../utils/utils";
export default {
  methods: {
    toEdit(id) {
      this.reader = this.readerList.find(item => item.sid === id);
      this.dialogVisible = true;
    },
    // 修改读者信息
    updateReader() {
      let success = res => {
        console.log(res.data);
        if (res.data.data === 1) {
          console.log("修改读者成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.getReaderData();
        } else {
          console.log("修改读者失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          console.log("修改失败");
        }
      };

      util.axiosMethod({
        url: "/reader",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.reader),
        callback: success
      });

      this.dialogVisible = false;
    },
    // 分页改变
    pageChange(current) {
      console.log(current);
      this.current = current;
      this.getReaderData();
    },

    // 获取读者信息
    getReaderData() {
      this.loading = true;
      let success = res => {
        console.log(res.data);
        console.log(res.data.data.records.length);
        this.loading = false;
        if (res.data.code == 200 && res) {
          this.total = res.data.data.total;
          this.readerList = res.data.data.records;
        } else {
          if (this.current == 1) {
            this.readerList = [];
            this.total = 0;
          } else {
            this.current = this.current - 1;
            this.getReaderData();
          }
        }
      };
      util.axiosMethod({
        url: "/reader",
        method: "get",
        params: {
          current: this.current,
          size: this.size,
          sNo: this.queryReader.sno,
          sName: this.queryReader.sname
        },
        callback: success
      });
    },
    // 挂失和解挂操作
    lostCard(id) {
      let that = this;
      console.log("lostcart" + id);
      let reader = that.readerList.find(item => item.sid === id);
      console.log(that.readerList);
      console.log("lost" + reader);
      if (reader !== undefined) {
        if (reader.status === 1) reader.status = 0;
        else reader.status = 1;
      }
      let success = res => {
        console.log(res.data);

        if (res.data.data === 1) {
          that.getReaderData();
          this.$notify({
            title: "成功",
            message: "挂失/解挂读者成功",
            type: "success"
          });
        } else {
          this.$notify({
            title: "失败",
            message: "挂失/解挂读者失败",
            type: "error"
          });
        }
      };
      this.$confirm("挂失（解挂）读者智卡卡, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "danger"
      })
        .then(() => {
          if (reader.sid !== "") {
            util.axiosMethod({
              url: "/reader",
              method: "put",
              headers: { "Content-Type": "application/json;charset=UTF-8" },
              data: JSON.stringify(reader),
              callback: success
            });
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作"
          });
        });
    },
    // 根据条件模糊查询
    query() {
      this.getReaderData();
    },
    //  删除读者信息
    deleteReader(id) {
      let success = res => {
        console.log(res.data);

        if (res.data.data === 1) {
          this.getReaderData();
          this.$notify({
            title: "成功",
            message: "删除读者成功",
            type: "success"
          });
        } else {
          this.$notify({
            title: "失败",
            message: "删除读者失败",
            type: "error"
          });
        }
      };
      this.$confirm("删除读者信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "danger"
      })
        .then(() => {
          util.axiosMethod({
            url: "/reader",
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
  },
  mounted() {
    let that = this;
    that.getReaderData();
  },
  data() {
    return {
      readerList: [],
      current: 1,
      size: 4,
      total: 0,
      loading: false,
      queryReader: {
        sno: "",
        sname: ""
      },
      reader: {
        sid: "",
        sno: "",
        sname: "",
        spassword: "",
        cardId: "",
        sex: "",
        tel: "",
        balance: "",
        status: ""
      },
      dialogVisible: false
    };
  }
};
</script>

<style scoped></style>
