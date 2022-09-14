import vue  from "../../main";

export function openTips(title, msg) {
  const h = vue.$createElement;
  vue.$notify({
    title: title,
    message: h('i', {style: 'color: teal'}, msg)
  });
}

/**
 * @param title title
 * @param msg msg
 * @param type 类型：success | warning
 */
export function openTipsByType(title, msg, type) {
  this.$notify({
    title: title,
    message: msg,
    type: type
  });
}
