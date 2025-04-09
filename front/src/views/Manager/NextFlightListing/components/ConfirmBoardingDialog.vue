<script setup lang="ts">
import { ref, watch } from 'vue'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import type { Flight } from '@/mock/flight'
import { getReservationById, updateReservationStatus } from '@/mock/booking'

const props = defineProps<{
  modelValue: boolean
  flight: Flight | null
}>()

const emit = defineEmits(['update:modelValue'])

const reservationId = ref('')
const errorMessage = ref('')

watch(
  () => props.modelValue,
  (isOpen) => {
    if (!isOpen) {
      reservationId.value = ''
      errorMessage.value = ''
    }
  },
)

const handleConfirmBoarding = () => {
  if (!props.flight) return

  if (!reservationId.value.trim()) {
    errorMessage.value = 'Digite o ID da reserva.'
    return
  }

  const idParsed = Number(reservationId.value)
  if (isNaN(idParsed)) {
    errorMessage.value = 'ID inválido.'
    return
  }

  const reservation = getReservationById(idParsed)

  if (!reservation) {
    errorMessage.value = 'Reserva não encontrada.'
    return
  }

  if (reservation.code !== props.flight.code) {
    errorMessage.value = 'Esta reserva não pertence a este voo.'
    return
  }

  if (reservation.status !== 'CHECK-IN') {
    errorMessage.value = 'A reserva não está no estado CHECK-IN.'
    return
  }

  updateReservationStatus(reservation.id, 'EMBARCADA')
  emit('update:modelValue', false)
}

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}
</script>

<template>
  <Dialog :open="modelValue" @update:open="handleOpenChange">
    <DialogContent class="sm:max-w-md">
      <DialogHeader class="flex gap-4">
        <DialogTitle>Confirmar Embarque</DialogTitle>
        <DialogDescription>
          Digite o <strong>ID</strong> da reserva para confirmar o embarque do cliente.
        </DialogDescription>
      </DialogHeader>

      <div class="my-2">
        <input
          v-model="reservationId"
          type="number"
          placeholder="ID da reserva"
          class="w-full px-4 py-2 border rounded text-black"
        />
        <p v-if="errorMessage" class="text-red-600 mt-2 text-sm">{{ errorMessage }}</p>
      </div>

      <div class="flex justify-end space-x-2">
        <Button @click="handleConfirmBoarding" class="bg-green-600 text-white">Confirmar</Button>
        <Button @click="emit('update:modelValue', false)">Voltar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>
