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
import { getFlightByCode, type Flight } from '@/mock/flight'
import { createNewReservation } from '@/mock/booking'
import { registerExtract } from '@/mock/extract'
import { useAuthStore } from '@/stores/auth'
import { getTodayDate } from '@/utils/date/getTodayDate'

const route = useRoute()
const router = useRouter()

const milesStore = useMilesStore()
const authStore = useAuthStore()

const code = route.params.code as string
const flight = ref<Flight | null>(getFlightByCode(code) ?? null)
const miles = ref(0)
const generatedCode = ref('')

const valueToPay = computed(() => {
  const discount = miles.value * milesStore.pricePerMile
  return (flight.value?.price ?? discount) - discount
})

async function handleReserveFlight() {
  milesStore.setTotalMiles(milesStore.totalMiles - miles.value)

  generatedCode.value = await createNewReservation({
    flight: flight.value,
    price: valueToPay.value,
    miles: miles.value,
  })

  registerExtract({
    userId: authStore.user?.userId as number,
    date: getTodayDate(),
    reservationCode: generatedCode.value,
    value: valueToPay.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: miles.value,
    description: `${flight.value?.originAirport}->${flight.value?.destinationAirport}`,
    type: 'SAÍDA',
  })
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
            <NumberField id="miles" v-model="miles" :min="0" :max="milesStore.totalMiles">
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
              <Button @click="handleReserveFlight" class="w-full mt-auto"> Pagar </Button>
            </DialogTrigger>
          </div>
        </ResizablePanel>
      </ResizablePanelGroup>
    </div>
  </Dialog>
</template>
