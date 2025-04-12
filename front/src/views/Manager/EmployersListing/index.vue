<script setup lang="ts">
import { computed } from 'vue'
import { Pencil, Trash2, Plus } from 'lucide-vue-next'

import { Button } from '@/components/ui/button'
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from '@/components/ui/table'

import employeeService from '@/mock/employees'

const employees = computed(() =>
  employeeService.registeredEmployees.value.slice().sort((a, b) => a.name.localeCompare(b.name)),
)

function goToCreate() {}

function editEmployee() {}

function deleteEmployee() {}
</script>

<template>
  <div class="min-h-screen flex flex-col justify-center gap-10 items-end">
    <Button class="w-52 h-9" @click="goToCreate">
      <Plus class="mr-2 h-4 w-4" />
      Novo Funcionário
    </Button>

    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Nome</TableHead>
          <TableHead>CPF</TableHead>
          <TableHead>E-mail</TableHead>
          <TableHead>Telefone</TableHead>
          <TableHead class="text-center">Ações</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow v-for="employee in employees" :key="employee.email">
          <TableCell>{{ employee.name }}</TableCell>
          <TableCell>{{ employee.cpf }}</TableCell>
          <TableCell>{{ employee.email }}</TableCell>
          <TableCell>{{ employee.phone }}</TableCell>
          <TableCell class="flex justify-center gap-2">
            <Button size="icon" variant="secondary" @click="editEmployee()">
              <Pencil class="h-4 w-4" />
            </Button>
            <Button size="icon" variant="destructive" @click="deleteEmployee()">
              <Trash2 class="h-4 w-4" />
            </Button>
          </TableCell>
        </TableRow>
      </TableBody>
    </Table>
  </div>
</template>
