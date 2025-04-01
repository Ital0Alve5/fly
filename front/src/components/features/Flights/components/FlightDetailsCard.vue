<script setup lang="ts">
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card'
</script>
<template>
  <Card>
    <CardHeader>
      <CardTitle>Detalhes do voo</CardTitle>
      <CardDescription>Veja mais detalhes do voo abaixo:</CardDescription>
    </CardHeader>
    <CardContent>
      <ul>
        <li class="flex gap-2">
          <b>Origem:</b>
          <p>oi</p>
        </li>
        <li class="flex gap-2">
          <b>Destino:</b>
          <p></p>
        </li>
        <li class="flex gap-2">
          <b>Data e hora:</b>
          <p></p>
        </li>
        <li class="flex gap-2">
          <b>Preço do assento:</b>
          <p></p>
        </li>
      </ul>
    </CardContent>
  </Card>
</template>
