import Vue from 'vue'
import Router from 'vue-router'
import Empty from "../components/Empty";
import ScrapFrom from "../components/ScrapFrom";
import MaintenanceFrom from "../components/MaintenanceFrom";
import DemandFrom from "../components/DemandFrom";
import Login from "../components/Login";
import Register from "../components/Register";
import {store} from "../store";
import EquipmentView from "../components/EquipmentView";
import FormManagement from "../components/FormManagement";
import PermissionManagement from "../components/PermissionManagement";
import {openTips} from "../assets/js/Tips";


Vue.use(Router)
const originalPush = Router.prototype.push

Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}



const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Empty',
      component: Empty
    },
    {
      path: '/Equipment/ScrapFrom',
      name: 'ScrapFrom',
      component: ScrapFrom
    },
    {
      path: '/Equipment/MaintenanceFrom',
      name: 'MaintenanceFrom',
      component: MaintenanceFrom
    },
    {
      path: '/Equipment/DemandForm',
      name: 'DemandForm',
      component: DemandFrom
    }, {
      path: '/Login',
      name: 'Login',
      component: Login
    },{
      path: '/Register',
      name: 'Register',
      component: Register
    }, {
      path: '/EquipmentView',
      name: 'EquipmentView',
      component: EquipmentView
    }, {
      path: '/FormManagement',
      name: 'FormManagement',
      component: FormManagement
    }, {
      path: '/PermissionManagement',
      name: 'PermissionManagement',
      component: PermissionManagement
    }
  ]
});

router.beforeEach((to, from, next) => {
  const isLogin = store.state.isLogin;
  //登录拦截
  if (!isLogin && to.name != 'Login' && to.name != 'Register') {
    console.log("change page");
    store.commit('changeIndependentPageStatus', true);
    console.log(store.state);
    next({path: '/Login'});
  } else {
    next();
  }

  const isHandler = store.state.isHandler;
  //权限拦截
  if (!isHandler && to.name == 'FormManagement') {
    console.log("intercept, not a handler, cant go FormManagement page!!!");
    openTips("权限不足", "已跳转到主页");
    next({path: '/'});
  }

  const isSuperHandler = store.state.isSuperHandler;
  if (!isSuperHandler && to.name == 'PermissionManagement') {
    console.log("intercept, not a handler, cant go PermissionManagement page!!!");
    openTips("权限不足", "不是超级权限用户,已跳转到主页");
    next({path: '/'});
  }

  next();

});

export default router;
