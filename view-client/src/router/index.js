import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from '../views/HomeView.vue'
import AdminUsers from '../views/AdminUsers.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/admin/users',
    name: 'admin-users',
    component: AdminUsers,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

  if (to.meta.requiresAuth && !token) {
    next({ name: 'login' })
  } else if (to.meta.requiresAdmin && userInfo.roles !== 'ADMIN') {
    next({ name: 'home' })
  } else {
    next()
  }
})

export default router
