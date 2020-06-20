<template>
  <section>
    <el-form
      :model="info"
      :inline="true"
      label-width="100px"
      class="demo-ruleForm"
    >
      <el-form-item label="学工号" prop="sno">
        <el-input v-model="info.ano"></el-input> </el-form-item
      ><br />
      <el-form-item label="姓名" prop="sname">
        <el-input v-model="info.aname"></el-input> </el-form-item
      ><br />
      <el-form-item label="密码" prop="spassword">
        <el-input
          placeholder="密码为空，默认为不修改密码"
          v-model="info.apassword"
          show-password
        ></el-input> </el-form-item
      ><br />

      <el-form-item label="电话" prop="tel">
        <el-input v-model="info.tel"></el-input> </el-form-item
      ><br />
      <el-form-item label="电话" prop="tel">
        <el-input v-model="info.address"></el-input> </el-form-item
      ><br />
      <el-form-item>
        <el-button type="primary" @click="updateAdmin"
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
      info: {
        aid: "",
        ano: "",
        aname: "",
        apassword: "",
        tel: "",
        address: ""
      }
    };
  },
  mounted() {
    let admin = JSON.parse(sessionStorage.getItem("admin"));
    this.info = admin;
  },
  methods: {
    updateAdmin() {
      let success = res => {
        console.log(res.data);
        if (res && res.data.code == 200) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          sessionStorage.setItem("admin", JSON.stringify(this.info));
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      this.$confirm("修改管理员信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "info"
      })
        .then(() => {
          util.axiosMethod({
            url: "/admin",
            method: "put",
            headers: { "Content-Type": "application/json;charset=UTF-8" },
            data: JSON.stringify(this.info),
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
