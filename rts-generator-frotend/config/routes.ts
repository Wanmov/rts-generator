export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { path: '/user/login', component: './User/Login' },
      { path: '/user/register', component: './User/Register' },
    ],
  },
  { path: '/', icon: 'home', component: './', name: '主页' },
  {
    path: '/generator/create',
    icon: 'plus',
    component: './Generator/Create',
    name: '创建生成器',
    hideInMenu: true,
  },
  {
    path: '/test/file',
    icon: 'home',
    component: './Test/File',
    name: '文件上传下载',
    hideInMenu: true,
  },
  {
    path: '/admin',
    icon: 'crown',
    name: '管理页',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/user' },
      { icon: 'table', path: '/admin/user', component: './Admin/User', name: '用户管理' },
      {
        icon: 'table',
        path: '/admin/generator',
        component: './Admin/Generator',
        name: '生成器管理',
      },
    ],
  },
  { path: '*', layout: false, component: './404' },
];
