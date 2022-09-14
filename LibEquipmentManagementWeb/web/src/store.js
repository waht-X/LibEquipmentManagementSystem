import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex)

export const store = new Vuex.Store({
  // store刷新后数据会重载 所以用localStorage保存在本地
  state:localStorage.getItem("LibEquipmentManagementWeb") ? JSON.parse(localStorage.getItem("LibEquipmentManagementWeb")): {
    isIndependentPage: false,
    isLogin: false,
    user: {},
    isHandler: false,
    isSuperHandler: false,
  },
  //用于数据提交
  mutations: {
    changeIndependentPageStatus(state, status) {
      store.state.isIndependentPage = status;
    },
    changeLoginStatus(state, status) {
      store.state.isLogin = status;
    },
    changePermission(state, status) {
      store.state.isHandler = status;
    },
    changSuperPermission(state, status) {
      store.state.isSuperHandler = status;
    }
  }
})
