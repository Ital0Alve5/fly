import { createRouter, createWebHistory } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import Auth from '@/views/Auth/index.vue'
import Booking from '@/views/Client/Booking.vue'
import Reservation from '@/views/Client/Reservation.vue'
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
      path: '/reservas/reserva/:reservationId(\\d+)',
      name: 'reserva',
      component: Reservation,
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
    (!userData && to.meta.requiresAuth) ||
    (userData && !userData.isManager && to.meta.isManager) ||
    (userData && userData.isManager && !to.meta.isManager && to.name !== 'auth')
  ) {
    return { name: 'auth' }
  }
})

export default router
