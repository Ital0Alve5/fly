<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Toaster from '@/components/ui/toast/Toaster.vue'

import Authenticated from '@/layouts/Authenticated.vue'
import Default from '@/layouts/Default.vue'
import { useNotificationListener } from '@/composables/useNotificationListener'

useNotificationListener()

const authStore = useAuthStore()
</script>

<template>
  <Toaster />

  <Authenticated v-if="authStore.isAuthenticated && $route.name !== 'auth'">
    <RouterView />
  </Authenticated>
  <Default v-else>
    <RouterView />
  </Default>
</template>
