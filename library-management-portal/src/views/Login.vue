<template>
  <div class="parent">
    <div class="form-container">
      <div class="user-img"></div>
      <ul class="list">
        <li>
          <h2>管理员登录</h2>
        </li>
        <li>
          <input
            type="text"
            name="username"
            v-model="username"
            placeholder="User Name"
          />
        </li>

        <li>
          <input
            type="password"
            v-model="password"
            name="password"
            placeholder="Password"
          />
        </li>
        <li>
          <div class="code">
            <input type="text" v-model="code" name="code" placeholder="Code" />
            <img :src="captchaSrc" @click="getCode" alt="" />
          </div>
        </li>
        <li>
          <button @click="doLogin">提交</button>
        </li>
        <li>
          <el-link icon="el-icon-edit" style="color:red;" @click="toRegister"
            >注册</el-link
          >
        </li>

        <li></li>
      </ul>
    </div>
  </div>
</template>

<script>
import util from "../utils/utils";
export default {
  data() {
    return {
      username: "",
      password: "",
      code: "",
      captchaSrc: ""
    };
  },
  mounted() {
    this.getCode();
  },
  methods: {
    doLogin() {
      let username = this.username.trim();
      let password = this.password.trim();
      let code = this.code.trim();
      if (username == "" || username == undefined) {
        this.$message({
          message: "请输入用户名",
          type: "warning"
        });
        return;
      }
      if (password == "" || password == undefined) {
        this.$message({
          message: "请输入密码",
          type: "warning"
        });
        return;
      }
      if (code == "" || code == undefined) {
        this.$message({
          message: "验证码不能为空",
          type: "warning"
        });
        return;
      }
      let success = res => {
        console.log(res.data);
        if (res && res.data.code == 200) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          sessionStorage.setItem("admin", JSON.stringify(res.data.data));
          this.$router.push({ path: "/" });
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      util.axiosMethod({
        url: "/doLogin",
        method: "post",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify({
          username: this.username,
          password: this.password,
          code: this.code
        }),
        callback: success
      });
    },
    toRegister() {
      this.$router.push({ path: "/register" });
    },

    getCode() {
      let success = res => {
        console.log(res.data);
        this.$notify({
          title: "成功",
          message: "获取验证码成功",
          type: "success"
        });
      };
      let url = "/code?d=" + new Date() * 1;
      this.captchaSrc = url;
      util.axiosMethod({
        url: url,
        method: "get",
        callback: success
      });
    }
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
.parent {
  background-color: #f5683d;
  padding-top: 30vh;
  position: absolute;
  top: 0px;
  left: 0px;
  bottom: 0px;
  right: 0px;
}
.form-container {
  width: 250px;
  height: auto;
  padding: 40px 30px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px 0 #000;
  margin: auto;
  position: relative;
}
.user-img {
  background: url("../imgs/landscape.png") no-repeat;
  width: 70px;
  height: 70px;
  background-size: 100%;
  position: absolute;
  top: -40px;
  left: 110px;
}
ul.list {
  list-style-type: none;
  text-align: center;
}
ul.list li {
  width: 250px;
  margin-bottom: 10px;
}
ul.list li > input {
  width: 250px;
  text-align: center;
  padding: 8px 0px;
  border: none;
  background-color: #d3d3d3;
}
ul.list li button {
  width: 250px;
  padding: 8px 0px;
  text-align: center;
  border: nono;
  background-color: #4590fb;
  color: #fff;
}
.code input {
  width: 150px;
  text-align: center;
  padding: 8px 0px;
  border: none;
  background-color: #d3d3d3;
}
.code img {
  margin: auto;
  width: 100px;
  height: 25px;
}
</style>
