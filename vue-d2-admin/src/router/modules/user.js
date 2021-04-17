import layoutHeaderAside from '@/layout/header-aside'

// 由于懒加载页面太多的话会造成webpack热更新太慢，所以开发环境不使用懒加载，只有生产环境使用懒加载
const _import = require('@/libs/util.import.' + process.env.NODE_ENV)

const meta = { auth: true }

export default {
  path: '/user',
  name: 'user',
  meta,
  redirect: { name: 'user-list' },
  component: layoutHeaderAside,
  children: [
    { path: 'list', name: 'user-list', component: _import('user/index.vue'), meta: { ...meta, title: '用户列表' } },
    { path: 'books', name: 'book-list', component: _import('user/books.vue'), meta: { ...meta, title: '书籍列表' } },
    { path: 'banned', name: 'user-banned-list', component: _import('user/banned.vue'), meta: { ...meta, title: '封禁记录' } },
  ]
}
