<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Pencil, Trash2, Plus } from 'lucide-vue-next'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from '@/components/ui/table'
import UpdateEmployeeDialog from './components/UpdateEmployeeDialog.vue'
import AddEmployeeDialog from './components/AddEmployeeDialog.vue'
import type { Employee } from '@/types/Auth/AuthenticatedUserData'
import { useAuthStore } from '@/stores/auth'
import getAllEmployees from './services/getAllEmployees'
import deleteEmployee from './services/deleteEmployee'
import updateEmployee from './services/updateEmployee'

const authStore = useAuthStore()
const employees = ref<Employee[]>([])
const isUpdateEmployeeDialog = ref(false)
const isAddEmployeeDialog = ref(false)
const selectedEmployee = ref<Employee | null>(null)

onMounted(() => {
  fetchEmployees()
})
async function fetchEmployees() {
  try {
    const response = await getAllEmployees()
    employees.value = response.data.sort((a, b) => a.nome.localeCompare(b.nome))
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error)
  }
}

function goToCreate() {
  isAddEmployeeDialog.value = true
}

function editEmployee(employee: Employee) {
  isUpdateEmployeeDialog.value = true
  selectedEmployee.value = employee
}

async function handleUpdateEmployee(data: {
  codigo: number
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}) {
  try {
    await updateEmployee(data)
    await fetchEmployees()
  } catch (error) {
    console.error('Erro ao atualizar funcionário:', error)
  }
}

async function handleDeleteEmployee(employee: Employee) {
  try {
    await deleteEmployee(employee.codigo)
    await fetchEmployees()
  } catch (error) {
    console.error('Erro ao excluir funcionário:', error)
  }
}
</script>

<template>
  <UpdateEmployeeDialog
    :employee="selectedEmployee"
    v-model:open="isUpdateEmployeeDialog"
    @updated="handleUpdateEmployee"
  />

  <AddEmployeeDialog v-model:open="isAddEmployeeDialog" @added="fetchEmployees" />
  <div class="flex flex-col justify-center h-screen">
    <nav class="p-1 shadow-md mt-6 flex justify-end" >
      <Button class="w-52 h-9" @click="goToCreate">
      <Plus class="mr-2 h-4 w-4" />
      Novo Funcionário
    </Button>
    </nav>
    <br>
    <div class="max-h-[500px] overflow-y-aut0">
      <Card>
        <CardHeader>
          <CardTitle class="text-2xl">Empregados</CardTitle>
        </CardHeader>
        <CardContent>
          <Table class="w-full table-auto border-separate border-spacing-2">
            <TableHeader>
              <TableRow>
                <TableHead class="text-center px-6 py-3 text-lg">Nome</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">CPF</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">E-mail</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Telefone</TableHead>
                <TableHead class="text-center px-6 py-3 text-lg">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow v-for="employee in employees" :key="employee.email">
                <TableCell class="text-center px-6 py-4 text-lg">{{ 
                  employee.nome 
                  }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ 
                employee.cpf 
                }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ 
                employee.email 
                }}</TableCell>
                <TableCell class="text-center px-6 py-4 text-lg">{{ 
                employee.telefone 
                }}</TableCell>
                <TableCell class="flex justify-center gap-2">
                  <Button size="icon" variant="secondary" @click="editEmployee(employee)">
                    <Pencil class="h-4 w-4" />
                  </Button>
                  <Button
                    v-if="employee.codigo !== authStore.user?.usuario?.codigo"
                    size="icon"
                    variant="destructive"
                    @click="handleDeleteEmployee(employee)"
                  >
                    <Trash2 class="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  </div>
</template>
