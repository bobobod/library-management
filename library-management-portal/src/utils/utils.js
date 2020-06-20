import axios from "axios";
import { Message } from "element-ui";
import router from "../router";
axios.interceptors.response.use(
  success => {
    return success;
  },
  error => {
    if (error.response.status == 504 || error.response.status == 404) {
      Message.error({ message: "服务器被吃了( ╯□╰ )" });
    } else if (error.response.status == 403) {
      Message.error({ message: "权限不足，请联系管理员" });
    } else if (error.response.status == 401) {
      Message.error({ message: "尚未登录，请登录" });
      router.push({ path: "/login" });
    } else {
      if (error.response.data.msg) {
        Message.error({ message: error.response.data.msg });
      } else {
        Message.error({ message: "未知错误!" });
      }
    }
    return;
  }
);
const utils = {
  axiosMethod: config => {
    axios({
      method: config.method,
      url: config.url,
      params: config.params ? config.params : null,
      data: config.data ? config.data : null,
      headers: config.headers ? config.headers : null
    })
      .then(config.callback)
      .catch(config.catch ? config.catch : () => {});
  }
};

export default utils;
