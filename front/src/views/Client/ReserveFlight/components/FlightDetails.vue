<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import type { Flight } from '@/types/Flight'
import { formatDateTime } from '@/utils/date/formatDateTime'

defineProps<{
  flight: Flight | null
  valueToPay?: number
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
          <p>{{ flight.codigo }}</p>
        </li>
        <li class="flex gap-2">
          <b>Origem:</b>
          <p>{{ flight.aeroporto_origem.cidade }} ({{ flight.aeroporto_origem.codigo }})</p>
        </li>
        <li class="flex gap-2">
          <b>Destino:</b>
          <p>{{ flight.aeroporto_destino.cidade }} ({{ flight.aeroporto_destino.codigo }})</p>
        </li>
        <li class="flex gap-2">
          <b>Data e hora:</b>
          <p>{{formatDateTime(flight.data)}}</p>
        </li>
        <li class="flex gap-2">
          <b>Preço da poltrona:</b>
          <p>
            {{
              flight.valor_passagem.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            }}
          </p>
        </li>
        <li class="flex gap-2">
          <b>Quantidade de milhas necessárias:</b>
          <p>{{ Math.floor((valueToPay ?? 0) / 5) }}</p>
        </li>
      </ul>
    </CardContent>
  </Card>
</template>
