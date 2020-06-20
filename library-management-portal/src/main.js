import Vue from "vue";
// 默认暴露可以使用如何名字
import App from "./App.vue";
import ElementUi from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

// 因为是index可以省略
import router from "./router";

Vue.use(ElementUi);
router.beforeEach((to, from, next) => {
  if (to.path === "/login") {
    sessionStorage.removeItem("admin");
  }

  let admin = JSON.parse(sessionStorage.getItem("admin"));
  if (
    !admin &&
    to.path !== "/login" &&
    to.path !== "/register" &&
    to.path !== "/portal" &&
    to.path !== "/forgive"
  ) {
    next({ path: "/login" });
  } else {
    next();
  }
}); // eslint-disable-next-line no-new
new Vue({
  // 配置对象的属性都是一些确定的名字，不能随便修改
  el: "#app",
  // 局部注册
  render: h => h(App),
  router
});
