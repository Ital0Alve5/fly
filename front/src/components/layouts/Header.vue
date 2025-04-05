<script setup lang="ts">
import router from '@/router'
import { useAuthStore } from '@/stores/auth'
import { LogOut } from 'lucide-vue-next'

type NavigationItem = {
  label: string
  link?: string
  action?: string
}

const clientNavigationLinks: NavigationItem[] = [
  {
    label: 'Reservas',
    link: '/reservas',
  },
  {
    label: 'Fazer reserva',
    link: '/voos',
  },
  {
    label: 'Comprar milhas',
    link: '/comprar-milhas',
  },
  {
    label: 'Extrato de milhas',
    link: '/extrato-milhas',
  },
  {
    label: 'Fazer Check-in',
    link: '/fazer-check-in'
  }
]

const managerNavigationLinks: NavigationItem[] = [
  {
    label: 'coisa de adm',
    link: '/admTeste',
  },
]

const authStore = useAuthStore()

function handleLogout() {
  router.replace('/')
  authStore.logout()
}
</script>

<template>
  <header class="flex justify-end p-4 rounded-xl fixed w-full">
    <ul class="ml-auto flex gap-4">
      <li
        v-for="navigation in authStore.user?.isManager
          ? managerNavigationLinks
          : clientNavigationLinks"
        :key="navigation.link || navigation.label"
      >
        <RouterLink
          v-if="navigation.link"
          :to="navigation.link"
          activeClass="border-b border-b-active-link-border"
        >
          {{ navigation.label }}
        </RouterLink>
      </li>
    </ul>
    <p class="ml-auto font-semibold">Olá, {{ authStore.user?.name.split(' ')[0] }}!</p>
    <LogOut @click="handleLogout" class="ml-6 cursor-pointer" />
  </header>
</template>
