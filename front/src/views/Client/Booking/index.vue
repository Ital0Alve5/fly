<script setup lang="ts">
import { ref } from 'vue';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
} from '@/components/ui/table';
import { useMilesStore } from '@/stores/miles';
import CancelDialog from '../CancelFlight/Cancel.vue';
import bookingData from '@/mock/booking';

const milesStore = useMilesStore();

const bookings = ref(bookingData);

const viewReservation = (id: number) => {
  console.log('View reservation', id);
};

const cancelDialogRef = ref<InstanceType<typeof CancelDialog> | null>(null);
const selectedReservation = ref<number | null>(null);

const openCancelDialog = (id: number) => {
  selectedReservation.value = id;
  cancelDialogRef.value?.openDialog();
};

const confirmCancellation = () => {
  if (selectedReservation.value !== null) {
    const reservation = bookings.value.find(r => r.id === selectedReservation.value);
    if (reservation) {
      reservation.status = 'CANCELADA';
      console.log(`Reservation ID ${reservation.id} status updated to: ${reservation.status}`);
    } else {
      console.log(`Reservation with ID ${selectedReservation.value} not found.`);
    }
  } else {
    console.log('No reservation selected for cancellation.');
  }
};
</script>

<template>
  <div class="flex flex-col justify-center h-screen">
    <nav class="p-1 shadow-md mt-6">
      <div class="container mx-auto flex justify-between items-center">
        <span class="text-lg">
          Saldo de Milhas: <strong>{{ milesStore.miles }}</strong>
        </span>
      </div>
    </nav>

    <div class="max-h-[500px] overflow-y-auto">
      <Card>
        <CardHeader>
          <CardTitle class="text-2xl">Minhas Reservas e Voos</CardTitle>
        </CardHeader>
        <CardContent>
          <Table class="w-full table-auto border-separate border-spacing-2">
            <TableHeader>
              <TableRow>
                <TableHead class="text-center px-6 py-3 text-lg">Data/Hora</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Origem</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Destino</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Status</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow v-for="reservation in bookings" :key="reservation.id">
                <TableCell class="text-center px-6 py-4 text-lg">{{ reservation.dataHora }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ reservation.origem }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ reservation.destino }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">
                  <span
                    :class="{
                      'text-green-500': reservation.status === 'REALIZADA',
                      'text-blue-500': reservation.status === 'CRIADA',
                      'text-red-500': reservation.status === 'CANCELADA',
                    }"
                  >
                    {{ reservation.status }}
                  </span>
                </TableCell>
                <TableCell class="text-center">
                  <Button
                    v-if="reservation.status === 'CRIADA'|| reservation.status === 'CHECK-IN'"
                    class="mr-2"
                    @click="viewReservation(reservation.id)"
                  >
                    Ver Reserva
                  </Button>
                  <Button
                    v-if="reservation.status === 'CRIADA'|| reservation.status === 'CHECK-IN'"
                    variant="destructive"
                    @click="openCancelDialog(reservation.id)"
                  >
                    Cancelar
                  </Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>

    <CancelDialog
      ref="cancelDialogRef"
      @confirm="confirmCancellation"
    />
  </div>
</template>
