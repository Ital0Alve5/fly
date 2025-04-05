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

const isCancelDialogOpen = ref(false)
const selectedFlight = ref<string>('')
const flights = ref(getFlightsInNext48Hours())

watch(isCancelDialogOpen, (newVal) => {
  if (!newVal) {
    flights.value = getFlightsInNext48Hours()
  }
})

function handleCancelFlight(selectedFlightCode: string) {
  selectedFlight.value = selectedFlightCode
  isCancelDialogOpen.value = true
}
</script>

<template>
  <div>
    <CancelFlightDialog v-model="isCancelDialogOpen" :selectedFlightCode="selectedFlight" :confirmation-handler="'managerFlight'" />
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
              <Button>Realizar voo</Button>
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  </div>
</template>