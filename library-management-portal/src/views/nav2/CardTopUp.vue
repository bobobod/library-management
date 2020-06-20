<template>
  <section>
    <el-button :loading="loading" @click="queryCard">查询智能卡</el-button>
    <el-divider></el-divider>
    <div v-if="check">
      <el-form
        :model="reader"
        :inline="true"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="智能卡" prop="cardId"> {{ cardId }}</el-form-item
        ><br />
        <el-form-item label="姓名" prop=""> {{ reader.sname }}</el-form-item
        ><br />
        <el-form-item label="余额" prop="balance">
          {{ reader.balance }}</el-form-item
        ><br />
        <el-form-item label="充值金额" prop="balance2">
          <el-input-number
            v-model="num"
            @change="handleChange"
            :min="0"
            :max="100"
            label="描述文字"
          ></el-input-number> </el-form-item
        ><br />
        <el-form-item>
          <el-button type="primary" @click="topUp"
            >确定充值</el-button
          > </el-form-item
        ><br />
      </el-form>
    </div>
    <div v-else>
      <el-tag type="warning">暂无数据，请点击按钮读取智能卡！</el-tag>
    </div>
  </section>
</template>

<script>
import util from "../../utils/utils";
export default {
  data() {
    return {
      loading: false,
      cardId: "",
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
      },
      restaurants: [],
      state: "",
      timeout: null,
      num: "",
      check: false
    };
  },
  methods: {
    handleChange(value) {
      this.num = value;
    },
    queryCard() {
      this.loading = true;
      this.$message({
        showClose: true,
        message: "正在读卡，请放入智能卡",
        type: "warning"
      });
      this.getCardId();
    },
    getCardId() {
      let success = res => {
        console.log(res.data);
        console.log("hello");

        if (res.data.data !== null) {
          console.log("查询智能卡成功");
          this.$notify({
            title: "成功",
            message: "查询智能卡成功",
            type: "success"
          });
          this.cardId = res.data.data;
          this.getReaderByCard(this.cardId);
        } else {
          console.log("查询智能卡失败");
          this.$notify({
            title: "失败",
            message: "查询智能卡失败",
            type: "error"
          });
          this.loading = false;
          this.check = false;
        }
      };
      util.axiosMethod({
        url: "/card",
        method: "get",
        callback: success
      });
    },
    getReaderByCard(cardId) {
      let success = res => {
        if (res.data.data !== null) {
          this.$notify({
            title: "成功",
            message: "查询读者成功",
            type: "success"
          });
          this.reader = res.data.data;
          this.loading = false;
          this.check = true;
        } else {
          this.$notify({
            title: "失败",
            message: "查询读者失败",
            type: "error"
          });
          this.loading = false;
          this.check = false;
        }
      };
      util.axiosMethod({
        url: `/reader/${cardId}`,
        method: "get",
        callback: success
      });
    },
    topUp() {
      let success = res => {
        console.log(res.data);
        if (res.data.data === 1) {
          console.log("充值成功");
          this.$notify({
            title: "成功",
            message: "充值成功",
            type: "success"
          });
          this.getReaderData();
        } else {
          console.log("充值失败");
          this.$notify({
            title: "失败",
            message: "充值失败",
            type: "error"
          });
        }
      };
      this.reader.balance = this.reader.balance + this.num;
      util.axiosMethod({
        url: "/reader",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.reader),
        callback: success
      });
    }
  }
};
</script>

<style></style>
