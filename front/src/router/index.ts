import Auth from '@/views/Auth/index.vue'
import Booking from '@/views/Client/Booking/index.vue'
import Reservation from '@/views/Client/Reservation/index.vue'
import Search from '@/views/Client/FlightListing/Search.vue'
import BuyMiles from '@/views/Client/BuyMiles/index.vue'
import Details from '@/views/Client/ReserveFlight/Details.vue'

import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import ExtractMiles from '@/views/Client/ExtractMiles/index.vue'
import NextFlightListing from '@/views/Manager/NextFlightListing.vue'

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
      path: '/reservas/reserva/:code',
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
      component: Details,
      meta: { requiresAuth: true },
    },
    {
      path: '/comprar-milhas',
      name: 'comprarMilhas',
      component: BuyMiles,
      meta: { requiresAuth: true },
    },
    {
      path: '/extrato-milhas',
      name: 'extratoDeMilhas',
      component: ExtractMiles,
      meta: { requiresAuth: true },
    },
    {
      path: '/adm',
      meta: { requiresAuth: true, isManager: true },
      children: [
        {
          path: 'voos',
          name: 'admVoos',
          component: NextFlightListing,
        },
      ],
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
