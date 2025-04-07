<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { cancelReservation } from '@/mock/booking'
import { useToast } from '@/components/ui/toast'

const props = defineProps<{
  modelValue: boolean
  reservationId: number | null
}>()

const { toast } = useToast()

const emit = defineEmits(['update:modelValue'])

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}

function handleCancelReservation() {
  cancelReservation(props.reservationId as number)
  emit('update:modelValue', false)

  toast({
    title: 'Reserva cancelada com sucesso',
    description: 'Sua reserva foi cancelada!',
    variant: 'default',
    duration: 1000,
  })
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
