export default [
  { path: '/', icon: 'home', component: './', name: '主页' },
  {
    path: '/user',
    layout: false,
    routes: [
      { path: '/user/login', component: './User/Login' },
      { path: '/user/register', component: './User/Register' },
    ],
  },
  {
    path: '/generator/create',
    icon: 'plus',
    component: './Generator/Create',
    name: '创建生成器',
  },
  {
    path: '/generator/update',
    icon: 'plus',
    component: './Generator/Create',
    name: '修改生成器',
    hideInMenu: true,
  },
  {
    path: '/generator/use/:id',
    icon: 'plus',
    component: './Generator/Use',
    name: '使用生成器',
    hideInMenu: true,
  },
  {
    path: '/generator/detail/:id',
    icon: 'home',
    component: './Generator/Detail',
    name: '生成器详情',
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
