<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import {
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
} from '@/components/ui/table'

import { ref, onMounted } from 'vue'
import type { History, Reserve } from '@/types/Reserve'
import { useRoute } from 'vue-router'
import getReservationDetails from './services/getReservationDetails'
import { AxiosError } from 'axios'
import { toast } from '@/components/ui/toast'
import { formatDateTime } from '@/utils/date/formatDateTime'
import { useAuthStore } from '@/stores/auth'
import getReservationHistory from './services/getReservationHistory'

const authStore = useAuthStore()
const route = useRoute()
const reservation = ref<Reserve>({
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
})

const history = ref<History>([])

onMounted(async () => {
  if (authStore.isAuthenticated) {
    try {
      const { data } = await getReservationDetails(route.params.code.toString())
      const { data: historyData } = await getReservationHistory(route.params.code.toString())
      reservation.value = data
      history.value = historyData
      history.value.shift()
    } catch (error) {
      toast({
        title: 'Erro ao acessar reserva',
        description:
          error instanceof AxiosError
            ? error.response?.data.message
            : 'Falha ao tentar acessar a reserva',
        variant: 'destructive',
        duration: 2500,
      })
    }
  }
})
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <Card v-if="reservation" class="w-full max-w-2xl">
      <CardHeader>
        <CardTitle>
          Reserva do voo de {{ reservation.voo.aeroporto_origem.codigo }} à
          {{ reservation.voo.aeroporto_destino.codigo }}
        </CardTitle>
        <CardDescription>Informações de sua reserva:</CardDescription>
      </CardHeader>
      <CardContent>
        <section class="mb-2">
          <ul class="space-y-2">
            <li class="flex gap-2">
              <b>Data do voo:</b>
              <p>{{ formatDateTime(reservation.voo.data) }}</p>
            </li>
            <li class="flex gap-2">
              <b>Data da reserva:</b>
              <p>{{ formatDateTime(reservation.data) }}</p>
            </li>
            <li class="flex gap-2">
              <b>Código:</b>
              <p>{{ reservation.codigo }}</p>
            </li>
            <li class="flex gap-2">
              <b>Origem:</b>
              <p>{{ reservation.voo.aeroporto_origem.nome }}</p>
            </li>
            <li class="flex gap-2">
              <b>Destino:</b>
              <p>{{ reservation.voo.aeroporto_destino.nome }}</p>
            </li>
            <li class="flex gap-2">
              <b>Valor:</b>
              <p>
                {{
                  reservation.valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
                }}
              </p>
            </li>
            <li class="flex gap-2">
              <b>Milhas gastas:</b>
              <p>{{ reservation.milhas_utilizadas }}</p>
            </li>
            <li class="flex gap-2">
              <b>Estado da reserva:</b>
              <p>{{ reservation.estado }}</p>
            </li>
            <li class="flex gap-2">
              <b>Quantidade de poltronas:</b>
              <p>{{ reservation.quantidade_poltronas }}</p>
            </li>
          </ul>
        </section>
        <hr />
        <section>
          <Table class="w-full table-auto border-separate border-spacing-2">
            <TableHeader>
              <TableRow>
                <TableHead class="text-center px-6 py-3 text-lg">Data/Hora</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Estado origem</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Estado destino</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow v-for="h in history" :key="h.id">
                <TableCell class="text-center px-6 py-4"> {{ formatDateTime(h.data) }} </TableCell>
                <TableCell class="text-center px-6 py-4"> {{ h.estado_origem }} </TableCell>
                <TableCell class="text-center px-6 py-4"> {{ h.estado_destino }} </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </section>
      </CardContent>
    </Card>

    <div v-else class="text-center p-8">
      <h2 class="text-2xl font-bold mb-4">Reserva não encontrada</h2>
      <p class="text-lg">O código da reserva "{{ route.params.code }}" não foi localizado.</p>
      <p class="text-muted-foreground mt-2">Verifique o código e tente novamente.</p>
    </div>
  </div>
</template>
