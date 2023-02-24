import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '../layouts/DefaultLayout.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: DefaultLayout,
      children: [
        {
          path: '/',
          name: 'dashboard',
          component: () => import('../views/dashboard/layouts/DashboardLayout.vue')
        },
        {
          path: '/mangas',
          name: 'manga',
          component: () => import('../views/manga/layouts/MangaLayout.vue')
        }
      ]
    }
  ]
})

export default router
