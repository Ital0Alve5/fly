<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import { computed } from 'vue'

import Authenticated from '@/layouts/Authenticated.vue'
import Default from '@/layouts/Default.vue'

const globalStore = useGlobalStore()

// globalStore.setAuthenticatedUserData({
//   name: 'Adm',
//   email: 'string',
//   isManager: true,
// })

// globalStore.setAuthenticatedUserData({
// name: 'italo',
// email: 'string',
// isManager: false,
// })

const isAuthenticated = computed(() => {
  const userData = globalStore.getAuthenticatedUserData()

  return !!userData.value
})
</script>

<template>
  <Authenticated v-if="isAuthenticated && $route.name !== 'auth'">
    <RouterView />
  </Authenticated>
  <Default v-else> <RouterView /> </Default>
</template>
