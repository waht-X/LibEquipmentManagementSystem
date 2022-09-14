<template>
  <div>
    <!--按照账号进行搜索-->
    <el-input placeholder="按照账号进行搜索" style="width: 270px; margin-left: 1000px"
              v-model="data.account" class="input-with-select" size="small">
      <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
    </el-input>

    <el-divider></el-divider>

    <el-table :data="users" border style="width: 100%" stripe>

      <el-table-column label="用户名" width="150px">
        <template slot-scope="scope">
          {{ value(scope.row.username) }}
        </template>
      </el-table-column>
      <el-table-column label="账户">
        <template slot-scope="scope">
          {{ value(scope.row.account) }}
        </template>
      </el-table-column>
      <el-table-column label="邮箱">
        <template slot-scope="scope">
          {{ value(scope.row.email) }}
        </template>
      </el-table-column>
      <el-table-column label="账户等级" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag>{{ value(scope.row.level) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账户状态" width="100px" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ value(_inverseTransformCategory(scope.row.status, statuses)) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="changeLevel(1, scope.row)">提权</el-button>
          <el-button size="mini" @click="changeLevel(-1, scope.row)">降权</el-button>
          <el-button size="mini" type="danger" @click="deleteAccount(scope.row.account, scope.$index)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <el-divider></el-divider>

    <el-pagination
      background
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :page-count="totalPage"
      :current-page="pageIndex"
      :page-size="pageSize">
    </el-pagination>

  </div>
</template>

<script>
import {getAllStatus, getUsersInfo, inverseTransformCategory} from "../assets/js/DealData";
import {openTips} from "../assets/js/Tips";

export default {
  name: "PermissionManagement",
  data() {
    return {
      users: [],
      statuses: [],
      data: {},
      totalPage: 1,
      pageIndex: 1,
      pageSize: 6
    }
  },
  methods: {
    value(val) {
      return ((val == "" || val == null) && val != 0 ) ? "暂无数据" : val;
    },
    _inverseTransformCategory(id, category) {
      return inverseTransformCategory(id, category);
    },
    changeLevel(model, user) {
      this.$axios({
        url: 'user/modifyUserLevel',
        method: 'get',
        params: {
          model: model,
          account: user.account
        }
      }).then(res => {
        if (res.data.success) {
          if (model > 0) {
            openTips("账户权限修改成功", `账户[${user.account}]权限上升一级`);
            user.level++;
          } else {
            openTips("账户权限修改成功", `账户[${user.account}]权限下降一级`);
            user.level--;
          }
        } else {
          openTips("账户权限修改失败", res.data.msg);
        }
      })
    },
    deleteAccount(account, index) {
      this.$axios({
        url: 'user/deleteUser',
        method: 'get',
        params: {
          account: account
        }
      }).then(res => {
        if (res.data.success) {
          openTips("SUCCESS", "账户注销成功");
          this.users.splice(index, 1);
        } else {
          openTips("FAIL", res.data.msg);
        }
      })
    },
    search() {
      getUsersInfo(this.data.account, this.pageSize, this.pageIndex)
        .then(res => {
        if (res.success) {
          console.log(res.data);
          this.users = res.data.users;
          this.totalPage = res.data.totalPage;
        } else {
          openTips(res.errorCode, res.msg);
        }
      })
    },
    handleCurrentChange(val) {
      this.pageIndex = val;
      this.search();
    }

  },
  created() {
    getAllStatus().then(res => {
      this.statuses = res.data;
    })
  }
}
</script>

<style scoped>

</style>
