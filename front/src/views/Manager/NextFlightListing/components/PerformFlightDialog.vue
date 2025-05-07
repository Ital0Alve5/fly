<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { searchReservesByFlightCode, updateReservationStatus } from '@/mock/booking'
import { useToast } from '@/components/ui/toast'
import { performFlight } from '@/views/Manager/NextFlightListing/services/NextFlightListingService.ts'
import api from '@/lib/axios'
import type { Reserve } from '@/types/Reserve'
import { ref } from 'vue'

const props = defineProps<{
  modelValue: boolean
  selectedFlightCode: string
}>()

const emit = defineEmits(['update:modelValue'])
const { toast } = useToast()
const isLoading = ref(false)

const handleConfirmPerformance = async () => {
  try {
    isLoading.value = true

    const flightPerformed = await performFlight(props.selectedFlightCode)
    if (flightPerformed === null || flightPerformed === undefined || flightPerformed === false) {
      throw new Error('Falha ao atualizar status do voo')
    }

    performFlight(props.selectedFlightCode);

    const reservations = await searchReservesByFlightCode(props.selectedFlightCode) // mudar para api
    reservations.forEach(async (reservation) => {
      const newStatus: Reserve['estado'] = reservation.estado === 'EMBARCADA' ? 'REALIZADA' : 'NÃO REALIZADA'
      
      await updateReservationStatus(reservation.codigo, newStatus) // mudar para api
    })

    toast({
      title: 'Voo realizado com sucesso concluída com sucesso',
      description: `Voo ${props.selectedFlightCode} marcado como realizado.
                   ${reservations.length} reservas atualizadas.`,
      variant: 'default',
      duration: 1000,
    })
  } catch (error) {
    console.error('Erro ao realizar voo:', error)
    toast({
      title: 'Erro na operação',
      description: error instanceof Error ? error.message : 'Falha ao atualizar voo e reservas',
      variant: 'destructive',
      duration: 2500,
    })
  } finally {
    isLoading.value = false
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
            <li>Atualizar o status do voo para REALIZADO</li>
            <li>Atualizar o status das reservas relacionadas</li>
          </ul>
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2 mt-4">
        <Button 
          @click="handleConfirmPerformance" 
          class="bg-green-600 hover:bg-green-700"
          :disabled="isLoading"
        >
          <span v-if="isLoading">Processando...</span>
          <span v-else>Confirmar Operação</span>
        </Button>
        <Button 
          variant="destructive" 
          @click="emit('update:modelValue', false)"
          :disabled="isLoading"
        >
          Cancelar
        </Button>
      </div>
    </DialogContent>
  </Dialog>
</template>