<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import type { Flight } from '@/types/Flight'
import { useRouter } from 'vue-router'
import { formatDateTime } from '@/utils/date/formatDateTime';

const props = defineProps<{
  flight: Flight
}>()

const router = useRouter()

function handleClickCard() {
  router.push({ name: 'flightDetails', params: { code: props.flight.codigo } })
}
</script>

<template>
  <Card
    @click="handleClickCard"
    class="w-full max-w-md col-span-3 cursor-pointer hover:opacity-80 transition-opacity"
  >
    <CardHeader>
      <CardTitle
        >Voo de {{ flight.aeroporto_origem.codigo }} à
        {{ flight.aeroporto_destino.codigo }}</CardTitle
      >
      <CardDescription>Informações do vôo:</CardDescription>
    </CardHeader>
    <CardContent>
      <section>
        <ul class="space-y-2">
          <li class="flex gap-2">
            <b>Data:</b>
            <p>{{ formatDateTime(flight.data) }}</p>
          </li>
          <li class="flex gap-2">
            <b>Código:</b>
            <p>{{ flight.codigo }}</p>
          </li>
          <li class="flex gap-2">
            <b>Origem:</b>
            <p>{{ flight.aeroporto_origem.cidade }}</p>
          </li>
          <li class="flex gap-2">
            <b>Destino:</b>
            <p>{{ flight.aeroporto_destino.cidade }}</p>
          </li>
        </ul>
      </section>
    </CardContent>
  </Card>
</template>
