<template>
  <div>
    报废表单
    <el-divider></el-divider>
    <div class="input-margin">
      <div>
        <span>设备名称: <el-input class="input" v-model="scrapFrom.name"></el-input></span>
        <span class="input-margin">设备编号: <el-input class="input" v-model="scrapFrom.serial"></el-input></span>
        <span class="input-margin">
          设备类别:
          <el-select v-model="scrapFrom.category" placeholder="设备类别">
            <el-option
              v-for="item in equipmentsCategory"
              :key="item.id"
              :value="item.desc">
            </el-option>
          </el-select>
        </span>
      </div><br>
      <div>
        <span>设备型号: <el-input class="input" v-model="scrapFrom.type"></el-input></span>
        <span class="input-margin">设备规格: <el-input class="input" v-model="scrapFrom.specification"></el-input></span>
      </div><br>
      <div>
        <span>设备数量: <el-input class="input" v-model="scrapFrom.number"></el-input></span>
        <span class="input-margin">设备单价: <el-input class="input" v-model="scrapFrom.price"></el-input></span>
      </div><br>
      <div>
        <span>购入日期: <el-date-picker type="datetime" class="input"
                                   v-model="scrapFrom.buyDate"
                                   value-format="yyyy-MM-dd HH:mm:ss"
                                   format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker></span>
        <span class="input-margin">报废日期: <el-date-picker type="datetime" class="input"
                                                        v-model="scrapFrom.fixDate"
                                                        value-format="yyyy-MM-dd HH:mm:ss"
                                                        format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker></span>
      </div><br>
<!--      <div>-->
<!--        <span>维修厂家: <el-input class="input" v-model="scrapFrom.fixServer"></el-input></span>-->
<!--        <span class="input-margin">总计费用: <el-input class="input" v-model="scrapFrom.sumPrice"></el-input></span>-->
<!--      </div><br>-->
      <div>
        <span>申&nbsp;&nbsp;请&nbsp;人: <el-input class="input" v-model="scrapFrom.applicant"></el-input></span>
        <span class="input-margin">申请部门:
<!--          <el-input class="input" v-model="scrapFrom.department"></el-input>-->
          <el-select v-model="scrapFrom.department" placeholder="申请部门" class="input">
              <el-option
                v-for="item in departments"
                :key="item.id"
                :value="item.desc">
              </el-option>
            </el-select>
        </span>
      </div><br>
      <div>
        检验依据: <br>
        <el-input type="textarea" v-model="scrapFrom.position" :rows="2"></el-input>
      </div><br>
      <div>
        报废原因/故障描述:<br>
        <el-input type="textarea" v-model="scrapFrom.reason" :rows="3"></el-input>
      </div><br>
      <div>
        备注:<br>
        <el-input type="textarea" v-model="scrapFrom.note"></el-input>
      </div><br>
    </div>
    <el-button class="submit" type="primary" plain @click="submit">提交</el-button>

  </div>
</template>

<script>
import {
  getAllCategories as _getAllCategories,
  getAllDepartment,
  submitForm,
  transformCategory
} from "../assets/js/DealData";
import {openTips} from "../assets/js/Tips";

export default {
  name: "ScrapFrom",
  data() {
    return {
      scrapFrom: {},
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

    const department = getAllDepartment();
    department.then(res => {
      this.departments = res.data;
    })

  },
  methods: {
    submit() {
      const data = this.scrapFrom;
      console.log(data);
      data.category = transformCategory(data.category, this.equipmentsCategory);
      data.department = transformCategory(data.department, this.departments);
      data.formType = 1;
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
