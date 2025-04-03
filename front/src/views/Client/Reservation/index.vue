<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.push('/reservas')
}

const reservation = ref({
  date: '05/03/2025 14:20:45',
  time: '14:30',
  code: 'ABC123',
  origin: 'Curitiba',
  destination: 'Havana',
  originAirport: 'CWB',
  destinationAirport: 'CUB',
  price: 'R$ 450,00',
  milesSpent: 15000,
  status: 'Confirmado',
})
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <button @click="goBack" class="absolute top-16 left-6 text-white px-4 py-2">← Voltar</button>
    <Card class="w-full max-w-md">
      <CardHeader>
        <CardTitle
          >Reserva do voo de {{ reservation.originAirport }} à
          {{ reservation.destinationAirport }}</CardTitle
        >
        <CardDescription>Informações de sua reserva:</CardDescription>
      </CardHeader>
      <CardContent>
        <section>
          <ul class="space-y-2">
            <li class="flex gap-2">
              <b>Data:</b>
              <p>{{ reservation.date }} às {{ reservation.time }}</p>
            </li>
            <li class="flex gap-2">
              <b>Código:</b>
              <p>{{ reservation.code }}</p>
            </li>
            <li class="flex gap-2">
              <b>Origem:</b>
              <p>{{ reservation.origin }}</p>
            </li>
            <li class="flex gap-2">
              <b>Destino:</b>
              <p>{{ reservation.destination }}</p>
            </li>
            <li class="flex gDateap-2">
              <b>Valor:</b>
              <p>{{ reservation.price }}</p>
            </li>
            <li class="flex gap-2">
              <b>Milhas gastas:</b>
              <p>{{ reservation.milesSpent }}</p>
            </li>
            <li class="flex gap-2">
              <b>Estado da reserva:</b>
              <p>{{ reservation.status }}</p>
            </li>
          </ul>
        </section>
      </CardContent>
    </Card>
  </div>
</template>
