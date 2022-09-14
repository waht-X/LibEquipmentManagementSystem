// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";
//引入vuex
import {store} from "./store";


//后端默认url前缀
axios.defaults.baseURL = 'http://localhost:8088';
//添加认证防止session失效
axios.defaults.withCredentials = true;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// 将axios工具绑定到vue实例的原型中：目的：为了在任何vue组件中都能够使用this.$axios获取axios工具
Vue.prototype.$axios = axios;


Vue.use(ElementUI)

/* eslint-disable no-new */
const vue = new Vue({
  el: '#app',
  router,
  store,//使用vuex
  components: { App },
  template: '<App/>'
})

export default vue;
