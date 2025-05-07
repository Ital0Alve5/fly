<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { useToast } from '@/components/ui/toast'
import { Button } from '@/components/ui/button'
import PerformFlightDialog from './components/PerformFlightDialog.vue'
import RegisterFlightDialog from './components/RegisterFlightDialog.vue'
import { cancelReservationByFlightCode } from '@/mock/booking'
import { fetchFlightsNext48Hours, cancelFlight } from '@/views/Manager/NextFlightListing/services/NextFlightListingService.ts'

import ConfirmBoardingDialog from '@/views/Manager/NextFlightListing/components/ConfirmBoardingDialog.vue'
import CancelFlightDialog from './components/CancelFlightDialog.vue'

const isBoardingDialogOpen = ref(false)
const isCancelDialogOpen = ref(false)
const isPerformDialogOpen = ref(false)
const isRegisterFlightFormOpen = ref(false)
const selectedFlight = ref<string>('')
const generatedCode = ref('')
const createdFlight = ref(false)
const flights = ref()
const loading = ref(false)
const { toast } = useToast()

onMounted(async () => {
  await loadFlights()
})

async function loadFlights() {
  try {
    loading.value = true
    flights.value = await fetchFlightsNext48Hours()
  } catch (error) {
    console.error('Error loading flights:', error)
    toast({
      title: 'Erro ao carregar voos',
      description: 'Não foi possível carregar os voos das próximas 48 horas',
      variant: 'destructive',
    })
  } finally {
    loading.value = false
  }
}


watch([isCancelDialogOpen, isPerformDialogOpen], async ([newCancelVal, newPerformVal]) => {
  if (!newCancelVal || !newPerformVal) {
    await loadFlights()
  }
})

function handleConfirmBoarding(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isBoardingDialogOpen.value = true
}

function handleCancelFlightDialog(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isCancelDialogOpen.value = true
}

function handlePerformFlight(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isPerformDialogOpen.value = true
}

async function handleFlightRegistered(code: string) {
  await loadFlights()
  generatedCode.value = code
  createdFlight.value = true
}


async function handelCancelFlight() {
  try {
    cancelReservationByFlightCode(selectedFlight.value) // mudar para API
    const success = await cancelFlight(selectedFlight.value)
    if (success) {
      cancelFlight(selectedFlight.value)
      await loadFlights()
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
    <Dialog
      v-if="createdFlight"
      :default-open="true"
      @update:open="
        (value) => {
          if (!value) createdFlight = false
        }
      "
    >
      <DialogContent class="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Código do voo</DialogTitle>
          <DialogDescription>Código gerado unicamente para o voo cadastrado</DialogDescription>
        </DialogHeader>
        <div class="flex items-center space-x-2">
          <div class="grid flex-1 gap-2">
            <div>{{ generatedCode }}</div>
          </div>
        </div>
      </DialogContent>
    </Dialog>

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
            <TableHead>Origem</TableHead>
            <TableHead>Destino</TableHead>
            <TableHead>Status</TableHead>
            <TableHead></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="flight in flights" :key="flight.codigo">
            <TableCell>
              {{ flight.data }}
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
