/*
路由器模块
*/
import Vue from "vue";
import VueRouter from "vue-router";
// 引入路由组件
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import Category from "../views/nav1/Category.vue";
import Book from "../views/nav1/Book.vue";
import Borrow from "../views/nav3/Borrow";
import Reader from "../views/nav2/Reader";
import Admin from "../views/nav5/Admin";
import Register from "../views/Register.vue";
import ReaderAdd from "../views/nav2/ReaderAdd";
import CardTopUp from "../views/nav2/CardTopUp";
import Error from "../views/error/Error";
import AutoBorrow from "../views/nav4/AutoBorrow";
Vue.use(VueRouter);

export default new VueRouter({
  // n个路由
  routes: [
    {
      path: "/login",
      hidden: true,
      component: Login
    },
    {
      path: "/register",
      hidden: true,
      component: Register
    },

    {
      path: "/portal",
      hidden: true,
      component: AutoBorrow
    },
    {
      path: "/",
      component: Home,
      name: "图书管理",
      iconCls: "el-icon-s-grid", // 图标样式class
      redirect: "/book",
      children: [
        { path: "/book", component: Book, name: "图书管理" },
        { path: "/category", component: Category, name: "类别管理" }
      ]
    },
    {
      path: "/",
      component: Home,
      name: "读者智能卡管理",
      iconCls: "el-icon-user-solid",
      children: [
        { path: "/reader", component: Reader, name: "读者管理" },
        { path: "/readerAdd", component: ReaderAdd, name: "读者录入" },
        { path: "/cardTopUp", component: CardTopUp, name: "智能卡充值" }
      ]
    },
    {
      path: "/",
      name: "借阅管理",
      iconCls: "el-icon-position",
      component: Home,
      children: [{ path: "/borrow", component: Borrow, name: "图书借阅管理" }]
    },
    {
      path: "/",
      name: "个人信息管理",
      iconCls: "el-icon-s-custom",
      component: Home,
      children: [{ path: "/admin", component: Admin, name: "信息管理" }]
    },
    {
      path: "*",
      hidden: true,
      component: Error
    }

    /*     {
          path: '/about',
          component: About
        },
        {
          path: '/home',
          component: Home,
          children: [
            {
              // path最左边的斜杆永远标识根路径
              // path: '/home/news',
              path: 'news',
              component: News
            },
            {
              path: 'message',
              component: Message,
              children: [
                {
                  // 写占位符：
                  path: 'detail/:id',
                  component: MessageDetail
                }
              ]
            },
            {
              path: '',
              redirect: 'news'
            }
          ]
        },
        // 默认显示
        {
          path: '/',
          redirect: '/about'
        } */
  ]
});
