<script setup lang="ts">
defineProps<{
  amount: number
}>()
</script>

<template>
  <p class="text-xl">
    Valor a pagar:
    <b>{{ amount.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}</b>
  </p>
</template>
