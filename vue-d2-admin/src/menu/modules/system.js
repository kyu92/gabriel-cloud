export default {
  path: '/system',
  title: '系统',
  icon: 'gear',
  children: [
    {path: "/system/config", title: "参数设置", icon: 'pie-chart'},
    {path: "/system/storage", title: "储存空间", icon: 'folder-open'},
    {path: "/system/log", title: "系统日志", icon: "file-text-o"},
    {path: "/system/quest", title: "答疑管理", icon: "question-circle-o"},
    {path: "/system/quest/info", title: "咨询管理", icon: "at"},
  ]
}
