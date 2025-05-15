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
import type { Reserve } from '@/types/Reserve'
import { Button } from '@/components/ui/button'
import { useToast } from '@/components/ui/toast'
import getReservationDetails from '@/views/Client/Reservation/services/getReservationDetails'
import { AxiosError } from 'axios'
import { formatDateTime } from '@/utils/date/formatDateTime'

const { toast } = useToast()

const props = defineProps<{
  open: boolean
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
}>()

const reserveBlank: Reserve = {
  codigo: '',
  codigo_cliente: 0,
  estado: 'CRIADA',
  data: '',
  valor: 0,
  milhas_utilizadas: 0,
  quantidade_poltronas: 0,
  voo: {
    codigo: '',
    data: '',
    valor_passagem: 0,
    quantidade_poltronas_total: 0,
    quantidade_poltronas_ocupadas: 0,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: '',
      nome: '',
      cidade: '',
      uf: '',
    },
    aeroporto_destino: {
      codigo: '',
      nome: '',
      cidade: '',
      uf: '',
    },
  },
}

const reserveCode = ref('')
const reserveFound = ref<Reserve | null>(null)
const isWithin48Hours = ref(false)
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
    reserveFound.value = reserveBlank
  }
})

const checkin = (reserva: Reserve) => {
  reserva.estado = 'CHECK-IN'
  openDialog.value = false
  toast({
    title: 'Status atualizado',
    description: `O status foi alterado para ${reserva.estado}.`,
    variant: 'default',
    duration: 2000,
  })
}

const toCancel = (reserva: Reserve) => {
  reserva.estado = 'CANCELADA'
  openDialog.value = false
  toast({
    title: 'Status atualizado',
    description: `O status foi alterado para ${reserva.estado}.`,
    variant: 'default',
    duration: 2000,
  })
}

const setIsWithin48Hours = (reserva: Reserve) => {
  if (!reserva.codigo) throw new Error()

  const inputDate = new Date(reserva.data)
  const now = new Date()
  const timeDifference = inputDate.getTime() - now.getTime()
  const hours48 = 48 * 60 * 60 * 1000

  isWithin48Hours.value = timeDifference >= 0 && timeDifference <= hours48
}

const checkReserveCode = async () => {
  if (reserveCode.value.trim()) {
    try {
      const { data } = await getReservationDetails(reserveCode.value)
      setIsWithin48Hours(data)
      reserveFound.value = data
    } catch (error) {
      console.log(error)
      toast({
        title: 'Erro ao encontrar reserva',
        description:
          error instanceof AxiosError
            ? error.response?.data.message
            : 'Falha ao tentar encontrar a reserva.',
        variant: 'destructive',
        duration: 2500,
      })
    }

    if (reserveFound.value) {
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
        <DialogDescription
          >Escreva o código que recebeu para pesquisar sua reserva.</DialogDescription
        >
      </DialogHeader>
      <Label for="reserveCode" class="flex gap-4">Código da Reserva</Label>
      <div class="flex items-center space-x-2">
        <Input id="codigoReserva" v-model="reserveCode" class="p-2 border rounded" />
        <Button @click="checkReserveCode">Consultar</Button>
      </div>
      <div v-if="reserveFound" class="mt-4">
        <div>
          <section>
            <ul class="space-y-2">
              <li class="flex gap-2">
                <b>Data:</b>
                <p>{{ formatDateTime(reserveFound.voo.data) }}</p>
              </li>
              <li class="flex gap-2">
                <b>Código:</b>
                <p>{{ reserveFound.codigo }}</p>
              </li>
              <li class="flex gap-2">
                <b>Origem:</b>
                <p>{{ reserveFound.voo.aeroporto_origem.nome }}</p>
              </li>
              <li class="flex gap-2">
                <b>Destino:</b>
                <p>{{ reserveFound.voo.aeroporto_destino.nome }}</p>
              </li>
              <li class="flex gap-2">
                <b>Valor:</b>
                <p>
                  {{
                    reserveFound.valor.toLocaleString('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    })
                  }}
                </p>
              </li>
              <li class="flex gap-2">
                <b>Milhas gastas:</b>
                <p>{{ reserveFound.milhas_utilizadas }}</p>
              </li>
              <li class="flex gap-2">
                <b>Estado da reserva:</b>
                <p>{{ reserveFound.estado }}</p>
              </li>
            </ul>
          </section>
          <div class="mt-4 flex justify-center gap-4">
            <Button
              v-if="reserveFound.estado === 'CRIADA' && isWithin48Hours"
              @click="checkin(reserveFound)"
              >Check-in</Button
            >
            <Button
              v-if="reserveFound.estado === 'CRIADA'"
              variant="destructive"
              @click="toCancel(reserveFound)"
              >Cancelar</Button
            >
          </div>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
