<script setup lang="ts">
import Skeleton from '@/components/ui/skeleton/Skeleton.vue'
import type { Flight } from '@/types/Flight'

defineProps<{
  flights: Flight[]
  loading: boolean
  fetchedFlights: boolean
}>()
</script>

<template>
  <div class="grid grid-cols-12 my-4 gap-4 w-11/12 max-w-5xl">
    <template v-if="loading">
      <Skeleton class="col-span-3 h-[229px] w-full rounded-xl" v-for="number in 8" :key="number" />
    </template>
    <template v-else-if="fetchedFlights && flights.length === 0">
      <span class="text-center text-3xl mt-8 col-span-12">Nenhum vôo encontrado!</span>
    </template>
    <template v-else>
      <slot v-for="flight in flights" :flight="flight" />
    </template>
  </div>
</template>
