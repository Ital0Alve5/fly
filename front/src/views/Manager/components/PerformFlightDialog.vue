<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { performFlight, getFlightByCode } from '@/mock/flight'
import { searchReservesByFlightCode, updateReservationStatus } from '@/mock/booking'
import { useToast } from '@/components/ui/toast'
import type { Reserve } from '@/types/Reserve'

const props = defineProps<{
  modelValue: boolean
  selectedFlightCode: string
}>()

const emit = defineEmits(['update:modelValue'])
const { toast } = useToast()

const handleConfirmPerformance = async () => {
  try {
    const flight = getFlightByCode(props.selectedFlightCode)
    if (!flight) {
      throw new Error('Voo não encontrado')
    }

    performFlight(props.selectedFlightCode)

    console.log(props.selectedFlightCode)

    const reservations = await searchReservesByFlightCode(props.selectedFlightCode)

    console.log(reservations)

    reservations.forEach(async (reservation) => {
      const newStatus: Reserve['status'] =
        reservation.status === 'EMBARCADA' ? 'REALIZADA' : 'NÃO REALIZADA'

      await updateReservationStatus(reservation.reservationCode, newStatus)
    })

    toast({
      title: 'Operação concluída com sucesso',
      description: `Voo ${flight.code} marcado como realizado.
                   ${reservations.length} reservas atualizadas.`,
      variant: 'default',
      duration: 1000,
    })
  } catch (error) {
    toast({
      title: 'Erro na operação',
      description: error instanceof Error ? error.message : 'Falha ao atualizar voo e reservas',
      variant: 'destructive',
      duration: 2500,
    })
  } finally {
    emit('update:modelValue', false)
  }
}

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}
</script>

<template>
  <Dialog :open="props.modelValue" @update:open="handleOpenChange">
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Confirmar Realização do Voo</DialogTitle>
        <br />
        <DialogDescription>
          Esta ação irá:
          <ul class="list-disc pl-5 mt-2 space-y-1">
            <li>Atualizar o status da reserva aplicada.</li>
          </ul>
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2 mt-4">
        <Button @click="handleConfirmPerformance" class="bg-green-600 hover:bg-green-700">
          Confirmar Operação
        </Button>
        <Button variant="destructive" @click="emit('update:modelValue', false)">Cancelar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>
