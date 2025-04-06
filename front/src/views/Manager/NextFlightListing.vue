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
import { Button } from '@/components/ui/button'
import { getFlightsInNext48Hours } from '@/mock/flight'
import CancelFlightDialog from '@/components/ui/Dialogs/CancelFlightDialog.vue'
import PerformFlightDialog from '@/views/Manager/components/PerformFlightDialog.vue'

const isCancelDialogOpen = ref(false)
const isPerformDialogOpen = ref(false)
const selectedFlight = ref<string>('')
const flights = ref(getFlightsInNext48Hours())

watch([isCancelDialogOpen, isPerformDialogOpen], ([newCancelVal, newPerformVal]) => {
  if (!newCancelVal || !newPerformVal) {
    flights.value = getFlightsInNext48Hours()
  }
})

function handleCancelFlight(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isCancelDialogOpen.value = true
}

function handlePerformFlight(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isPerformDialogOpen.value = true
}

</script>

<template>
  <div>
    <CancelFlightDialog v-model="isCancelDialogOpen" :selectedFlightCode="selectedFlight" :confirmation-handler="'managerFlight'" />
    <PerformFlightDialog v-model="isPerformDialogOpen" :selectedFlightCode="selectedFlight" />
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
              <Button v-if="flight.status === 'CONFIRMADO'" variant="destructive" @click="handleCancelFlight(flight.code)">
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