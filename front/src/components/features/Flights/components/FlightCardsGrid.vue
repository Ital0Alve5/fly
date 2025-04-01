<script setup lang="ts">
import Skeleton from '@/components/ui/skeleton/Skeleton.vue'
import type { Flight } from '@/mock/flight'

defineProps<{
  flights: Flight[]
  loading: boolean
}>()
</script>

<template>
  <div class="grid grid-cols-12 mt-4 gap-4 w-11/12 max-w-5xl">
    <template v-if="loading">
      <Skeleton class="col-span-3 h-[229px] w-full rounded-xl" v-for="number in 8" />
    </template>
    <template v-else>
      <slot v-for="flight in flights" :flight="flight" />
    </template>
  </div>
</template>
