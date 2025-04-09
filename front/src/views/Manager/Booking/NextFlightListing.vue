<script setup lang="ts">
import { ref } from 'vue'

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
import type { Flight } from '@/mock/flight'

import ConfirmBoardingDialog from '@/views/Manager/Booking/components/ConfirmBoardingDialog.vue'

const selectedFlight = ref<Flight | null>(null)
const isBoardingDialogOpen = ref(false)

function openBoardingDialog(flight: Flight) {
  selectedFlight.value = flight
  isBoardingDialogOpen.value = true
}
</script>

<template>
  <div class="min-h-screen flex flex-col justify-center items-center">
    <Table>
      <TableCaption>Voos das próximas 48h.</TableCaption>
      <TableHeader>
        <TableRow>
          <TableHead>Data/hora</TableHead>
          <TableHead>Origem</TableHead>
          <TableHead>Destino</TableHead>
          <TableHead>Status</TableHead>
          <TableHead colspan="3">Ações</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="flight in getFlightsInNext48Hours()" :key="flight.code">
          <TableCell>{{ flight.dateTime }}</TableCell>
          <TableCell>{{ flight.originAirport }} ({{ flight.origin }})</TableCell>
          <TableCell>{{ flight.destinationAirport }} ({{ flight.destination }})</TableCell>
          <TableCell>{{ flight.status }}</TableCell>
          <TableCell class="w-[200px]">
            <Button class="bg-green-600 text-white" @click="openBoardingDialog(flight)">
              Confirmar embarque
            </Button>
          </TableCell>
          <TableCell class="w-[150px]">
            <Button variant="destructive">Cancelar voo</Button>
          </TableCell>
          <TableCell class="w-[150px]">
            <Button>Realizar voo</Button>
          </TableCell>
        </TableRow>
      </TableBody>
    </Table>

    <ConfirmBoardingDialog
      v-if="selectedFlight"
      :key="selectedFlight.code"
      v-model="isBoardingDialogOpen"
      :flight="selectedFlight"
    />
  </div>
</template>
