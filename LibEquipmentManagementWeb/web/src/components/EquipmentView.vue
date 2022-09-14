<template>
  <div>
    <div>
      <span>
        <span style="font-size: small">设备类型: </span>:
        <el-select v-model="data.category" placeholder="请选择" size="mini" class="select">
          <el-option
            v-for="item in categories"
            :key="item.id"
            :value="item.desc">
          </el-option>
        </el-select>
      </span>

      <span>
        <span style="font-size: small">表单类型: </span>:
        <el-select v-model="data.formType" placeholder="请选择" size="mini" class="select">
          <el-option
            v-for="item in formTypes"
            :key="item.id"
            :value="item.desc">
          </el-option>
        </el-select>
      </span>

      <span>
        <span style="font-size: small">申请部门: </span>
        <el-select v-model="data.department" placeholder="请选择" size="mini" class="select">
          <el-option
            v-for="item in departments"
            :key="item.id"
            :value="item.desc">
          </el-option>
        </el-select>
      </span>

      <span>
        <span style="font-size: small">起始时间: </span>
        <el-date-picker
          v-model="selectedTime"
          type="datetimerange"
          size="small"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right">
        </el-date-picker>
      </span>

      <!--按照设备编号进行搜索-->
      <el-input placeholder="按照设备名称进行搜索" style="width: 270px"
                v-model="data.name" class="input-with-select" size="small">
        <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
      </el-input>

      <br>
      <el-divider></el-divider>
      <br>

      <el-table
        :data="searchResult"
        border
        stripe
        style="width: 100%">

        <el-table-column
          prop="addTime"
          label="表单提交日期"
          width="180">
        </el-table-column>
        <el-table-column
          prop="serial"
          label="设备编号"
          width="180">
        </el-table-column>
        <el-table-column
          prop="name"
          label="设备名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="type"
          label="设备型号"
          width="180">
        </el-table-column>
        <el-table-column
          prop="applicant"
          label="提交人"
          width="100">
        </el-table-column>
        <el-table-column
          label="申请部门"
          align="center"
          width="120">
          <template slot-scope="scope">
            <el-tag>{{ tagView(scope.row.department, departments) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="formType"
          label="表单类型"
          width="100"
          align="center">
          <template slot-scope="scope">
            <el-tag>{{ tagView(scope.row.formType, formTypes) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="formStatus"
          label="表单状态"
          width="100"
          align="center">
          <template slot-scope="scope">
            <el-tag>{{ tagView(scope.row.formStatus, formStatuses) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center">
          <template slot-scope="scope">
            <el-button size="small" @click="openDialog(scope.row)">详情</el-button>
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

      <!--dialog-->
      <el-dialog title="表单详情" :visible.sync="dialogVisible">
        <el-descriptions :title="tagView(formDetails.formType, this.formTypes)">
          <el-descriptions-item label="设备名称">
            {{ value(formDetails.name) }}
          </el-descriptions-item>
          <el-descriptions-item v-if="formDetails.formType != 3" label="设备编号">
            {{ value(formDetails.serial) }}
          </el-descriptions-item>
          <el-descriptions-item label="设备分类">
            {{ value(tagView(formDetails.category, categories)) }}
          </el-descriptions-item>

          <el-descriptions-item label="设备型号">
            {{ value(formDetails.type) }}
          </el-descriptions-item>
          <el-descriptions-item label="设备规格">
            {{ value(formDetails.specification) }}
          </el-descriptions-item>

          <el-descriptions-item label="设备数量">
            {{ value(formDetails.number) }}
          </el-descriptions-item>
          <el-descriptions-item label="设备单价">
            {{ value(formDetails.price) }}
          </el-descriptions-item>
          <el-descriptions-item label="设备总价">
            {{ value(formDetails.sumPrice) }}
          </el-descriptions-item>

          <el-descriptions-item label="购入日期">
            {{ value(formDetails.buyDate) }}
          </el-descriptions-item>

          <el-descriptions-item v-if="formDetails.formType == 2" label="报修日期">
            {{ value(formDetails.fixDate) }}
          </el-descriptions-item>
          <el-descriptions-item v-else-if="formDetails.formType == 1" label="报废日期">
            {{ value(formDetails.fixDate) }}
          </el-descriptions-item>


          <el-descriptions-item label="申请人">
            {{ value(formDetails.applicant) }}
          </el-descriptions-item>
          <el-descriptions-item label="申请部门">
            {{ value(tagView(formDetails.department, departments)) }}
          </el-descriptions-item>

          <br>
          <el-descriptions-item>
            <template v-if="formDetails.formType == 3" slot="label">
              设备用途:<br>
            </template>
            <template v-else-if="formDetails.formType == 2" slot="label">
              报修部位:<br>
            </template>
            <template v-else-if="formDetails.formType == 1" slot="label">
              检验依据:<br>
            </template>
            <template v-else slot="label">
              error
            </template>
            {{ value(formDetails.position) }}
          </el-descriptions-item>
          <br>

          <el-descriptions-item>
            <template v-if="formDetails.formType == 3" slot="label">
              购入理由:<br>
            </template>
            <template v-else-if="formDetails.formType == 2" slot="label">
              报修原因/故障描述:<br>
            </template>
            <template v-else-if="formDetails.formType == 1" slot="label">
              报废原因/故障描述:<br>
            </template>
            {{ value(formDetails.reason) }}
          </el-descriptions-item>
          <br>

          <el-descriptions-item label="备注">
            {{ value(formDetails.note) }}
          </el-descriptions-item>


          <el-descriptions-item v-if="showHandler" label="处理人">
            {{ value(formDetails.handlerName) }}
          </el-descriptions-item>


          <el-descriptions-item v-if="showHandler" label="处理时间">
            {{ value(formDetails.modifyTime) }}
          </el-descriptions-item>

        </el-descriptions>
        <div>


        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import {
  getAllCategories,
  getAllDepartment, getAllFormStatus,
  getAllFormType, getUserName,
  inverseTransformCategory,
  transformCategory
} from "../assets/js/DealData";
import {openTips} from "../assets/js/Tips";

window.fun;
export default {
  name: "EquipmentView",
  data() {
    return {
      showHandler: false,
      formStatuses: [],
      formDetails: {},  //用于显示dialog内的表单详情数据
      searchResult: [],
      pageSize: 6,
      pageIndex: 1,
      totalPage: 1,
      formTypes: [],
      categories: [],
      departments: [],
      data: {},
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      selectedTime: '',
      dialogVisible: false,
    }
  },
  methods: {
    openDialog(data) {
      this.dialogVisible = true;
      this.formDetails = data;
      this.getHandlerName();
      this._showHandler();
    },
    handleCurrentChange(val) {
      this.pageIndex = val;
      this.search();
    },
    tagView(id, category) {
      return inverseTransformCategory(id, category);
    },
    search() {
      console.log("search equipment");
      if (this.selectedTime != null) {
        this.data.startTime = this.selectedTime[0];
        this.data.endTime = this.selectedTime[1];
      } else {
        this.data.startTime = null;
        this.data.endTime = null;
      }
      getAllFormStatus().then(res => {
        this.formStatuses = res.data;
      });

      this.data.category = transformCategory(this.data.category, this.categories);
      this.data.department = transformCategory(this.data.department, this.departments);
      this.data.formType = transformCategory(this.data.formType, this.formTypes);
      //分页查询
      this.data.pageIndex = this.pageIndex - 1;
      this.data.pageSize = this.pageSize;
      this.$axios({
        url: 'data/search',
        method: 'post',
        data: this.data
      }).then(res => {
        console.log("search data: ");
        console.log(res.data);
        this.searchResult = res.data.data.searchResult;
        this.totalPage = res.data.data.totalPage;
      })

    },
    value(val) {
      return (val == null || val == '') ? '暂无数据' : val;
    },
    getHandlerName() {
      if (this.formDetails.handlerId == null || this.formDetails.handlerId == '') {
        return null;
      }
      const _userName = getUserName(this.formDetails.handlerId);
      _userName.then(res => {
        if (res.success) {
          this.formDetails.handlerName = res.data;
        } else {
          openTips(res.errorCode, res.msg);
          this.formDetails.handlerName = "暂无数据(内部数据错误)";
        }
      });
    },
    _showHandler() {
      if (this.formDetails.handlerId != '' && this.formDetails.handlerId != null) {
        this.showHandler = true;
        return
      }
      this.showHandler = false;
    },



  },
  created() {
    const allDepartment = getAllDepartment();
    allDepartment.then(res => {
      this.departments = res.data;
    });

    const allCategory = getAllCategories();
    allCategory.then(res => {
      this.categories = res.data;
    })

    const allFormType = getAllFormType();
    allFormType.then(res => {
      this.formTypes = res.data;
    })

  },
  computed: {

  }


}


</script>

<style scoped>
.select {
  width: 100px;
}

.abs {
  position: absolute;
  left: 100px;
}
</style>
