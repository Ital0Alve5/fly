<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { cancelReservation, cancelReservationByFlightCode } from '@/mock/booking'
import { cancelFlight } from '@/mock/flight';
import { useToast } from '@/components/ui/toast'

const props = defineProps<{
  modelValue: boolean
  selectedReservationId?: number
  confirmationHandler: string
  selectedFlightCode: string
}>()

const emit = defineEmits(['update:modelValue'])
const { toast } = useToast()

const handleConfirmCancelation = () => {
  switch(props.confirmationHandler) {
    case 'clientBooking':
      cancelReservation(props.selectedReservationId as number)
      emit('update:modelValue', false)
      break
    case 'managerFlight':
      cancelFlight(props.selectedFlightCode)
      cancelReservationByFlightCode(props.selectedFlightCode)
      toast({
        title: 'Voo cancelado com sucesso',
        description: 'Todas as reservas foram canceladas com sucesso.',
        variant: 'default',
        duration: 1000,
      })
      emit('update:modelValue', false)
      break
    default:
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
        <DialogTitle>Confirmar Cancelamento</DialogTitle>
        <br />
        <DialogDescription v-if="props.confirmationHandler === 'clientBooking'">
          Tem certeza de que deseja cancelar esta reserva? Esta ação não pode ser desfeita.
        </DialogDescription>
        <DialogDescription v-if="props.confirmationHandler === 'managerFlight'">
          Tem certeza de que deseja cancelar este voo? Esta ação não pode ser desfeita.
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2">
        <Button @click="handleConfirmCancelation" variant="destructive">Confirmar</Button>
        <Button @click="emit('update:modelValue', false)">Voltar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>