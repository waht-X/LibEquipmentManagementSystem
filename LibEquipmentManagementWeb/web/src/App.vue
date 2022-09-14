<template>
  <div id="app">

    <div>
      <el-container>
        <el-aside class="aside shadow-radius" width="200px" v-if="!dependencePage">
          <el-menu
            @select="selectMenu">

            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>设备管理</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="1-1">报废处理</el-menu-item>
                <el-menu-item index="1-2">维修处理</el-menu-item>
                <el-menu-item index="1-3">需求申请</el-menu-item>
              </el-menu-item-group>
            </el-submenu>

            <el-menu-item index="2">
              <i class="el-icon-menu"></i>
              <span slot="title">记录查看</span>
            </el-menu-item>

            <el-menu-item index="3">
              <i class="el-icon-document"></i>
              <span slot="title">申请管理</span>
            </el-menu-item>

            <el-menu-item index="4">
              <i class="el-icon-setting"></i>
              <span slot="title">权限管理</span>
            </el-menu-item>

          </el-menu>
        </el-aside>
        <el-container>
          <el-header class="header shadow-radius" v-if="!dependencePage">
            <el-popover placement="bottom" width="60px" trigger="click">
              <template slot="reference">
                <el-avatar class="header-img focus-cursor" size="medium" :src="unknownSrc"></el-avatar>
              </template>
              <div style="text-align: center">
                <el-button type="text" size="mini" @click="login">登录</el-button>
                <el-button type="text" size="mini" @click="logout">退出登录</el-button>
              </div>

            </el-popover>

            <span class="user-name">{{ userName }}</span>
          </el-header>
          <el-main class="main shadow-radius" ref="_main">
            <router-view></router-view>
          </el-main>
        </el-container>
      </el-container>
    </div>


  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      unknownSrc: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      user: {
        name: '未登录',
      }
    }
  },
  mounted() {
    window.addEventListener('unload', this.saveState);
  },
  methods: {
    login() {
      console.log("ready jump to login");
      this.$router.push("/Login");
    },
    logout() {
      console.log('logout');
      //退出登录
      this.$store.commit('changeLoginStatus', false);
    },
    saveState() {
      localStorage.setItem("LibEquipmentManagementWeb", JSON.stringify(this.$store.state))
    },
    selectMenu(key, keyPath) {

      console.log("---selectMenu---");
      console.log(key, keyPath);
      console.log("----------------");
      if (key == "1-1") {
        this.$router.push('/Equipment/ScrapFrom');
      } else if (key == "1-2") {
        this.$router.push('/Equipment/MaintenanceFrom');
      } else if (key == "1-3") {
        this.$router.push('/Equipment/DemandForm');
      } else if (key == "2") {
        this.$router.push('/EquipmentView');
      } else if (key == "3") {
        this.$router.push('/FormManagement');
      } else if (key == "4") {
        console.log("permissionManagement");
        this.$router.push('/PermissionManagement');
      }
    }
  },
  computed: {
    userName() {
      if (!this.$store.state.isLogin) {
        //未登录
        return "未登录";
      } else {
        const user = this.$store.state.user;
        this.user = this.$store.state.user;
        this.user = user;
        return user.username;
      }
    },
    dependencePage() {
      return this.$store.state.isIndependentPage;
    }
  },
}

</script>

<style>
.aside {
  margin-right: 10px;
  text-align: center;
}

.header {
  margin-bottom: 10px;
}

.main {
  height: 630px;
}

.header-img {
  position: relative;
  top: 10px;
  left: 1020px;
}

.user-name {
  position: relative;
  left: 1040px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.shadow-radius {
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 4px
}

.focus-cursor {
  cursor: pointer;
}
</style>
