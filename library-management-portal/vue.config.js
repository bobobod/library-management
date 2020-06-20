module.exports = {
  devServer: {
    open: false,
    host: "localhost",
    port: 8080,
    https: false,
    proxy: {
      "/": {
        target: "http://127.0.0.1:8099", // 本地请求后端真正的地址，只有本地开发时才会做代理，上线不执行这段代码
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          // 路径重写
          "^/api": ""
        }
      }
    }
  }
};
