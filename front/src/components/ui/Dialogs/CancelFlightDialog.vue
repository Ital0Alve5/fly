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

const props = defineProps<{
  modelValue: boolean
  selectedReservationId: number | null
}>()

const emit = defineEmits(['update:modelValue'])

const handleConfirmCancelation = () => {
  cancelReservation(props.selectedReservationId as number)
  emit('update:modelValue', false)
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
        <DialogDescription>
          Tem certeza de que deseja cancelar esta reserva? Esta ação não pode ser desfeita.
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2">
        <Button @click="handleConfirmCancelation" variant="destructive">Confirmar</Button>
        <Button @click="emit('update:modelValue', false)">Voltar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>