import Auth from '@/views/Auth/index.vue'
import Booking from '@/views/Client/Booking.vue'
import Reservation from '@/views/Client/Reservation.vue'
import Adm from '@/views/Manager/PaginaAdmTeste.vue'

import { createRouter, createWebHistory } from 'vue-router'
import { useGlobalStore } from '@/stores/global'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
   path: '/reservas/reserva/:reservationId(\\d+)'',
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
  ],
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
