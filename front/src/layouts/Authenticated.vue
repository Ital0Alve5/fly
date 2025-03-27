<script setup lang="ts">
import Header from '@/components/layouts/Header.vue'
</script>
<template>
  <div class="flex flex-col">
    <Header />
    <main class="px-5">
      <slot />
    </main>
  </div>
</template>
