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
import { useToast } from '@/components/ui/toast'
import { Button } from '@/components/ui/button'
import { getFlightsInNext48Hours, cancelFlight } from '@/mock/flight'
import { cancelReservationByFlightCode } from '@/mock/booking'

import CancelFlightDialog from './components/CancelFlightDialog.vue'

const isCancelDialogOpen = ref(false)
const selectedFlight = ref<string>('')
const flights = ref(getFlightsInNext48Hours())
const { toast } = useToast()

watch(isCancelDialogOpen, (newVal) => {
  if (!newVal) {
    flights.value = getFlightsInNext48Hours()
  }
})

function handleCancelFlightDialog(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isCancelDialogOpen.value = true
}

function handelCancelFlight() {
  cancelFlight(selectedFlight.value)
  cancelReservationByFlightCode(selectedFlight.value)
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
    <CancelFlightDialog v-model="isCancelDialogOpen" @confirm="handelCancelFlight" />
    <div class="min-h-screen flex flex-col justify-center items-center">
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
            <TableCell> {{ flight.destinationAirport }} </TableCell>
            <TableCell>{{ flight.originAirport }}</TableCell>
            <TableCell>{{ flight.status }}</TableCell>
            <TableCell class="w-[200px]">
              <Button class="bg-green bg-green-500">Confirmar embarque</Button>
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
              <Button>Realizar voo</Button>
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  </div>
</template>
