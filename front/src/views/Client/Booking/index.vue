<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import {
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
} from '@/components/ui/table'
import { useRouter } from 'vue-router'
import { useMilesStore } from '@/stores/miles'
import CancelReservationDialog from '@/components/dialogs/CancelReservationDialog.vue'
import { getBookingByUserId } from '@/mock/booking'
import CheckReservationDialog from './components/CheckReservationDialog.vue'
import type { Reserve } from '@/types/Reserve'

const router = useRouter()
const milesStore = useMilesStore()
const booking = ref<Reserve[]>([])

onMounted(async () => {
  booking.value = await getBookingByUserId()
})

const viewReservation = async (id: number) => {
  const reserva = booking.value.find((r) => r.id === id)
  if (reserva) {
    router.push({ name: 'reserva', params: { code: reserva.reservationCode } })
  }
}
const selectedReservationId = ref<number | null>(null)
const isCancelDialogOpen = ref(false)
const isCheckReservationDialogOpen = ref(false)

function handleCancelFlight(selectedReservation: number) {
  isCancelDialogOpen.value = true
  selectedReservationId.value = selectedReservation
}

function openCheckReservationDialog() {
  isCheckReservationDialogOpen.value = true
}
</script>

<template>
  <div>
    <CancelReservationDialog
      :reservation-id="selectedReservationId as number"
      v-model="isCancelDialogOpen"
    />
    <CheckReservationDialog v-model:open="isCheckReservationDialogOpen" />
    <div class="flex flex-col justify-center h-screen">
      <nav class="p-1 shadow-md mt-6">
        <div class="container mx-auto flex justify-between items-center">
          <span class="text-lg">
            Saldo de Milhas: <strong>{{ milesStore.totalMiles }}</strong>
          </span>
          <Button @click="openCheckReservationDialog">Consultar reserva</Button>
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
                <TableRow v-for="reservation in booking" :key="reservation.id">
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reservation.dateTimeR
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reservation.origin
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reservation.destination
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">
                    <span
                      :class="{
                        'text-green-500': reservation.status === 'REALIZADA',
                        'text-blue-500': reservation.status === 'CRIADA',
                        'text-red-500': reservation.status === 'CANCELADA',
                        'text-purple-500': reservation.status === 'CHECK-IN',
                      }"
                    >
                      {{ reservation.status }}
                    </span>
                  </TableCell>
                  <TableCell class="text-center">
                    <Button class="mr-2" @click="viewReservation(reservation.id)"
                      >Ver Reserva</Button
                    >
                    <Button
                      v-if="reservation.status === 'CRIADA' || reservation.status === 'CHECK-IN'"
                      variant="destructive"
                      @click="handleCancelFlight(reservation.id)"
                      >Cancelar</Button
                    >
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  </div>
</template>
