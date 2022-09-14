import vue from "../../main";
import {openTips} from "./Tips";



/**
 * 获得所有设备分类
 */
export function getAllCategories() {
  return vue.$axios({
    url: 'data/allCategories',
    method: 'get',
  }).then(res => {
    return res.data;
  })
}


/**
 * 上传表单数据
 * @param data 需要进行上传的数据
 * @returns {Promise<AxiosResponse<any>>} 后端返回的报文数据，封装为Promise格式，
 * 后端返回的报文含义字段：success，data，msg，errorCode
 */
export function submitForm(data) {
  data.formStatus = 0;
  return vue.$axios({
    url: 'data/submitForm',
    method: 'post',
    data: data
  }).then(res => {
    return res.data;
  })
}

/**
 * 转化分类，将分类对应的名称转化为对应的分类码
 * @param desc  需要进行转化的分类名
 * @param Category 分类名与分类码一一对应的实体Obj
 * @returns {number|*}  转化后的分类码， 如果不存在则返回null
 */
export function transformCategory(desc, Category) {
  for (const ca of Category) {
    if (ca.desc + "" == desc + "") {
      return ca.id;
    }
  }
  console.log("没有替换对应的category字段，该category为: " + desc);
  return null;
}

/**
 * 与transformCategory功能相反，将id转化为对应的描述
 * @param id 需要进行转化的分类的id
 * @param Category id与对应的描述的实体对象
 * @constructor
 */
export function inverseTransformCategory(id, Category) {
  console.log("start inverseTransformCategory")
  for (const ca of Category) {
    if (ca.id + "" == id + "") {
      return ca.desc;
    }
  }
  console.log("没有替换对应的category字段，该category为: " + id);
  return null;
}

/**
 * 获得所有的部门分类
 * @returns {Promise<AxiosResponse<any>>} 返回后端返回的报文，以Promise对象封装
 */
export function getAllDepartment() {
  console.log("getAllDepartment");
  return vue.$axios({
    url: 'data/allDepartment',
    method: 'get'
  }).then(res => {
    return res.data;
  })
}

export function getAllFormStatus() {
  return vue.$axios({
    url: 'data/allFormStatus',
    method: 'get'
  }).then(res => {
    return res.data;
  })
}

/**
 * 获得所有的表单类别分类
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getAllFormType() {
  return vue.$axios({
    url: 'data/allFormType',
    method: 'get'
  }).then(res => {
    return res.data;
  })
}

/**
 * 获得所有的状态：账户状态/数据状态
 */
export function getAllStatus() {
  return vue.$axios
    .get('data/getAllStatus')
    .then(res => {
    return res.data;
  })
}


/**
 * 将对象obj深度拷贝
 * @param obj 需要进行拷贝的对象
 * @returns {any} 返回拷贝好的对象
 */
export function objDeepCopy(obj) {
  return JSON.parse(JSON.stringify(obj));
}

/**
 * 通过用户id获取用户名
 * @param id 用户id
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getUserName(id) {
  return vue.$axios({
    url: 'user/getUserName',
    method: 'get',
    params: {
      id: id
    },
  }).then(res => {
    return res.data;
  })
}

/**
 * 验证用户等级是否为管理者等级
 */
export function checkUserLevel() {
  vue.$axios({
    url: 'user/isHandler',
    method: 'get'
  }).then(res => {
    console.log("checkLevel");
    console.log(res.data);
    if (!res.data.success) {
      openTips("Tips", res.data.msg + ",已返回首页")
      vue.$router.push("/");
    }
  }).catch(res => {
    openTips(res.code, res.message);
    openTips("Tips", res.data.msg + ",已返回首页")
    vue.$router.push("/");
  })
}

/**
 * 获取用户信息
 * @param account 可选，不选择则获取所有指定范围内的账户信息，反之则只获取指定账户的信息
 */
export function getUsersInfo(account, size, index) {
  index--;
  return vue.$axios({
    url: 'user/getUsersInfo',
    method: 'get',
    params: {
      account: account,
      size: size,
      index: index
    }
  }).then(res => {
    return res.data;
  })
}
