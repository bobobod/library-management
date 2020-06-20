<template>
  <div id="parent">
    <div class="child">
      <h2>
        <p class="wel">欢迎使用自动借阅服务</p>
      </h2>
      <div>
        <el-button
          icon="
el-icon-search"
          class="btn"
          @click="
            {
              dialogVisible2 = true;
            }
          "
          >查询图书</el-button
        >
      </div>
      <div>
        <el-button
          icon="
el-icon-notebook-1"
          class="btn"
          @click="
            {
              flag = true;
              dialogVisible = true;
            }
          "
          >借阅图书</el-button
        >
      </div>
      <div>
        <el-button
          icon="el-icon-notebook-2"
          class="btn"
          @click="
            {
              flag = false;
              dialogVisible = true;
            }
          "
          >归还图书</el-button
        >
      </div>
      <div>
        <el-popover placement="top" v-model="visible">
          <div style="width:300px;height:230px;">
            <div>
              <span>请选择登录方式</span><br />
              <el-radio-group v-model="radio">
                <el-radio :label="1">智能卡扫描</el-radio>
                <el-radio :label="2">帐号密码登录</el-radio>
              </el-radio-group>
              <el-divider></el-divider>
            </div>
            <div v-if="radio * 1 == 1">
              <div style="height:120px">
                <el-button
                  :loading="loading"
                  @click="loginByScanCard"
                  icon="el-icon-search"
                  >请放入智能卡</el-button
                >
              </div>
            </div>
            <div v-else>
              <el-input v-model="username" placeholder="用户名"></el-input>
              <el-input
                v-model="password"
                show-password
                placeholder="用户密码"
              ></el-input>
              <el-col :span="15">
                <el-input v-model="code" placeholder="验证码"></el-input>
              </el-col>
              <img
                :src="captchaSrc"
                @click="getCode"
                alt=""
                style="height:35px;"
              />
            </div>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible = false"
                >取消</el-button
              >
              <el-button type="primary" size="mini" @click="submit"
                >提交</el-button
              >
            </div>
          </div>
          <el-button slot="reference" class="btn" icon="el-icon-info"
            >学生门户</el-button
          >
        </el-popover>
      </div>
    </div>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-steps :active="active" finish-status="success" simple>
          <el-step title="步骤 1"></el-step>
          <el-step title="步骤 2"></el-step>
          <el-step title="步骤 3"></el-step>
        </el-steps>
      </div>
      <div style="margin-top:14px;">
        <div v-if="active == 0">
          <el-button :loading="loading" @click="queryCard"
            >请放入智能卡</el-button
          >
          <br />
          <span v-show="cCheck">
            <el-alert
              :title="message ? '读取智能卡成功' : '读取智能卡失败'"
              :type="message ? 'success' : 'error'"
              show-icon
            >
            </el-alert>
          </span>
        </div>
        <div v-else-if="active == 1">
          <el-button :loading="loading" @click="queryBookCard"
            >请放入图书卡</el-button
          >
          <br />
          <span v-show="bcCheck">
            <el-alert
              :title="message ? '读取图书卡成功' : '读取图书卡失败'"
              :type="message ? 'success' : 'error'"
              show-icon
            >
            </el-alert>
          </span>
        </div>
        <div v-else>
          <div v-if="flag == true">
            <p>请选择借阅时间：</p>
            <el-date-picker
              v-model="borrowTime"
              type="date"
              style="width: 200px"
              placeholder="选择借阅日期"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
            <p>请选择逾期日期：</p>
            <el-date-picker
              v-model="expire"
              type="date"
              style="width: 200px"
              placeholder="选择逾期日期"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </div>
          <div v-else>
            <el-alert
              title="扣费规则：当归还日期不在指定日期内，按每天一块钱进行扣费。"
              type="warning"
              show-icon
            >
            </el-alert>
            <p>请选择归还日期：</p>
            <el-date-picker
              v-model="returnTime"
              type="date"
              style="width: 200px"
              placeholder="选择归还日期"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <div v-if="active == 3">
          <el-button
            @click="
              {
                dialogVisible = false;
                initData();
              }
            "
            >取 消</el-button
          >
          <el-button type="primary" @click="operate">确 定</el-button>
        </div>
        <div v-else>
          <el-button
            @click="
              {
                dialogVisible = false;
                active = 0;
                cCheck = false;
                bcCheck = false;
                initData();
              }
            "
            >取 消</el-button
          >
          <el-button :disabled="!message" @click="next">下一步</el-button>
        </div>
      </span>
    </el-dialog>
    <el-dialog
      title="查询图书所在位置"
      :visible.sync="dialogVisible2"
      width="480px"
    >
      <el-input
        placeholder="请输入图书名"
        v-model="query.name"
        style="width:200px;"
        size="medium"
        prefix-icon="el-icon-search"
        clearable
      >
      </el-input>
      <el-input
        placeholder="请输入图书作者"
        v-model="query.writer"
        prefix-icon="el-icon-search"
        style="width:200px;"
        size="medium"
        clearable
      >
      </el-input>
      <el-button size="medium" style="text-align: right" @click="getBookLists"
        >查询图书</el-button
      >
      <el-divider></el-divider>

      <el-table
        :data="bookLists"
        v-loading="loading1"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
      >
        <el-table-column prop="bname" label="图书名" width="90">
        </el-table-column>

        <el-table-column prop="writer" label="作者" width="90">
        </el-table-column>
        <el-table-column prop="press" label="出版社" width="90">
        </el-table-column>
        <el-table-column
          prop="position"
          label="所在位置"
          :formatter="myFormatter"
          width="90"
        >
        </el-table-column>
        <el-table-column label="图书状态" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.status == 0">正常</span>
            <span v-else>出借</span>
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
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="
            {
              dialogVisible2 = false;
              initData();
            }
          "
          >关闭</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogVisible1"
      width="50%"
      :title="userTitle"
      @close="beforeClose"
    >
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">
            用户基本信息及操作<i class="header-icon el-icon-info"></i>
          </template>
          <div>
            <el-form
              :model="reader"
              :inline="true"
              label-width="100px"
              class="demo-ruleForm"
            >
              <el-form-item label="学号">
                <el-input v-model="reader.sno"></el-input> </el-form-item
              ><br />
              <el-form-item label="姓名">
                <el-input v-model="reader.sname"></el-input> </el-form-item
              ><br />
              <el-form-item label="密码">
                <el-input
                  placeholder="为空默认为不修改"
                  v-model="reader.spassword"
                  show-password
                ></el-input> </el-form-item
              ><br />
              <el-form-item label="性别" prop="sex">
                <el-radio-group v-model="reader.sex">
                  <el-radio :label="0">男</el-radio>
                  <el-radio :label="1">女</el-radio>
                </el-radio-group> </el-form-item
              ><br />
              <el-form-item label="电话">
                <el-input v-model="reader.tel"></el-input> </el-form-item
              ><br />
              <el-form-item label="余额">
                <el-input
                  :disabled="true"
                  v-model="reader.balance"
                ></el-input> </el-form-item
              ><br />
              <el-form-item>
                <el-button type="primary" @click="updateReader"
                  >修 改</el-button
                > </el-form-item
              ><br />
            </el-form>
          </div>
        </el-collapse-item>
        <el-collapse-item title="用户借阅查询">
          <el-table :data="records" border>
            <el-table-column prop="bookId" label="图书编号"> </el-table-column>
            <el-table-column prop="bname" label="图书名"> </el-table-column>
            <el-table-column prop="borrowDate" label="借阅时间">
            </el-table-column>
            <el-table-column prop="expire" label="应还时间"> </el-table-column>

            <el-table-column label="逾期天数（天）">
              <template slot-scope="scope">
                <span v-if="scope.row.overdue === null">未归还</span>
                <span v-else-if="scope.row.overdue === 0">未逾期</span>
                <span style="color:red;" v-else>{{ scope.row.overdue }} </span>
              </template>
            </el-table-column>
            <el-table-column label="归还日期">
              <template slot-scope="scope">
                <span v-if="scope.row.returnDate === null">未归还</span>
                <span v-else>{{ scope.row.returnDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="状态">
              <template slot-scope="scope">
                <span v-if="scope.row.status === 0">借出</span>
                <span v-else> 已还 </span>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
        <el-collapse-item title="用户挂失与注销">
          <div>
            用户可以自行进行挂失操作，解挂需要<span style="color:red"
              >管理员</span
            >权限。。。
          </div>
          <el-divider></el-divider>
          <div>
            <el-button
              @click="lost"
              style="width:120px"
              icon="el-icon-warning-outline"
              >挂失</el-button
            >
          </div>
          <div>
            <el-button
              @click="logout"
              icon="el-icon-s-custom"
              style="width:120px"
              >注销账户</el-button
            >
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-dialog>
  </div>
</template>

<script>
import util from "../../utils/utils";
export default {
  data() {
    return {
      total: 0,
      current: 1,
      size: 3,
      query: {
        name: "",
        writer: ""
      },
      loading1: false,
      bookLists: [],
      records: [],
      userTitle: "",
      username: "",
      password: "",
      flag: "",
      cCheck: false,
      bcCheck: false,
      message: false,
      cardId: "",
      bookCardId: "",
      borrowTime: "",
      expire: "",
      returnTime: "",
      loading: false,
      active: 0,
      dialogVisible: false,
      dialogVisible1: false,
      dialogVisible2: false,
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
      borrow: {
        id: "",
        bookId: "",
        bname: "",
        cid: "",
        borrowDate: "",
        expire: "",
        overdue: "",
        sno: "",
        sname: "",
        cardId: "",
        returnDate: "",
        status: 0
      },
      book: {
        bid: "",
        bookId: "",
        bname: "",
        cid: "",
        writer: "",
        press: "",
        price: "",
        issue: "",
        status: 0
      },
      visible: false,
      radio: 2,
      code: "",
      captchaSrc: ""
    };
  },
  computed: {
    title() {
      if (this.flag) return "借阅服务";
      else return "归还服务";
    }
  },
  mounted() {
    this.getCode();
  },
  methods: {
    getCode() {
      let success = res => {
        console.log(res.data);
      };
      let url = "/code?d=" + new Date() * 1;
      this.captchaSrc = url;
      util.axiosMethod({
        url: url,
        method: "get",
        callback: success
      });
    },
    myFormatter(row, column) {
      let position = row[column.property];
      if (position == 0) return "暂无该位置信息";
      let str = position.split("/");
      return str[0] + "柜台" + str[1] + "列" + str[2] + "号";
    },
    pageChange(current) {
      console.log(current);
      this.current = current;
      this.getBookLists();
    },
    getBookLists() {
      let success = res => {
        if (res && res.status === 200) {
          this.bookLists = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.total = 0;
          console.log("查询失败");
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
          writer: this.query.writer
        },
        callback: success
      });
    },
    // ---
    beforeClose() {
      this.initData();
    },
    lost() {
      let success = res => {
        if (res.data.code == 200 && res) {
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
        url: "/reader/lost",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },

        data: JSON.stringify(this.reader),
        callback: success
      });
    },
    logout() {
      this.dialogVisible1 = false;
      this.initData();
    },
    // 选择操作
    operate() {
      if (this.flag) this.addBorrowRecord();
      else this.queryBorrowById();
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
        this.cCheck = true;
        if (res.data.code == 200 && res) {
          console.log("查询智能卡成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.cardId = res.data.data;
          if (this.flag) this.getReaderByCard(this.cardId, true);
          else {
            this.message = true;
            this.loading = false;
          }
        } else {
          this.message = false;
          console.log("查询智能卡失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.loading = false;
        }
      };
      util.axiosMethod({
        url: "/card",
        method: "get",
        callback: success
      });
    },
    getReaderByCard(cardId, flag) {
      let success = res => {
        if (res.data.code == 200) {
          this.$notify({
            title: "成功",
            message: "查询读者成功",
            type: "success"
          });

          this.reader = res.data.data;
          this.loading = false;
          if (flag) {
            this.message = true;
          } else {
            this.visible = false;
            this.dialogVisible1 = true;
            this.userTitle = "欢迎[" + this.reader.sname + "]";
            this.queryUserRecordsByCardId(cardId);
          }
        } else {
          this.$notify({
            title: "失败",
            message: "查询读者失败",
            type: "error"
          });
          this.loading = false;
        }
      };
      util.axiosMethod({
        url: `/reader/${cardId}`,
        method: "get",
        callback: success
      });
    },
    updateReader() {
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          console.log(res.data.data);
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };

      util.axiosMethod({
        url: "/reader",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.reader),
        callback: success
      });
    },
    submit() {
      if (this.radio === 2) {
        this.loginByUsernameAndPassword();
      }
      this.visible = false;
    },
    loginByScanCard() {
      this.loading = true;
      let success = res => {
        if (res.data.code == 200 && res) {
          console.log("查询智能卡成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.cardId = res.data.data;
          this.getReaderByCard(this.cardId, false);
        } else {
          this.message = false;
          console.log("查询智能卡失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.loading = false;
        }
      };
      util.axiosMethod({
        url: "/card",
        method: "get",
        callback: success
      });
    },
    loginByUsernameAndPassword() {
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.reader = res.data.data;
          this.userTitle = "欢迎[" + this.reader.sname + "]";
          this.dialogVisible1 = true;

          this.queryUserRecordsByCardId(this.reader.cardId);
          console.log(res.data.data);
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };

      util.axiosMethod({
        url: "/reader/login",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        method: "post",
        data: JSON.stringify({
          username: this.username,
          password: this.password,
          code: this.code
        }),
        callback: success
      });
    },
    queryBookCard() {
      this.loading = true;
      this.$message({
        showClose: true,
        message: "正在读卡，请放入图书卡",
        type: "warning"
      });
      this.getBookByCardId();
    },
    getBookByCardId() {
      let success = res => {
        console.log(res.data);
        console.log("hello");
        this.bcCheck = true;
        if (res.data.data !== null) {
          console.log("查询图书信息成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.bookCardId = res.data.data;
          if (this.flag) this.getBookByBookId(this.bookCardId);
          else {
            this.message = true;
            this.loading = false;
          }
        } else {
          this.message = false;
          console.log("查询智能卡失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.loading = false;
        }
      };
      util.axiosMethod({
        url: "/card/ntg",
        method: "get",
        callback: success
      });
    },
    getBookByBookId(cardId) {
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          console.log(res.data.data);

          this.book = res.data.data;
          this.message = true;
          this.loading = false;
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.loading = false;
        }
      };
      util.axiosMethod({
        url: `/book/${cardId}`,
        method: "get",
        callback: success
      });
    },
    next() {
      this.message = false;
      if (this.active == 1) this.message = true;
      if (this.active++ > 2) this.active = 0;
    },
    initData() {
      this.cardId = "";
      this.bookCardId = "";
      this.borrowTime = "";
      this.expire = "";
      this.total = 0;
      this.bookLists = [];
      this.query = {
        name: "",
        writer: ""
      };
      this.username = "";
      this.password = "";
      this.cCheck = false;
      this.bcCheck = false;
      this.active = 0;
      this.message = false;
      this.code = "";
      this.book = {
        bid: "",
        bookId: "",
        bname: "",
        cid: "",
        writer: "",
        press: "",
        price: "",
        issue: "",
        status: 0
      };
      this.reader = {
        sid: "",
        sno: "",
        sname: "",
        spassword: "",
        cardId: "",
        sex: "",
        tel: "",
        balance: 0,
        status: 0
      };
      this.borrow = {
        id: "",
        bookId: "",
        bname: "",
        cid: "",
        borrowDate: "",
        expire: "",
        overdue: "",
        sno: "",
        sname: "",
        cardId: "",
        returnDate: "",
        status: 0
      };
    },
    queryBorrowById() {
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.borrow = res.data.data;
          this.updateBorrowRecord();
          console.log(res.data.data);
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.dialogVisible = false;
          this.initData();
        }
      };
      console.log(this.bookCardId + "bookid");

      util.axiosMethod({
        url: "/borrow/info",
        method: "get",
        params: {
          rid: this.cardId,
          bid: this.bookCardId
        },
        callback: success
      });
    },
    updateBorrowRecord() {
      this.borrow.returnDate = this.returnTime;
      this.borrow.status = 1;
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: "归还成功",
            type: "success"
          });
          console.log(res.data.data);
        } else {
          this.$notify({
            title: "失败",
            message: "归还失败",
            type: "error"
          });
        }
        this.initData();
        this.dialogVisible = false;
      };
      let cat = error => {
        this.initData();
        this.$notify({
          title: "失败",
          message: error.data.msg,
          type: "error"
        });
      };
      util.axiosMethod({
        url: "/borrow",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.borrow),
        callback: success,
        catch: cat
      });
    },
    addBorrowRecord() {
      this.borrow.bookId = this.book.bookId;
      this.borrow.bname = this.book.bname;
      this.borrow.cid = this.book.cid;
      this.borrow.borrowDate = this.borrowTime;
      this.borrow.expire = this.expire;
      this.borrow.overdue = null;
      this.borrow.sno = this.reader.sno;
      this.borrow.sname = this.reader.sname;
      this.borrow.cardId = this.reader.cardId;
      this.borrow.returnDate = null;
      this.borrow.status = 0;
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          console.log(res.data.data);
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
        this.initData();
        this.dialogVisible = false;
      };
      if (this.reader.balance * 1 >= 0) {
        util.axiosMethod({
          url: "/borrow",
          method: "post",
          headers: { "Content-Type": "application/json;charset=UTF-8" },
          data: JSON.stringify(this.borrow),
          callback: success
        });
      } else {
        this.$notify({
          title: "失败",
          message: "余额不足，请先充值...",
          type: "warning"
        });
      }
    },
    queryUserRecordsByCardId(cardId) {
      let success = res => {
        if (res.data.code == 200 && res) {
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.records = res.data.data;
        } else {
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
        }
      };
      util.axiosMethod({
        url: `/borrow/${cardId}`,
        method: "get",
        callback: success
      });
    }
  }
};
</script>

<style scoped>
#parent {
  background: #f5683d;
  position: absolute;
  top: 0px;
  left: 0px;
  bottom: 0px;
  right: 0px;
}

.child {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 100px auto;
  width: 300px;
  height: 300px;
  padding: 35px 35px 15px 35px;
  background: white;
  text-align: center;
  border: 1px solid #d3d3d3;
  box-shadow: 0 0 25px black;
}
.btn {
  width: 250px;
  color: green;
  margin-bottom: 10px;
}
.wel {
  size: 60px;
  color: red;
}
</style>
