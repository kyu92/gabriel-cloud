export default {
  path: '/user',
  title: '用户',
  iconSvg: 'user',
  children: [
    {path: "/user", title: "用户列表", icon: 'vcard'},
    {path: "/user/books", title: "书籍列表", icon: 'book'},
    {path: "/user/banned", title: "封禁记录", icon: "ban"}
  ]
}
