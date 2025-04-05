<script setup lang="ts">
import { ref } from 'vue'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import type { Flight } from '@/mock/flight'
import { getReservationByCode, updateReservationStatus } from '@/mock/booking'

const props = defineProps<{
  modelValue: boolean
  flight: Flight | null
}>()

const emit = defineEmits(['update:modelValue'])

const reservationCode = ref('')
const errorMessage = ref('')

const handleConfirmBoarding = () => {
  if (!props.flight) return

  const reservation = getReservationByCode(reservationCode.value)

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

  updateReservationStatus(reservation.code, 'EMBARCADO')
  emit('update:modelValue', false)
  reservationCode.value = ''
  errorMessage.value = ''
}
</script>

<template>
  <Dialog :open="modelValue">
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Confirmar Embarque</DialogTitle>
        <br />
        <DialogDescription>
          Digite o código da reserva para confirmar o embarque do cliente.
        </DialogDescription>
      </DialogHeader>

      <div class="my-2">
        <input
          v-model="reservationCode"
          type="text"
          placeholder="Código da reserva"
          class="w-full px-4 py-2 border rounded"
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
