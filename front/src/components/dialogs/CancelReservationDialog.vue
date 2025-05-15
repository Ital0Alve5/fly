<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { useToast } from '@/components/ui/toast'
import { cancelReservationService } from '@/views/Client/Reservation/services/reservationService'

const props = defineProps<{
  modelValue: boolean
  reservationCode: string | null
}>()

const { toast } = useToast()

const emit = defineEmits(['update:modelValue'])

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}

async function handleCancelReservation() {
  if (props.reservationCode) {
    await cancelReservationService(props.reservationCode)
    toast({
      title: 'Reserva cancelada',
      description: 'Seu cancelamento foi processado.',
      variant: 'default',
      duration: 1000,
    })
    emit('cancelled')
  }
  emit('update:modelValue', false)
}
</script>

<template>
  <Dialog :open="props.modelValue" @update:open="handleOpenChange">
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Confirmar Cancelamento</DialogTitle>
        <br />
        <DialogDescription>
          Tem certeza de que deseja cancelar esta reserva? Esta ação não pode ser desfeita.
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2">
        <Button @click="handleCancelReservation" variant="destructive">Confirmar</Button>
        <Button @click="emit('update:modelValue', false)">Voltar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>
