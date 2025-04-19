<script setup lang="ts">
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput,
} from '@/components/ui/number-field'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { useMilesStore } from '@/stores/miles'
import FlightDetails from './components/FlightDetails.vue'
import { getFlightByCode, reserveSeats } from '@/mock/flight'
import { createNewReservation } from '@/mock/booking'
import { registerExtract } from '@/mock/extract'
import { useAuthStore } from '@/stores/auth'
import { getTodayDate } from '@/utils/date/getTodayDate'
import type { Flight } from '@/types/Flight'

const route = useRoute()
const router = useRouter()

const milesStore = useMilesStore()
const authStore = useAuthStore()

const code = route.params.code as string
const flight = ref<Flight | null>(getFlightByCode(code) ?? null)
const miles = ref(0)
const seats = ref(1)
const generatedCode = ref('')

const valueToPay = computed(() => {
  const discount = miles.value * milesStore.pricePerMile
  if (!flight.value || flight.value.valor_passagem === undefined) {
    return -1
  }
  const totalValue = flight.value.valor_passagem * seats.value
  return totalValue - discount
})

const availableSeats = computed(() => {
  return flight.value
    ? flight.value.quantidade_poltronas_total - flight.value.quantidade_poltronas_ocupadas
    : 0
})

const availableMilesToUse = computed(() => {
  const maxMilesToCoverFullPrice = flight.value
    ? Math.floor((flight.value.valor_passagem * seats.value) / milesStore.pricePerMile)
    : 0
  return Math.min(maxMilesToCoverFullPrice, milesStore.totalMiles)
})

async function handleReserveFlight() {
  milesStore.setTotalMiles(milesStore.totalMiles - miles.value)

  generatedCode.value = await createNewReservation({
    flight: flight.value!,
    price: valueToPay.value,
    miles: miles.value,
    seats: seats.value,
  })

  registerExtract({
    codigo_cliente: authStore.user?.usuario.codigo as number,
    data: getTodayDate(),
    codigo_reserva: generatedCode.value,
    valor_reais: valueToPay.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: miles.value,
    descricao: `${flight.value?.aeroporto_origem.codigo}->${flight.value?.aeroporto_destino.codigo}`,
    tipo: 'SAÍDA',
  })

  reserveSeats(flight.value?.codigo || '', seats.value)
}
</script>
<template>
  <Dialog
    @update:open="
      (value) => {
        if (!value) router.push('/reservas')
      }
    "
  >
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Código de reserva</DialogTitle>
        <DialogDescription>Guarde este código para pesquisar sua reserva.</DialogDescription>
      </DialogHeader>
      <div class="flex items-center space-x-2">
        <div class="grid flex-1 gap-2">
          <div>{{ generatedCode }}</div>
        </div>
      </div>
    </DialogContent>

    <div class="min-h-screen flex flex-col items-center justify-center p-20">
      <strong class="w-full max-w-[1000px] text-left mb-4 text-xl">
        Suas milhas restantes: {{ milesStore.totalMiles }}
      </strong>

      <ResizablePanelGroup
        direction="horizontal"
        class="w-full max-w-[1000px] rounded-lg border shadow-sm overflow-hidden"
      >
        <ResizablePanel :default-size="60" class="bg-black">
          <div class="flex h-full items-center justify-center p-6">
            <div class="w-full max-w-[700px]">
              <FlightDetails :flight="flight" />
            </div>
          </div>
        </ResizablePanel>

        <ResizableHandle />

        <ResizablePanel :default-size="40" class="bg-black">
          <div class="h-full flex flex-col p-6 gap-4">
            <NumberField
              id="seat"
              v-model="seats"
              :min="1"
              :max="availableSeats"
              :disabled="availableSeats === 0"
            >
              <label>Quantidade de poltronas:</label>
              <NumberFieldContent>
                <NumberFieldDecrement />
                <NumberFieldInput />
                <NumberFieldIncrement />
              </NumberFieldContent>
            </NumberField>

            <NumberField id="miles" v-model="miles" :min="0" :max="availableMilesToUse">
              <label>Milhas a usar</label>
              <NumberFieldContent>
                <NumberFieldDecrement />
                <NumberFieldInput />
                <NumberFieldIncrement />
              </NumberFieldContent>
            </NumberField>

            <p class="text-xl">
              Valor a pagar:
              <b>{{
                valueToPay.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
              }}</b>
            </p>

            <DialogTrigger as-child>
              <Button
                @click="handleReserveFlight"
                class="w-full mt-auto"
                :disabled="availableSeats === 0"
              >
                {{ availableSeats === 0 ? 'Este voo já está cheio!' : 'Pagar' }}
              </Button>
            </DialogTrigger>
          </div>
        </ResizablePanel>
      </ResizablePanelGroup>
    </div>
  </Dialog>
</template>
