<script setup lang="ts">
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

import { type Flight, getFlightsInNext48Hours } from '@/mock/flight'
import RegisterFlightDialog from './components/RegisterFlightDialog.vue'
import { ref } from 'vue'

const registerFlightFormOpen = ref(false)
const flights = ref<Flight[]>(getFlightsInNext48Hours())

function handleFlightRegistered() {
  flights.value = getFlightsInNext48Hours()
}
</script>

<template>
  <div class="min-h-screen flex flex-col justify-center items-center">
    <RegisterFlightDialog
      @handle-flight-registered="handleFlightRegistered"
      v-model="registerFlightFormOpen"
    />
    <div @click="registerFlightFormOpen = true" class="flex justify-end mb-4 pr-8 w-full">
      <Button>Cadastrar Voo</Button>
    </div>
    <Table>
      <TableCaption>Voos das próximas 48h.</TableCaption>
      <TableHeader>
        <TableRow>
          <TableHead> Data/hora </TableHead>
          <TableHead>Origem</TableHead>
          <TableHead>Destino</TableHead>
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
          <TableCell class="w-[200px]"
            ><Button class="bg-green bg-green-500">Confirmar embarque</Button></TableCell
          >
          <TableCell class="w-[150px]"
            ><Button variant="destructive">Cancelar voo</Button></TableCell
          >
          <TableCell class="w-[150px]"><Button>Realizar voo</Button></TableCell>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>
