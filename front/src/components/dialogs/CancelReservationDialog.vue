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
import { AxiosError } from 'axios'
import cancelReservation from '@/views/Client/Booking/services/cancelReservation'

const props = defineProps<{
  modelValue: boolean
  reservationCode: string | null
}>()

const { toast } = useToast()

const emit = defineEmits(['update:modelValue', 'success'])

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}

async function handleCancelReservation() {
  if (!props.reservationCode) return

  try {
    await cancelReservation(props.reservationCode)

    toast({
      title: 'Reserva cancelada com sucesso',
      description: 'Sua reserva foi cancelada!',
      variant: 'default',
      duration: 1000,
    })

    emit('success')
  } catch (error) {
    toast({
      title: 'Erro ao cancelar reserva!',
      description:
        error instanceof AxiosError ? error.response?.data.message : 'Falha ao tentar cancelar!',
      variant: 'destructive',
      duration: 2500,
    })
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
