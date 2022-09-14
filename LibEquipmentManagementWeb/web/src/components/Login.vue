<template>
  <div>
    <div class="login-box">
      <img src="../assets/img/goback.svg" class="goback-icon focus-cursor" @click="goback" alt=""/>
      <p style="font-size: large">登录</p>
      <br>
      <div class="form">
        username:
        <el-input placeholder="请输入账号" v-model="form.username" style="width: 300px;"></el-input>
        <br>
        <br>
        password:
        <el-input placeholder="请输入密码" v-model="form.password" style="width: 300px;" show-password></el-input>
        <br>
        <br>
        <span class="focus-cursor" @click="jumpToRegister">没有账号？账号注册</span>
        <br>
        <br>
        <el-button @click="login">登录</el-button>
        <el-button @click="resetForm">重置</el-button>
        <br>
        <br>
        <br>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
    }
  },
  created() {
    this.$store.state.isIndependentPage = true;

  },
  methods: {
    jumpToRegister() {
      this.$router.push('register')
    },
    resetForm() {
      const values = Object.keys(this.form);
      for (const value of values) {
        this.form[value] = '';
      }
    },
    login() {
      this.$axios({
        url: '/user/login',
        method: "post",
        data: {
          account: this.form.username,
          password: this.form.password
        }
      }).then(res => {
        console.log(res.data);
        if (res.data.success) {
          this.$store.commit('changeIndependentPageStatus', false);
          this.openTips('登录成功', '跳转至主页');
          this.$store.state.user = res.data.data.userInfo;
          this.$store.commit('changeLoginStatus', true);
          this.$router.push('/');

          //获得权限判断
          this.$axios.get('user/isHandler').then(res => {
            if (res.data.success) {
              this.$store.commit('changePermission', true);
            } else {
              this.$store.commit('changePermission', false);
            }
          })

          //超级权限判断
          this.$axios.get('user/isSuperHandler').then(res => {
            if (res.data.success) {
              this.$store.commit('changSuperPermission', true);
            } else {
              this.$store.commit('changSuperPermission', false);
            }
          })
        } else {
          //登录失败
          this.openTips('登录失败', res.data.msg);
        }
      })
    },
    goback() {
      this.$router.push('/');
      this.$store.commit('changeIndependentPageStatus', false);
    },
    openTips(title, msg) {
      const h = this.$createElement;
      this.$notify({
        title: title,
        message: h('i', {style: 'color: teal'}, msg)
      });
    },


  },
}
</script>

<style scoped>


.login-box {
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  margin: auto auto;
  text-align: center;
  width: 500px;
}

.form {
  /*width: 300px;*/
  margin: auto auto;
  width: auto;
}

.focus-cursor {
  cursor: pointer;
  /*text-decoration: blue;*/
  /*text-decoration: underline;*/
}

.focus-cursor:hover {
  text-decoration: blue;
  text-decoration: underline;
}

.goback-icon {
  width: 28px;
  height: 28px;
  margin-left: -430px;
  padding-top: 20px;;
}

</style>
