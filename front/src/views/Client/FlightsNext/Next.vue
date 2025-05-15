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
import type { Reserve } from '@/types/Reserve'
import { useToast } from '@/components/ui/toast'
import CancelReservationDialog from '@/components/dialogs/CancelReservationDialog.vue'
import { getClientFlightListInNext48hrs, performCheckin } from '@/clientService/ClientService'
import { formatDateTime } from '@/utils/date/formatDateTime'
import { AxiosError } from 'axios'

const { toast } = useToast()

const checkin = async (reserve: Reserve) => {
  try {
    const { data } = await performCheckin(reserve)

    toast({
      title: 'Status atualizado',
      description: `O status da reserva ${data.codigo} foi alterado para ${data.estado}.`,
      variant: 'default',
      duration: 2000,
    })
  } catch (error) {
    toast({
      title: 'Erro ao realizar checkin!',
      description:
        error instanceof AxiosError
          ? error.response?.data.message
          : 'Falha ao tentar realizar o checkin!',
      variant: 'destructive',
      duration: 2500,
    })
  }

  try {
    await setFlights()
  } catch (error) {
    toast({
      title: 'Erro ao atualizar voos!',
      description:
        error instanceof AxiosError
          ? error.response?.data.message
          : 'Falha ao tentar atualizar os voos!',
      variant: 'destructive',
      duration: 2500,
    })
  }
}

const selectedReservationcode = ref<string | null>(null)
const isCancelDialogOpen = ref(false)
const flights = ref<Reserve[]>([])

onMounted(async () => {
  setFlights()
})

async function setFlights() {
  flights.value = await getClientFlightListInNext48hrs()
}

function handleCancelFlight(selectedReservation: string) {
  isCancelDialogOpen.value = true
  selectedReservationcode.value = selectedReservation
}
</script>

<template>
  <CancelReservationDialog
    :reservation-code="selectedReservationcode"
    @success="setFlights"
    v-model="isCancelDialogOpen"
  />
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
              <template v-for="reserve in flights" :key="reserve.id">
                <TableRow>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    formatDateTime(reserve.voo.data)
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reserve.voo.aeroporto_origem.codigo
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">{{
                    reserve.voo.aeroporto_destino.codigo
                  }}</TableCell>
                  <TableCell class="text-center px-6 py-4 text-lg">
                    <span
                      :class="{
                        'text-purple-500': reserve.estado === 'CHECK-IN',
                        'text-blue-500': reserve.estado === 'CRIADA',
                      }"
                    >
                      {{ reserve.estado }}
                    </span>
                  </TableCell>
                  <TableCell class="text-center">
                    <Button
                      v-if="reserve.estado === 'CRIADA'"
                      @click="checkin(reserve)"
                      class="mr-2"
                    >
                      Check-in
                    </Button>
                    <Button
                      v-if="reserve.estado === 'CRIADA' || reserve.estado === 'CHECK-IN'"
                      variant="destructive"
                      @click="handleCancelFlight(reserve.codigo)"
                    >
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
