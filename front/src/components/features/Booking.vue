<script setup lang="ts">
import { ref } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card'
import { Button } from '../ui/button'
import { Table, TableHead, TableRow, TableHeader, TableBody, TableCell } from '../ui/table'

// Mock de dados da tabelinha
const saldoMilhas = ref(5000)
const reservas = ref([
  { id: 1, status: 'Reservado', dataHora: '2025-03-25 14:00', origem: 'GRU', destino: 'JFK' },
  { id: 2, status: 'Concluído', dataHora: '2025-03-20 08:30', origem: 'GIG', destino: 'MIA' },
  { id: 3, status: 'Cancelado', dataHora: '2025-03-15 16:45', origem: 'BSB', destino: 'LIS' },
])

// Métodos pros botões
const comprarMilhas = () => {}
const consultarExtrato = () => {}
const efetuarReserva = () => {}
const consultarReserva = () => {}
const fazerCheckin = () => {}

const verReserva = (id: number) => {
  console.log('Ver reserva', id)
}

const cancelarReserva = (id: number) => {
  console.log('Cancelar reserva', id)
}
</script>

<template>
  <div class="min-h-screen">
    <nav class="p-4 shadow-md mt-8">
      <div class="container mx-auto flex justify-between items-center">
        <span class="text-white text-lg"
          >Saldo de Milhas: <strong>{{ saldoMilhas }}</strong></span
        >
      </div>
      <div class="container mx-auto mt-4 flex justify-around">
        <Button @click="comprarMilhas">Comprar Milhas</Button>
        <Button @click="consultarExtrato">Consultar Extrato de Milhas</Button>
        <Button @click="efetuarReserva">Efetuar Reserva</Button>
        <Button @click="consultarReserva">Consultar Reserva</Button>
        <Button @click="fazerCheckin">Fazer Check-in</Button>
      </div>
    </nav>

    <div class="container mx-auto p-8">
      <Card>
        <CardHeader>
          <CardTitle class="text-2xl">Minhas Reservas e Voos</CardTitle>
        </CardHeader>
        <CardContent>
          <Table class="w-full table-auto border-separate border-spacing-2">
            <TableHeader>
              <TableRow>
                <TableHead class="text-center px-6 py-3 text-lg">Data/Hora</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Origem</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Aeroporto Destino</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Status</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow v-for="reserva in reservas" :key="reserva.id">
                <TableCell class="text-center px-6 py-4 text-lg">{{ reserva.dataHora }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ reserva.origem }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ reserva.destino }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">
                  <span
                    :class="{
                      'text-green-500': reserva.status === 'Concluído',
                      'text-blue-500': reserva.status === 'Reservado',
                      'text-red-500': reserva.status === 'Cancelado',
                    }"
                  >
                    {{ reserva.status }}
                  </span>
                </TableCell>
                <TableCell class="text-center">
                  <Button
                    v-if="reserva.status === 'Reservado'"
                    class="mr-2"
                    @click="verReserva(reserva.id)"
                    >Ver Reserva</Button
                  >
                  <Button
                    v-if="reserva.status === 'Reservado'"
                    variant="destructive"
                    @click="cancelarReserva(reserva.id)"
                    >Cancelar</Button
                  >
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  </div>
</template>
