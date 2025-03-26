<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import { computed } from 'vue'
// import clientsMock from '@/mock/clients'
import employeesMock from '@/mock/employees'

import Authenticated from '@/layouts/Authenticated.vue'
import Default from '@/layouts/Default.vue'

const globalStore = useGlobalStore()

// Simular login como funcionário administrador
const adm = employeesMock.getEmployeeByEmail('italo.zedelinski@empresa.com')
if (adm) globalStore.setAuthenticatedUserData(adm)

// Simular login como cliente
// const client = clientsMock.getClientByEmail('plinta.pagode@example.com')
// if (client) globalStore.setAuthenticatedUserData(client)

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
