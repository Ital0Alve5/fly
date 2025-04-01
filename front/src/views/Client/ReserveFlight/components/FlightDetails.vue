<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import type { Flight } from '@/mock/flight'

defineProps<{
  flight: Flight | null
}>()
</script>

<template>
  <Card v-if="flight">
    <CardHeader>
      <CardTitle>Detalhes do voo</CardTitle>
      <CardDescription>Veja mais detalhes do voo abaixo:</CardDescription>
    </CardHeader>
    <CardContent class="flex items-end gap-16">
      <ul class="flex flex-col gap-4">
        <li class="flex gap-2">
          <b>Código do voo:</b>
          <p>{{ flight.code }}</p>
        </li>
        <li class="flex gap-2">
          <b>Origem:</b>
          <p>{{ flight.origin }} ({{ flight.originAirport }})</p>
        </li>
        <li class="flex gap-2">
          <b>Destino:</b>
          <p>{{ flight.destination }} ({{ flight.destinationAirport }})</p>
        </li>
        <li class="flex gap-2">
          <b>Data e hora:</b>
          <p>{{ flight.dateTime }}</p>
        </li>
      </ul>
    </CardContent>
  </Card>
</template>
