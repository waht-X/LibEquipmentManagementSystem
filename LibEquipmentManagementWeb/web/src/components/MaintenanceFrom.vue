<template>
  <div>
    维修表单
    <el-divider></el-divider>
    <div class="input-margin">
      <div>
        <span>设备名称: <el-input class="input" v-model="maintenanceForm.name"></el-input></span>
        <span class="input-margin">设备编号: <el-input class="input" v-model="maintenanceForm.serial"></el-input></span>
        <span class="input-margin">
          设备类别:
          <el-select v-model="maintenanceForm.category" placeholder="设备类别">
            <el-option
              v-for="item in equipmentsCategory"
              :key="item.id"
              :value="item.desc">
            </el-option>
          </el-select>
        </span>
      </div><br>
      <div>
        <span>设备型号: <el-input class="input" v-model="maintenanceForm.type"></el-input></span>
        <span class="input-margin">设备规格: <el-input class="input" v-model="maintenanceForm.specification"></el-input></span>
      </div><br>
      <div>
        <span>设备数量: <el-input class="input" v-model="maintenanceForm.number"></el-input></span>
        <span class="input-margin">设备单价: <el-input class="input" v-model="maintenanceForm.price"></el-input></span>
      </div><br>
      <div>
        <span>购入日期: <el-date-picker type="datetime" class="input"
                                   v-model="maintenanceForm.buyDate"
                                   value-format="yyyy-MM-dd HH:mm:ss"
                                   format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker></span>
        <span class="input-margin">报修日期: <el-date-picker type="datetime" class="input"
                                   v-model="maintenanceForm.fixDate"
                                   value-format="yyyy-MM-dd HH:mm:ss"
                                   format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker></span>
      </div><br>
      <div>
        <span>维修厂家: <el-input class="input" v-model="maintenanceForm.fixServer"></el-input></span>
        <span class="input-margin">总计费用: <el-input class="input" v-model="maintenanceForm.sumPrice"></el-input></span>
      </div><br>
      <div>
        <span>申&nbsp;&nbsp;请&nbsp;人: <el-input class="input" v-model="maintenanceForm.applicant"></el-input></span>
        <span class="input-margin">申请部门:
<!--          <el-input class="input" v-model="maintenanceForm.department"></el-input>-->
        <el-select v-model="maintenanceForm.department" placeholder="申请部门" class="input">
              <el-option
                v-for="item in departments"
                :key="item.id"
                :value="item.desc">
              </el-option>
            </el-select>
        </span>
      </div><br>
      <div>
        报修部位: <br>
        <el-input type="textarea" v-model="maintenanceForm.position" :rows="2"></el-input>
      </div><br>
      <div>
        报修原因/故障描述:<br>
        <el-input type="textarea" v-model="maintenanceForm.reason" :rows="3"></el-input>
      </div><br>
      <div>
        备注:<br>
        <el-input type="textarea" v-model="maintenanceForm.note"></el-input>
      </div><br>
    </div>
    <el-button class="submit" type="primary" plain @click="submit">提交</el-button>

  </div>

</template>

<script>
import {getAllCategories as _getAllCategories, getAllDepartment, transformCategory} from "../assets/js/DealData";

export default {
  name: "MaintenanceFrom",
  data() {
    return {
      maintenanceForm: {},
      equipmentsCategory: [],
      departments: []
    }
  },
  created() {
    console.log("ready to get categories");
    const ret = _getAllCategories();
    ret.then(res => {
      this.equipmentsCategory = res.data;
    });

    console.log("ready to get department");
    const department = getAllDepartment();
    department.then(res => {
      this.departments = res.data;
    });

  },
  methods: {
    submit() {
      const data = this.maintenanceForm;
      console.log(data);
      data.category = transformCategory(data.category, this.equipmentsCategory);
      data.department = transformCategory(data.department, this.departments);
      data.formType = 2;
      const res = submitForm(data);
      res.then(res => {
        if (res.success) {
          openTips("表单提交成功", res.msg);
        } else {
          openTips("表单提交失败", res.msg);
        }
      }).catch(res => {
        openTips("表单提交失败", "参数错误" + res.message
          + "\r\ncode: " + res.code);
      })
    }
  }
}

import {submitForm} from "../assets/js/DealData";
import {openTips} from "../assets/js/Tips";
</script>

<style scoped>
.input {
  width: 250px;
}

.input-margin {
  margin-left: 70px;
}

.submit {
  margin-left: 500px;
  width: 200px;
}
</style>
