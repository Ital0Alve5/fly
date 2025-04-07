<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { ref, onMounted } from 'vue'
import { booking } from '@/mock/booking'
import type { Reserve } from '@/types/Reserve'
import { useRoute } from 'vue-router'

const route = useRoute()
const reservation = ref<Reserve | null>(null)

onMounted(() => {
  const foundReservation = booking.value.find(
    (res) => res.code.toLowerCase() === (route.params.code as string).toLowerCase(),
  )

  if (foundReservation) {
    reservation.value = foundReservation
  }
})
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <Card v-if="reservation" class="w-full max-w-md">
      <CardHeader>
        <CardTitle>
          Reserva do voo de {{ reservation.origin }} à {{ reservation.destination }}
        </CardTitle>
        <CardDescription>Informações de sua reserva:</CardDescription>
      </CardHeader>
      <CardContent>
        <section>
          <ul class="space-y-2">
            <li class="flex gap-2">
              <b>Data:</b>
              <p>{{ reservation.dateTimeF }}</p>
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
            <li class="flex gap-2">
              <b>Valor:</b>
              <p>
                {{
                  reservation.price.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
                }}
              </p>
            </li>
            <li class="flex gap-2">
              <b>Milhas gastas:</b>
              <p>{{ reservation.miles }}</p>
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
