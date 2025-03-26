<script setup lang="ts">
import router from '@/router'
import { useGlobalStore } from '@/stores/global'
import { LogOut } from 'lucide-vue-next'
import { computed } from 'vue'

const clientNavigationLinks = [
  {
    label: 'Reservas',
    link: '/reservas',
  },
]

const managerNavigationLinks = [
  {
    label: 'coisa de adm',
    link: '/admTeste',
  },
]

const globalStore = useGlobalStore()

const isUserManager = computed(() => {
  const userData = globalStore.getAuthenticatedUserData()

  return userData.value?.isManager
})

function handleLogout() {
  router.replace('/')
  globalStore.setAuthenticatedUserData(null)
}
</script>
<template>
  <header class="flex justify-end p-4 rounded-xl w-full fixed">
    <ul class="ml-auto flex gap-4">
      <li
        v-for="navigation in isUserManager ? managerNavigationLinks : clientNavigationLinks"
        :key="navigation.link"
      >
        <RouterLink :to="navigation.link" activeClass="border-b border-b-active-link-border"
          >{{ navigation.label }}
        </RouterLink>
      </li>
    </ul>
    <p class="ml-auto font-semibold">
      Olá, {{ globalStore.getAuthenticatedUserData().value?.name }}!
    </p>
    <LogOut @click="handleLogout" class="ml-6 cursor-pointer" />
  </header>
</template>
