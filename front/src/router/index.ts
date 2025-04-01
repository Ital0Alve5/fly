import Auth from '@/views/Auth/index.vue'
import Booking from '@/views/Client/Booking.vue'
import Reservation from '@/views/Client/Reservation.vue'
import Adm from '@/views/Manager/PaginaAdmTeste.vue'
import Search from '@/views/Flights/Search.vue'
import details from '@/views/Flights/Details.vue'

import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

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
      path: '/reservas/reserva/:reservationId(\\d+)',
      name: 'reserva',
      component: Reservation,
      meta: { requiresAuth: true },
    },
    {
      path: '/voos',
      name: 'voos',
      component: Search,
      meta: { requiresAuth: true },
    },
    {
      path: '/voos/voo/:code',
      name: 'flightDetails',
      component: details,
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
  const authStore = useAuthStore()

  if (
    (!authStore.user && to.meta.requiresAuth) ||
    (authStore.user && !authStore.user.isManager && to.meta.isManager) ||
    (authStore.user && authStore.user.isManager && !to.meta.isManager && to.name !== 'auth')
  ) {
    return { name: 'auth' }
  }
})

export default router
