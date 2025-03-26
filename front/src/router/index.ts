import { createRouter, createWebHistory } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import Auth from '@/views/Auth/index.vue'
import Booking from '@/views/Client/Booking.vue'
import Adm from '@/views/Manager/PaginaAdmTeste.vue'

const routes = [
  {
    path: '/',
    name: 'auth',
    component: Auth,
  },
  {
    path: '/reservas',
    name: 'reservas',
    component: Booking,
    meta: { requiresAuth: true },
  },
  {
    path: '/admTeste',
    name: 'admTeste',
    component: Adm,
    meta: { requiresAuth: true, isManager: true },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to) => {
  const globalStore = useGlobalStore()
  const userData = globalStore.getAuthenticatedUserData()

  if (
    (!userData.value && to.meta.requiresAuth) ||
    (userData.value && !userData.value.isManager && to.meta.isManager) ||
    (userData.value && userData.value.isManager && !to.meta.isManager && to.name !== 'auth')
  ) {
    return { name: 'auth' }
  }
})

export default router
