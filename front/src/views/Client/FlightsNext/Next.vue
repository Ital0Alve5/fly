<script setup lang="ts">
import { ref } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Table, TableHead, TableRow, TableHeader, TableBody, TableCell } from '@/components/ui/table'
import booking from '@/mock/booking'
import type { Reserve } from '@/types/Reserve'
import { useToast } from '@/components/ui/toast'
import CancelFlightDialog from '@/views/Client/Booking/components/CancelFlightDialog.vue'
import formatDateTime from '@/utils/date/formatDateTime'

const { toast } = useToast()

const checkin = (reserve: Reserve) => {
    reserve.status = 'CHECK-IN'
    toast({
      title: 'Status atualizado',
      description: `O status foi alterado para ${reserve.status}.`,
      variant: 'default',
      duration: 2000,
  })
}

const isWithin48Hours = (reserve: Reserve): boolean => {
  const now = new Date()
  const reserveDate = new Date(reserve.dateTimeF)
  const timeDifference = (reserveDate.getTime() - now.getTime()) / (1000 * 60 * 60)
  return timeDifference <= 48 && timeDifference > 0
}

const shouldShowReservation = (reserve: Reserve): boolean => {
  return (reserve.status === 'CRIADA' || reserve.status === 'CHECK-IN') && 
         isWithin48Hours(reserve)
}

const selectedReservationId = ref<number | null>(null)
const isCancelDialogOpen = ref(false)

function handleCancelFlight(selectedReservation: number) {
  isCancelDialogOpen.value = true
  selectedReservationId.value = selectedReservation
}
</script>

<template>
    <CancelFlightDialog v-model="isCancelDialogOpen" :selectedReservationId="selectedReservationId" />
    <div class="flex flex-col justify-center h-screen">
      <div class="max-h-[500px] overflow-y-auto">
        <Card>
          <CardHeader>
            <CardTitle class="text-2xl">Voos reservados para as próximas 48h</CardTitle>
          </CardHeader>
          <CardContent>
            <Table class="w-full table-auto border-separate border-spacing-2">
              <TableHeader>
                <TableRow>
                  <TableHead class="text-center px-6 py-3 text-lg">Data/Hora</TableHead>
                  <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Origem</TableHead>
                  <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Destino</TableHead>
                  <TableHead class="text-center px-6 py-3 text-lg">Status</TableHead>
                  <TableHead class="text-center px-6 py-3 text-lg">Ação</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                <template v-for="reserve in booking" :key="reserve.id">
                  <TableRow v-if="shouldShowReservation(reserve)">
                    <TableCell class="text-center px-6 py-4 text-lg">{{
                        formatDateTime(reserve.dateTimeF)
                    }}</TableCell>
                    <TableCell class="text-center px-6 py-4 text-lg">{{
                        reserve.origin
                    }}</TableCell>
                    <TableCell class="text-center px-6 py-4 text-lg">{{
                        reserve.destination
                    }}</TableCell>
                    <TableCell class="text-center px-6 py-4 text-lg">
                        <span
                        :class="{
                            'text-purple-500': reserve.status === 'CHECK-IN',
                            'text-blue-500': reserve.status === 'CRIADA',
                        }"
                        >
                        {{ reserve.status }}
                        </span>
                    </TableCell>
                    <TableCell class="text-center">
                        <Button 
                          v-if="reserve.status === 'CRIADA'" 
                          @click="checkin(reserve)" 
                          class="mr-2">
                            Check-in
                        </Button>
                        <Button
                          v-if="reserve.status === 'CRIADA' || reserve.status === 'CHECK-IN'"
                          variant="destructive"
                          @click="handleCancelFlight(reserve.id)">
                            Cancelar
                        </Button>
                    </TableCell>
                  </TableRow>
                </template>
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
</template>