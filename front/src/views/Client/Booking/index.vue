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
import CheckReservationDialog from './components/CheckReservationDialog.vue'
import { getClientReservationList } from '@/clientService/ClientService'
import type { ReservationListResponse } from '@/types/Api'
import { formatDateTime } from '@/utils/date/formatDateTime'

const router = useRouter()
const milesStore = useMilesStore()
const booking = ref<ReservationListResponse>([])

onMounted(async () => {
  getBooking()
})

async function getBooking() {
  const result = await getClientReservationList()
  booking.value = result ?? []
}

const viewReservation = async (codigo: string) => {
  const reserva = booking.value.find((r) => r.codigo === codigo)
  if (reserva) {
    router.push({ name: 'reserva', params: { code: reserva.codigo } })
  }
}
const selectedReservationCide = ref<string | null>(null)
const isCancelDialogOpen = ref(false)
const isCheckReservationDialogOpen = ref(false)

function handleCancelFlight(selectedReservation: string) {
  isCancelDialogOpen.value = true
  selectedReservationCide.value = selectedReservation
}

function openCheckReservationDialog() {
  isCheckReservationDialogOpen.value = true
}
</script>

<template>
  <div>
    <CancelReservationDialog
      :reservation-code="selectedReservationCide as string"
      @success="getBooking"
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
                <TableRow v-for="reservation in booking" :key="reservation.codigo">
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    formatDateTime(reservation.data)
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reservation.voo.aeroporto_origem.codigo
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reservation.voo.aeroporto_destino.codigo
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">
                    <span
                      :class="{
                        'text-green-500': reservation.estado === 'REALIZADA',
                        'text-blue-500': reservation.estado === 'CRIADA',
                        'text-red-500': reservation.estado === 'CANCELADA',
                        'text-purple-500': reservation.estado === 'CHECK-IN',
                      }"
                    >
                      {{ reservation.estado }}
                    </span>
                  </TableCell>
                  <TableCell class="text-center">
                    <Button class="mr-2" @click="viewReservation(reservation.codigo)"
                      >Ver Reserva</Button
                    >
                    <Button
                      v-if="reservation.estado === 'CRIADA' || reservation.estado === 'CHECK-IN'"
                      variant="destructive"
                      @click="handleCancelFlight(reservation.codigo)"
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
