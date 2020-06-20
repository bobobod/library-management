<template>
  <div class="parent">
    <div class="form-container">
      <div class="user-img"></div>
      <ul class="list">
        <li>
          <h2>管员员注册</h2>
        </li>
        <li>
          <input type="text" name="no" v-model="no" placeholder="User No" />
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
          <input
            type="text"
            v-model="tel"
            name="tel"
            placeholder="PhoneNumber"
          />
        </li>
        <li>
          <div class="code1">
            <input
              type="text"
              v-model="telCode"
              name="telCode"
              placeholder="PhoneCode"
            />
            <button @click="sendSMS">发送短信</button>
          </div>
        </li>
        <li>
          <input
            type="text"
            v-model="address"
            name="address"
            placeholder="Address"
          />
        </li>
        <li>
          <div class="code2">
            <input type="text" v-model="code" name="code" placeholder="Code" />
            <img :src="captchaSrc" @click="getCode" alt="" />
          </div>
        </li>
        <li>
          <button @click="doRegister">提交</button>
        </li>
        <li>
          <el-link icon="el-icon-edit" style="color:red;" @click="toLogin"
            >登录</el-link
          >
        </li>
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
      tel: "",
      address: "",
      code: "",
      telCode: "",
      captchaSrc: "",
      flag: false,
      message: "",
      no: ""
    };
  },
  mounted() {
    this.getCode();
  },
  methods: {
    doRegister() {
      let no = this.no.trim();
      let username = this.username.trim();
      let password = this.password.trim();
      let code = this.code.trim();
      let tel = this.tel.trim();
      let address = this.address.trim();
      let telCode = this.telCode.trim;
      this.checkPhone(tel);
      if (this.flag == true) {
        this.$message({
          message: this.message,
          type: "warning"
        });
        return;
      }

      this.flag = false;
      this.message = "";
      if (username == "" || username == undefined) {
        this.$message({
          message: "请输入用户名",
          type: "warning"
        });
        return;
      }
      if (no == "" || no == undefined) {
        this.$message({
          message: "请输入用户学工号",
          type: "warning"
        });
        return;
      }
      if (tel == "" || tel == undefined) {
        this.$message({
          message: "请输入手机号",
          type: "warning"
        });
        return;
      }
      if (address == "" || address == undefined) {
        this.$message({
          message: "请输入地址",
          type: "warning"
        });
        return;
      }
      if (telCode == "" || telCode == undefined) {
        this.$message({
          message: "手机验证码不能为空",
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
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      util.axiosMethod({
        url: "/admin/register",
        method: "post",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify({
          ano: this.no,
          aname: this.username,
          apassword: this.password,
          code: this.code,
          address: this.address,
          telCode: this.telCode,
          tel: this.tel
        }),
        callback: success
      });
    },
    toLogin() {
      this.$router.push({ path: "/login" });
    },
    sendSMS() {
      this.checkPhone(this.tel);
      if (this.flag == true) {
        this.$message({
          message: this.message,
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
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
        this.flag = false;
        this.message = "";
      };
      util.axiosMethod({
        url: "/sendSMS",
        method: "get",
        params: {
          tel: this.tel
        },
        callback: success
      });
    },
    checkPhone(phone) {
      if (!/^1[3456789]\d{9}$/.test(phone)) {
        this.flag = true;
        this.message = "手机号有误";
      } else {
        this.flag = false;
        this.message = "";
      }
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
  padding-top: 20vh;
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
ul.list li > button {
  width: 250px;
  padding: 8px 0px;
  text-align: center;
  border: nono;
  background-color: #4590fb;
  color: #fff;
}
.code2 input {
  width: 150px;
  text-align: center;
  padding: 8px 0px;
  border: none;
  background-color: #d3d3d3;
}
.code2 img {
  margin: auto;
  width: 100px;
  height: 25px;
}
div.code1 input {
  width: 150px;
  text-align: center;
  padding: 8px 0px;
  border: none;
  background-color: #d3d3d3;
}
div.code1 button {
  width: 100px;
  padding: 8px 0px;
  text-align: center;
  border: nono;
  background-color: grey;
  color: #fff;
}
</style>
