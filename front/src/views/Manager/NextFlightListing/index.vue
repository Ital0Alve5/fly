<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { useToast } from '@/components/ui/toast'
import { Button } from '@/components/ui/button'
import PerformFlightDialog from './components/PerformFlightDialog.vue'
import RegisterFlightDialog from './components/RegisterFlightDialog.vue'
import { formatDateTime } from '@/utils/date/formatDateTime'
import ConfirmBoardingDialog from '@/views/Manager/NextFlightListing/components/ConfirmBoardingDialog.vue'
import CancelFlightDialog from './components/CancelFlightDialog.vue'
import getFlightsInNext48Hrs from './services/getFlightsInNext48Hrs'
import { cancelFlight } from '@/views/Manager/NextFlightListing/services/NextFlightListingService.ts'
import type { Flight } from '@/types/Flight'
import { useAuthStore } from '@/stores/auth'

const isBoardingDialogOpen = ref(false)
const isCancelDialogOpen = ref(false)
const isPerformDialogOpen = ref(false)
const isRegisterFlightFormOpen = ref(false)
const selectedFlight = ref<string>('')
const generatedCode = ref('')
const createdFlight = ref(false)
const flights = ref<Flight[]>([])
const { toast } = useToast()
const authStore = useAuthStore()

onMounted(async () => {
  if (!authStore.user?.usuario?.codigo) return

  flights.value = await getFlightsInNext48Hrs()
})

watch([isCancelDialogOpen, isPerformDialogOpen], async ([newCancelVal, newPerformVal]) => {
  if (!newCancelVal || !newPerformVal) {
    flights.value = await getFlightsInNext48Hrs()
  }
})

async function handleConfirmBoarding(selectedFlightCode: string) {
  flights.value = await getFlightsInNext48Hrs()
  selectedFlight.value = selectedFlightCode
  isBoardingDialogOpen.value = true
}

async function handleCancelFlightDialog(selectedFlightCode: string) {
  flights.value = await getFlightsInNext48Hrs()
  selectedFlight.value = selectedFlightCode
  isCancelDialogOpen.value = true
}

async function handlePerformFlight(selectedFlightCode: string) {
  flights.value = await getFlightsInNext48Hrs()
  selectedFlight.value = selectedFlightCode
  isPerformDialogOpen.value = true
}

async function handleFlightRegistered(code: string) {
  flights.value = await getFlightsInNext48Hrs()
  generatedCode.value = code
  createdFlight.value = true
}

async function handelCancelFlight() {
  try {
    const success = await cancelFlight(selectedFlight.value)
    if (success) {
      isCancelDialogOpen.value = false

      toast({
        title: 'Voo cancelado com sucesso',
        description: 'Todas as reservas deste voo foram canceladas.',
        variant: 'default',
        duration: 1000,
      })
    } else {
      throw new Error('Erro ao cancelar voo')
    }
  } catch (error) {
    toast({
      title: 'Erro ao cancelar voo',
      description: error instanceof Error ? error.message : 'Erro ao cancelar voo',
      variant: 'destructive',
      duration: 2500,
    })
  }
}
</script>

<template>
  <div>
    <RegisterFlightDialog
      @handle-flight-registered="handleFlightRegistered"
      v-model="isRegisterFlightFormOpen"
    />
    <PerformFlightDialog v-model="isPerformDialogOpen" :selectedFlightCode="selectedFlight" />
    <CancelFlightDialog v-model="isCancelDialogOpen" @confirm="handelCancelFlight" />
    <ConfirmBoardingDialog v-model="isBoardingDialogOpen" :selectedFlightCode="selectedFlight" />
    <div class="min-h-screen flex flex-col justify-center items-center">
      <div @click="isRegisterFlightFormOpen = true" class="flex justify-end mb-4 pr-8 w-full">
        <Button>Cadastrar Voo</Button>
      </div>
      <Table>
        <TableCaption>Voos das próximas 48h.</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead> Data/hora </TableHead>
            <TableHead>Código</TableHead>
            <TableHead>Origem</TableHead>
            <TableHead>Destino</TableHead>
            <TableHead>Status</TableHead>
            <TableHead></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="flight in flights" :key="flight.codigo">
            <TableCell>
              {{ formatDateTime(flight.data) }}
            </TableCell>
            <TableCell>
              {{ flight.codigo }}
            </TableCell>
            <TableCell> {{ flight.aeroporto_origem.codigo }} </TableCell>
            <TableCell>{{ flight.aeroporto_destino.codigo }}</TableCell>
            <TableCell>{{ flight.estado }}</TableCell>
            <TableCell class="w-[200px]">
              <Button
                v-if="flight.estado === 'CONFIRMADO'"
                class="bg-green bg-green-500"
                @click="handleConfirmBoarding(flight.codigo)"
                >Confirmar embarque</Button
              >
            </TableCell>
            <TableCell class="w-[150px]">
              <Button
                v-if="flight.estado === 'CONFIRMADO'"
                variant="destructive"
                @click="handleCancelFlightDialog(flight.codigo)"
              >
                Cancelar voo
              </Button>
            </TableCell>
            <TableCell class="w-[150px]">
              <Button
                v-if="flight.estado === 'CONFIRMADO'"
                @click="handlePerformFlight(flight.codigo)"
              >
                Realizar Voo
              </Button>
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  </div>
</template>
