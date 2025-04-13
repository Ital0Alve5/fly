<script setup lang="ts">
import { ref, watch } from 'vue'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { searchReserves } from '@/mock/booking'
import type { Reserve } from '@/types/Reserve'
import { Button } from '@/components/ui/button'
import { useToast } from '@/components/ui/toast'

const { toast } = useToast()

const props = defineProps<{
  open: boolean
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
}>()

const reserveCode = ref('')
const reserveFound = ref<Reserve[]>([])

const openDialog = ref(props.open)

watch(
  () => props.open,
  (newVal) => {
    openDialog.value = newVal
  },
)

watch(openDialog, (newVal) => {
  emit('update:open', newVal)

  if (!newVal) {
    reserveCode.value = ''
    reserveFound.value = []
  }
})

const checkin = (reserva: Reserve) => {
  reserva.status = 'CHECK-IN'
  openDialog.value = false
  toast({
    title: 'Status atualizado',
    description: `O status foi alterado para ${reserva.status}.`,
    variant: 'default',
    duration: 2000,
  })
}

const toCancel = (reserva: Reserve) => {
  reserva.status = 'CANCELADA'
  openDialog.value = false
  toast({
    title: 'Status atualizado',
    description: `O status foi alterado para ${reserva.status}.`,
    variant: 'default',
    duration: 2000,
  })
}

const isWithin48Hours = (reserva: Reserve): boolean => {
  const now = new Date()
  const [datePart, timePart] = reserva.dateTimeF.split(' ')
  const [day, month, year] = datePart.split('/').map(Number)
  const [hours, minutes] = timePart.split(':').map(Number)
  const reserveDate = new Date(2000 + year, month - 1, day, hours, minutes)
  const timeDifference = (reserveDate.getTime() - now.getTime()) / (1000 * 60 * 60)
  return timeDifference <= 48 && timeDifference > 0
}

const checkReserveCode = async () => {
  if (reserveCode.value.trim()) {
    reserveFound.value = await searchReserves(reserveCode.value)

    if (reserveFound.value.length === 0) {
      toast({
        title: 'Reserva não encontrada',
        description: 'Nenhuma reserva foi encontrada com este código.',
        variant: 'default',
        duration: 2000,
      })
    }
  } else {
    toast({
      title: 'Código inválido',
      description: 'Por favor, insira um código de reserva válido.',
      variant: 'default',
      duration: 2000,
    })
  }
}
</script>

<template>
  <Dialog v-model:open="openDialog">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Consultar Reserva</DialogTitle>
        <DialogDescription>Escreva o código que recebeu para pesquisar sua reserva.</DialogDescription>
      </DialogHeader>
      <Label for="reserveCode" class="flex gap-4">Código da Reserva</Label>
      <div class="flex items-center space-x-2">
        <Input id="codigoReserva" v-model="reserveCode" class="p-2 border rounded" />
        <Button @click="checkReserveCode">Consultar</Button>
      </div>
      <div v-if="reserveFound.length > 0" class="mt-4">
        <div v-for="reserva in reserveFound" :key="reserva.id">
          <section>
            <ul class="space-y-2">
              <li class="flex gap-2">
                <b>Data:</b>
                <p>{{ reserva.dateTimeF }}</p>
              </li>
              <li class="flex gap-2">
                <b>Código:</b>
                <p>{{ reserva.reservationCode }}</p>
              </li>
              <li class="flex gap-2">
                <b>Origem:</b>
                <p>{{ reserva.origin }}</p>
              </li>
              <li class="flex gap-2">
                <b>Destino:</b>
                <p>{{ reserva.destination }}</p>
              </li>
              <li class="flex gap-2">
                <b>Valor:</b>
                <p>
                  {{
                    reserva.price.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
                  }}
                </p>
              </li>
              <li class="flex gap-2">
                <b>Milhas gastas:</b>
                <p>{{ reserva.miles }}</p>
              </li>
              <li class="flex gap-2">
                <b>Estado da reserva:</b>
                <p>{{ reserva.status }}</p>
              </li>
            </ul>
          </section>
          <div class="mt-4 flex justify-center gap-4">
            <Button
              v-if="reserva.status === 'CRIADA' && isWithin48Hours(reserva)"
              @click="checkin(reserva)"
              >Check-in</Button
            >
            <Button
              v-if="reserva.status === 'CRIADA'"
              variant="destructive"
              @click="toCancel(reserva)"
              >Cancelar</Button
            >
          </div>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
