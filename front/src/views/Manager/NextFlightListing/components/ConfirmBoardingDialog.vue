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
import { useToast } from '@/components/ui/toast'
import { Input } from '@/components/ui/input'

import { getReservationByCodeAndFlightCode, updateReservationStatus } from '@/mock/booking'
import { getFlightByCode } from '@/mock/flight'

const props = defineProps<{
  modelValue: boolean
  selectedFlightCode: string
}>()

const emit = defineEmits(['update:modelValue'])
const { toast } = useToast()

const reservationCode = ref('')
const errorMessage = ref('')

watch(
  () => props.modelValue,
  (isOpen) => {
    if (!isOpen) {
      reservationCode.value = ''
      errorMessage.value = ''
    }
  },
)

const handleConfirmBoarding = async () => {
  try {
    if (!props.selectedFlightCode) {
      throw new Error('Código do voo não informado.')
    }

    if (!reservationCode.value.trim()) {
      throw new Error('Digite o código da reserva.')
    }

    const reservation = await getReservationByCodeAndFlightCode(
      reservationCode.value,
      props.selectedFlightCode,
    )

    if (!reservation) {
      throw new Error('Reserva não encontrada.')
    }

    if (!getFlightByCode(reservation.voo.codigo)) {
      throw new Error('Esta reserva não pertence a este voo.')
    }

    if (reservation.estado !== 'CHECK-IN') {
      throw new Error('A reserva não está no estado CHECK-IN.')
    }

    await updateReservationStatus(reservation.codigo, 'EMBARCADA')

    toast({
      title: 'Embarque confirmado',
      description: `Reserva ${reservation.codigo} marcada como EMBARCADA.`,
      variant: 'default',
      duration: 1000,
    })

    emit('update:modelValue', false)
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Erro ao confirmar embarque.'

    toast({
      title: 'Erro',
      description: errorMessage.value,
      variant: 'destructive',
      duration: 2500,
    })
  }
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
          Esta ação irá:
          <ul class="list-disc pl-5 mt-2 space-y-1">
            <li>Marcar a reserva como <strong>EMBARCADA</strong> se o check-in estiver feito.</li>
          </ul>
        </DialogDescription>
      </DialogHeader>

      <div class="my-4">
        <Input v-model="reservationCode" type="text" placeholder="ID da reserva" />
        <p v-if="errorMessage" class="text-red-600 mt-2 text-sm">{{ errorMessage }}</p>
      </div>

      <div class="flex justify-end space-x-2">
        <Button @click="handleConfirmBoarding" class="bg-green-600 hover:bg-green-700">
          Confirmar Embarque
        </Button>
        <Button variant="destructive" @click="emit('update:modelValue', false)">Cancelar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>
