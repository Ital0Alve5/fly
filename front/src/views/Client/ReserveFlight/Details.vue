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
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { useMilesStore } from '@/stores/miles'
import FlightDetails from './components/FlightDetails.vue'
import { useAuthStore } from '@/stores/auth'
import type { Flight } from '@/types/Flight'
import { fetchFlightByCode } from '@/views/Client/FlightListing/services/FlightListingService'
import { useToast } from '@/components/ui/toast'
import createReservation from './services/createReservation'
import { AxiosError } from 'axios'
import SeatSelector from './components/SeatSelector.vue'
import MilesSelector from './components/MilesSelector.vue'
import PriceSummary from './components/PriceSummary.vue'


const route = useRoute()
const router = useRouter()
const { toast } = useToast()

const milesStore = useMilesStore()
const authStore = useAuthStore()

const code = route.params.code as string
const flight = ref<Flight>({
  codigo: '',
  data: '',
  valor_passagem: 0,
  quantidade_poltronas_total: 0,
  quantidade_poltronas_ocupadas: 0,
  estado: 'CONFIRMADO',
  aeroporto_origem: {
    codigo: '',
    nome: '',
    cidade: '',
    uf: '',
  },
  aeroporto_destino: {
    codigo: '',
    nome: '',
    cidade: '',
    uf: '',
  },
})
const miles = ref(0)
const seats = ref(1)
const generatedCode = ref('')
const loading = ref(false)

onMounted(async () => {
  try {
    loading.value = true
    const response = await fetchFlightByCode(code)
    flight.value = response
  } catch (error) {
    toast({
      title: 'Erro ao obter voos',
      description: error instanceof Error ? error.message : 'Erro ao obter voos',
      variant: 'destructive',
      duration: 2500,
    })
  } finally {
    loading.value = false
  }
})

const valueToPay = computed(() => {
  if (!flight.value || flight.value.valor_passagem === undefined) return -1

  const totalPassagem = flight.value.valor_passagem * seats.value
  const desconto = miles.value * milesStore.pricePerMile
  const totalComDesconto = totalPassagem - desconto

  return totalComDesconto > 0 ? totalComDesconto : 0
})

const availableSeats = computed(() => {
  return flight.value
    ? flight.value.quantidade_poltronas_total - flight.value.quantidade_poltronas_ocupadas
    : 0
})

const availableMilesToUse = computed(() => {
  if (!flight.value) return 0

  const valorMaximoDesconto = flight.value.valor_passagem * seats.value
  const milhasNecessarias = Math.floor(valorMaximoDesconto / milesStore.pricePerMile)

  return Math.min(milhasNecessarias, milesStore.totalMiles)
})

async function handleReserveFlight() {
  try {
    const { data } = await createReservation({
      codigo_cliente: authStore.user?.usuario?.codigo as number,
      valor: valueToPay.value,
      milhas_utilizadas: miles.value,
      quantidade_poltronas: seats.value,
      codigo_voo: flight.value.codigo,
      codigo_aeroporto_origem: flight.value.aeroporto_origem.codigo,
      codigo_aeroporto_destino: flight.value.aeroporto_destino.codigo,
    })

    generatedCode.value = data.codigo
  } catch (error) {
    toast({
      title: 'Erro ao criar reserva!',
      description:
        error instanceof AxiosError
          ? error.response?.data.message
          : 'Falha ao tentar criar reserva,',
      variant: 'destructive',
      duration: 2500,
    })
  }

  await milesStore.refreshMiles()
}

watch(seats, (value) => {
  if (value > availableSeats.value) seats.value = availableSeats.value
})
watch(miles, (value) => {
  if (value > availableMilesToUse.value) miles.value = availableMilesToUse.value
})

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
    <SeatSelector v-model="seats" :max="availableSeats" />

    <MilesSelector v-model="miles" :max="availableMilesToUse" />

    <PriceSummary :amount="valueToPay" />

    <div v-if="loading" class="text-white text-lg">
      Carregando dados do voo...
    </div>

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

            <NumberField
              id="miles"
              v-model="miles"
              :min="0"
              :max="availableMilesToUse"
              :disabled="availableMilesToUse === 0"
            >
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
                :disabled="availableSeats === 0 || seats > availableSeats"
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
