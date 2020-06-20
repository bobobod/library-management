<template>
  <section>
    <!-- 查询界面 -->

    <el-form :inline="true">
      <el-form-item label="学号">
        <el-input
          v-model="borrow.sno"
          placeholder="学号"
          class="filter-item"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名(支持模糊查询）">
        <el-input
          v-model="borrow.sname"
          placeholder="姓名"
          class="filter-item"
        ></el-input>
      </el-form-item>
      <br />
      <el-form-item>
        <el-radio-group v-model="borrow.flag">
          <el-radio :label="2">查询所有</el-radio>
          <el-radio :label="3">查询已借</el-radio>
          <el-radio :label="1">查询已还</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <el-button class="dalfBut" icon="el-icon-search" @click="query()"
      >查询所有</el-button
    >
    <el-button class="dalfBut" icon="el-icon-search" @click="queryOverdue()"
      >逾期查询</el-button
    >
    <el-divider></el-divider>

    <el-table
      :data="records"
      border
      style="width: 100%"
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column prop="bname" label="图书名" width="90">
      </el-table-column>
      <el-table-column prop="bookId" label="图书卡号" width="90">
      </el-table-column>
      <el-table-column
        prop="cid"
        :formatter="myFormatter"
        label="图书类型"
        width="90"
      >
      </el-table-column>
      <el-table-column prop="sno" label="读者学号" width="90">
      </el-table-column>
      <el-table-column prop="sname" label="读者姓名" width="90">
      </el-table-column>
      <el-table-column prop="cardId" label="读者卡号" width="90">
      </el-table-column>
      <el-table-column prop="borrowDate" label="借阅时间" width="90">
      </el-table-column>
      <el-table-column prop="expire" label="应还时间" width="90">
      </el-table-column>

      <el-table-column label="逾期天数（天）" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.overdue === null">未归还</span>
          <span v-else-if="scope.row.overdue === 0">未逾期</span>
          <span style="color:red;" v-else>{{ scope.row.overdue }} </span>
        </template>
      </el-table-column>
      <el-table-column label="归还日期" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.returnDate === null">未归还</span>
          <span v-else>{{ scope.row.returnDate }} </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">借出</span>
          <span v-else> 已还 </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="90">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="small"
            icon="el-icon-delete-solid"
            @click="deleteRecord(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      style="text-align: right"
      layout="prev, pager, next"
      v-if="total > 0"
      @current-change="pageChange"
      :page-size="size"
      :total="total"
    >
    </el-pagination>
  </section>
</template>

<script>
import util from "../../utils/utils";
export default {
  methods: {
    queryOverdue() {
      this.records = this.records.filter(
        item => item.overdue * 1 != null && item.overdue > 0
      );
    },
    // 分页改变
    pageChange(current) {
      console.log(current);
      this.current = current;
      this.getRecords();
    },
    myFormatter(row, column) {
      let id = row[column.property];
      let cat = this.cats.find(item => item.cid === id);
      if (cat == undefined) return "";
      return cat.cname;
    },
    // 获取记录
    getRecords() {
      this.loading = true;
      let success = res => {
        this.loading = false;
        if (res.data.code === 200 && res) {
          this.total = res.data.data.total;
          this.records = res.data.data.records;
        } else {
          if (this.current == 1) {
            console.log("查询失败");
            this.records = null;
            this.total = 0;
          } else {
            this.current = this.current - 1;
            this.getRecords();
          }
        }
      };
      util.axiosMethod({
        url: "/borrow",
        method: "get",
        params: {
          current: this.current,
          size: this.size,
          sNo: this.borrow.sno,
          sName: this.borrow.sname,
          flag: this.borrow.flag == "" ? 2 : this.borrow.flag
        },
        callback: success
      });
    },

    // 根据条件模糊查询
    query() {
      this.getRecords();
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
    //  删除记录
    deleteRecord(id) {
      let success = res => {
        console.log(res.data);

        if (res.data.code == 200 && res) {
          this.getRecords();
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
      let cat = res => {
        if (res && res.data.code == 500) {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      this.$confirm("删除记录信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "danger"
      })
        .then(() => {
          util.axiosMethod({
            url: "/borrow",
            method: "delete",
            params: {
              id: id
            },
            callback: success,
            catch: cat
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
    that.getRecords();
    that.getLeafCats();
  },
  data() {
    return {
      records: [],
      current: 1,
      size: 4,
      total: 0,
      loading: false,
      borrow: {
        sno: "",
        sname: "",
        flag: ""
      },
      cats: [],
      dialogVisible: false
    };
  }
};
</script>

<style scoped></style>
