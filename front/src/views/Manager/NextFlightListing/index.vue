<script setup lang="ts">
import { ref, watch } from 'vue'
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
import PerformFlightDialog from '@/views/Manager/components/PerformFlightDialog.vue'
import RegisterFlightDialog from './components/RegisterFlightDialog.vue'
import { getFlightsInNext48Hours, cancelFlight } from '@/mock/flight'
import { cancelReservationByFlightCode } from '@/mock/booking'

import ConfirmBoardingDialog from '@/views/Manager/NextFlightListing/components/ConfirmBoardingDialog.vue'
import CancelFlightDialog from './components/CancelFlightDialog.vue'

const isBoardingDialogOpen = ref(false)
const isCancelDialogOpen = ref(false)
const isPerformDialogOpen = ref(false)
const isRegisterFlightFormOpen = ref(false)
const selectedFlight = ref<string>('')
const generatedCode = ref('')
const createdFlight = ref(false)
const flights = ref(getFlightsInNext48Hours())
const { toast } = useToast()

watch([isCancelDialogOpen, isPerformDialogOpen], ([newCancelVal, newPerformVal]) => {
  if (!newCancelVal || !newPerformVal) {
    flights.value = getFlightsInNext48Hours()
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

function handleFlightRegistered(code: string) {
  flights.value = getFlightsInNext48Hours()
  generatedCode.value = code
  createdFlight.value = true
}

function handelCancelFlight() {
  cancelFlight(selectedFlight.value)
  cancelReservationByFlightCode(selectedFlight.value)
  flights.value = getFlightsInNext48Hours()
  isCancelDialogOpen.value = false

  toast({
    title: 'Voo cancelado com sucesso',
    description: 'Todas as reservas deste voo foram canceladas.',
    variant: 'default',
    duration: 1000,
  })
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
          <TableRow v-for="flight in flights" :key="flight.code">
            <TableCell>
              {{ flight.dateTime }}
            </TableCell>
            <TableCell> {{ flight.originAirport }} </TableCell>
            <TableCell>{{ flight.destinationAirport }}</TableCell>
            <TableCell>{{ flight.status }}</TableCell>
            <TableCell class="w-[200px]">
              <Button
                v-if="(flight.status !== 'REALIZADO') | 'CANCELADO'"
                class="bg-green bg-green-500"
                @click="handleConfirmBoarding(flight.code)"
                >Confirmar embarque</Button
              >
            </TableCell>
            <TableCell class="w-[150px]">
              <Button
                v-if="flight.status === 'CONFIRMADO'"
                variant="destructive"
                @click="handleCancelFlightDialog(flight.code)"
              >
                Cancelar voo
              </Button>
            </TableCell>
            <TableCell class="w-[150px]">
              <Button
                v-if="flight.status === 'CONFIRMADO'"
                @click="handlePerformFlight(flight.code)"
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
