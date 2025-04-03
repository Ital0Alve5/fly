<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { ref, onMounted } from 'vue'
import booking from '@/mock/booking'
import type { Reserve } from '@/types/Reserve'
import { useRoute } from 'vue-router'

const route = useRoute()
const reservation = ref<Reserve | null>(null)

onMounted(() => {
  const foundReservation = booking.find(res => 
    res.codigo.toLowerCase() === (route.params.code as string).toLowerCase()
  )
  
  if (foundReservation) {
    reservation.value = foundReservation
  }
})

const formatDateTime = (dateTimeString: string) => {
  const date = new Date(dateTimeString)
  return {
    date: date.toLocaleDateString('pt-BR'),
    time: date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <Card v-if="reservation" class="w-full max-w-md">
      <CardHeader>
        <CardTitle>
          Reserva do voo de {{ reservation.origem }} à {{ reservation.destino }}
        </CardTitle>
        <CardDescription>Informações de sua reserva:</CardDescription>
      </CardHeader>
      <CardContent>
        <section>
          <ul class="space-y-2">
            <li class="flex gap-2">
              <b>Data:</b>
              <p>{{ formatDateTime(reservation.dataHora).date }} às {{ formatDateTime(reservation.dataHora).time }}</p>
            </li>
            <li class="flex gap-2">
              <b>Código:</b>
              <p>{{ reservation.codigo }}</p>
            </li>
            <li class="flex gap-2">
              <b>Origem:</b>
              <p>{{ reservation.origem }}</p>
            </li>
            <li class="flex gap-2">
              <b>Destino:</b>
              <p>{{ reservation.destino }}</p>
            </li>
            <li class="flex gap-2">
              <b>Valor:</b>
              <p>{{ reservation.valor }}</p>
            </li>
            <li class="flex gap-2">
              <b>Milhas gastas:</b>
              <p>{{ reservation.milhas }}</p>
            </li>
            <li class="flex gap-2">
              <b>Estado da reserva:</b>
              <p>{{ reservation.status }}</p>
            </li>
          </ul>
        </section>
      </CardContent>
    </Card>

    <div v-else class="text-center p-8">
      <h2 class="text-2xl font-bold mb-4">Reserva não encontrada</h2>
      <p class="text-lg">O código da reserva "{{ route.params.code }}" não foi localizado.</p>
      <p class="text-muted-foreground mt-2">Verifique o código e tente novamente.</p>
    </div>
  </div>
</template>