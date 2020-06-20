<template>
  <div style="width: 600px;">
    <el-input
      placeholder="请输入图书类目名称进行搜索..."
      prefix-icon="el-icon-search"
      v-model="filterText"
    >
    </el-input>
    <el-popover placement="top" width="160" v-model="visible">
      <p>请输入根类目的名称</p>
      <div>
        <el-input
          size="mini"
          placeholder="名称"
          prefix-icon="el-icon-plus"
          v-model="rootName"
        >
        </el-input>
      </div>
      <br />
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="visible = false"
          >取消</el-button
        >
        <el-button type="primary" size="mini" @click="addRoot">确定</el-button>
      </div>
      <el-button
        slot="reference"
        size="mini"
        icon="el-icon-circle-plus-outline
"
      >
        添加父节点
      </el-button>
    </el-popover>
    <el-divider></el-divider>
    <el-tree
      :data="cats"
      :props="defaultProps"
      :expand-on-click-node="false"
      :filter-node-method="filterNode"
      ref="tree"
    >
      <span
        class="custom-tree-node"
        style="display: flex;justify-content: space-between;width: 100%;"
        slot-scope="{ node, data }"
      >
        <span>{{ data.cname }}</span>
        <span>
          <el-button
            type="primary"
            size="mini"
            class="depBtn"
            @click="() => showAddCatView(data)"
          >
            添加类目
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="depBtn"
            @click="() => showUpdateCatView(data)"
          >
            修改类目
          </el-button>
          <el-button
            type="danger"
            size="mini"
            class="depBtn"
            @click="() => deleteCat(data)"
          >
            删除类目
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog title="添加类目" :visible.sync="dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>上级类目</el-tag>
            </td>
            <td>{{ pname }}</td>
          </tr>
          <tr>
            <td>
              <el-tag>类目名称</el-tag>
            </td>
            <td>
              <el-input
                v-model="cat.cname"
                placeholder="请输入类目名称..."
              ></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddCat">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="修改类目" :visible.sync="dialogVisible1" width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>类目名称修改</el-tag>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>类目名称</el-tag>
            </td>
            <td>
              <el-input
                v-model="cat.cname"
                placeholder="请输入修改后类目名称..."
              ></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="doUpdateCat">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import util from "../../utils/utils";
export default {
  data() {
    return {
      visible: false,
      rootName: "",
      dialogVisible: false,
      dialogVisible1: false,
      filterText: "",
      cat: {
        cid: "",
        cname: "",
        parentId: -1,
        isParent: 0
      },
      pname: "",
      cats: [],
      defaultProps: {
        children: "children",
        label: "cname"
      }
    };
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  mounted() {
    this.initCats();
  },
  methods: {
    initCat() {
      this.rootName = "";
      this.cat = {
        cname: "",
        parentId: -1,
        isParent: 0
      };
      this.pname = "";
    },
    addRoot() {
      if (this.rootName != "") {
        this.cat.cname = this.rootName;
        let success = res => {
          console.log(res.data);
          if (res.status == 200 && res) {
            console.log("添加类目成功");
            this.$notify({
              title: "成功",
              message: res.data.msg,
              type: "success"
            });
            this.cat.cid = res.data.data;
            this.initCats();
            this.visible = false;
            this.initCat();
          } else {
            this.$notify({
              title: "失败",
              message: res.data.msg,
              type: "error"
            });
          }
        };

        util.axiosMethod({
          url: "/category/root",
          method: "post",
          headers: { "Content-Type": "application/json;charset=UTF-8" },
          data: JSON.stringify(this.cat),
          callback: success
        });
      } else {
        this.$notify({
          title: "失败",
          message: "名字不能为空",
          type: "error"
        });
      }
    },
    addCat2Cats(cats, cat) {
      for (let i = 0; i < cats.length; i++) {
        let d = cats[i];
        if (d.cid == cat.parentId) {
          d.children = d.children.concat(cat);
          if (d.children.length > 0) {
            d.isParent = 1;
          }
          return;
        } else {
          this.addCat2Cats(d.children, cat);
        }
      }
    },
    doAddCat() {
      let success = res => {
        console.log(res.data);
        if (res.status == 200 && res) {
          console.log("添加类目成功");
          this.$notify({
            title: "成功",
            message: "添加类目成功",
            type: "success"
          });
          this.cat.cid = res.data.data;
          this.addCat2Cats(this.cats, this.cat);
          this.dialogVisible = false;
          this.initCats();
        } else {
          console.log("添加类目失败");
          this.$notify({
            title: "失败",
            message: "添加类目失败",
            type: "error"
          });
        }
      };

      util.axiosMethod({
        url: "/category",
        method: "post",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.cat),
        callback: success
      });
    },
    removeCatFromCats(p, cats, id) {
      for (let i = 0; i < cats.length; i++) {
        let d = cats[i];
        if (d.cid == id) {
          cats.splice(i, 1);
          if (cats.length == 0) {
            p.isParent = 0;
          }
          return;
        } else {
          this.removeCatFromCats(d, d.children, id);
        }
      }
    },
    deleteCat(data) {
      if (data.isParent) {
        this.$message.error("父部门删除失败");
      } else {
        this.$confirm(
          "此操作将永久删除【" + data.cname + "】类目, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        )
          .then(() => {
            let success = res => {
              console.log(res.data);

              if (res.data.data === 1) {
                this.removeCatFromCats(null, this.cats, data.cid);
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
              url: "/category",
              method: "delete",
              params: {
                id: data.cid
              },
              callback: success
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    showAddCatView(data) {
      this.pname = data.cname;
      this.cat.parentId = data.cid;
      this.dialogVisible = true;
    },
    initCats() {
      let success = res => {
        if (res && res.status === 200) {
          this.cats = res.data.data;
        } else {
          console.log("查询失败");
        }
      };
      util.axiosMethod({
        url: "/category",
        method: "get",

        callback: success
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.cname.indexOf(value) !== -1;
    },
    showUpdateCatView(data) {
      this.cat = data;
      this.dialogVisible1 = true;
    },
    doUpdateCat() {
      let success = res => {
        console.log(res.data);
        if (res.data.data === 1) {
          console.log("修改类目成功");
          this.$notify({
            title: "成功",
            message: res.data.msg,
            type: "success"
          });
          this.initCats();
          this.initCat();
        } else {
          console.log("修改类目失败");
          this.$notify({
            title: "失败",
            message: res.data.msg,
            type: "error"
          });
          this.initCats();
          console.log("修改失败");
        }
        this.dialogVisible1 = false;
      };
      util.axiosMethod({
        url: "/category",
        method: "put",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        data: JSON.stringify(this.cat),
        callback: success
      });
    }
  }
};
</script>

<style>
.depBtn {
  padding: 2px;
}
</style>
