<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import { computed } from 'vue'
import employeesMock from '@/mock/employees'

import Authenticated from '@/layouts/Authenticated.vue'
import Default from '@/layouts/Default.vue'

const globalStore = useGlobalStore()

const isAuthenticated = computed(() => globalStore.authenticatedUser !== null)
</script>

<template>
  <Authenticated v-if="isAuthenticated && $route.name !== 'auth'">
    <RouterView />
  </Authenticated>
  <Default v-else>
    <RouterView />
  </Default>
</template>
