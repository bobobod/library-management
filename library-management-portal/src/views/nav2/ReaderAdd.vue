<template>
  <section>
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
      <el-form-item label="密码" prop="spassword">
        <el-input
          placeholder="请输入密码"
          v-model="reader.spassword"
          show-password
        ></el-input> </el-form-item
      ><br />
      <el-form-item label="性别" prop="sex">
        <el-radio v-model="reader.sex" label="0">男</el-radio>
        <el-radio v-model="reader.sex" label="1">女</el-radio> </el-form-item
      ><br />
      <el-form-item label="智能卡卡号" prop="cardId">
        <el-input
          v-model="reader.cardId"
          placeholder="请放入智能卡"
        ></el-input> </el-form-item
      ><br />
      <el-form-item label="电话" prop="tel">
        <el-input v-model="reader.tel"></el-input> </el-form-item
      ><br />
      <el-form-item>
        <el-button type="primary" @click="addReader"
          >确 定</el-button
        > </el-form-item
      ><br />
    </el-form>
  </section>
</template>

<script>
import util from "../../utils/utils";
export default {
  data() {
    return {
      reader: {
        sid: "",
        sno: "",
        sname: "",
        spassword: "",
        cardId: "",
        sex: "",
        tel: "",
        balance: 0,
        status: 0
      }
    };
  },
  methods: {
    addReader() {
      let success = res => {
        console.log(res.data);
        if (res.data.data === 1) {
          console.log("添加读者成功");
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
      this.$confirm("添加读者信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "info"
      })
        .then(() => {
          util.axiosMethod({
            url: "/reader",
            method: "post",
            headers: { "Content-Type": "application/json;charset=UTF-8" },
            data: JSON.stringify(this.reader),
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
