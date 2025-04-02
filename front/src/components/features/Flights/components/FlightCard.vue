<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

defineProps<{
  flight: {
    originAirport: string
    destinationAirport: string
    dateTime: string
    code: string
    origin: string
    destination: string
  }
}>()

function handleClickCard(){

}
</script>

<template>
  <Card @click="handleClickCard" class="w-full max-w-md col-span-3 cursor-pointer hover:opacity-80 transition-opacity">
    <CardHeader>
      <CardTitle>Voo de {{ flight.originAirport }} à {{ flight.destinationAirport }}</CardTitle>
      <CardDescription>Informações do vôo:</CardDescription>
    </CardHeader>
    <CardContent>
      <section>
        <ul class="space-y-2">
          <li class="flex gap-2">
            <b>Data:</b>
            <p>{{ flight.dateTime }}</p>
          </li>
          <li class="flex gap-2">
            <b>Código:</b>
            <p>{{ flight.code }}</p>
          </li>
          <li class="flex gap-2">
            <b>Origem:</b>
            <p>{{ flight.origin }}</p>
          </li>
          <li class="flex gap-2">
            <b>Destino:</b>
            <p>{{ flight.destination }}</p>
          </li>
        </ul>
      </section>
    </CardContent>
  </Card>
</template>
