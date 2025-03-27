<script setup lang="ts">
import Header from '@/components/layouts/Header.vue'
</script>
<template>
  <div class="flex flex-col">
    <Header />
    <main class="px-5 min-h-screen">
      <slot />
    </main>
  </div>
</template>
