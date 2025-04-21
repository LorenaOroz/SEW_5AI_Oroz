import { createRouter, createWebHistory } from 'vue-router'
import Song from "@/components/Song.vue";
import Login from "@/components/Login.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/songs',
      name: 'Songs',
      component: Song,
      meta: { requiresAuth: true }
    },
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/login',
      redirect: '/'
    }
  ]
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')

  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router