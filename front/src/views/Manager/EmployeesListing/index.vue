<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Pencil, Trash2, Plus } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import {
  Table, TableHeader, TableRow, TableHead, TableBody, TableCell,
} from '@/components/ui/table'
import UpdateEmployeeDialog from './components/UpdateEmployeeDialog.vue'
import AddEmployeeDialog from './components/AddEmployeeDialog.vue'
import type { Employee } from '@/types/Auth/AuthenticatedUserData'
import { useAuthStore } from '@/stores/auth'
import getAllEmployees from './services/getAllEmployees'
import addNewEmployee, { type NewEmployeeData } from './services/addNewEmployee'
import updateEmployee from './services/updateEmployee'
import deleteEmployee from './services/deleteEmployee'

const authStore = useAuthStore()

const employees = ref<Employee[]>([])
const isLoading = ref(false)
const isAddDialogOpen = ref(false)
const isUpdateDialogOpen = ref(false)
const selectedEmployee = ref<Employee | null>(null)

onMounted(fetchEmployees)

async function fetchEmployees() {
  isLoading.value = true
  try {
    const response = await getAllEmployees()
    employees.value = response.data.sort((a, b) => a.nome.localeCompare(b.nome))
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error)
  } finally {
    isLoading.value = false
  }
}

function goToCreate() {
  isAddDialogOpen.value = true
}

function editEmployee(employee: Employee) {
  selectedEmployee.value = employee
  isUpdateDialogOpen.value = true
}

async function handleAddEmployee(data: NewEmployeeData) {
  try {
    await addNewEmployee(data)
    await fetchEmployees()
  } catch (error) {
    console.error('Erro ao adicionar funcionário:', error)
  }
}

async function handleUpdateEmployee(data: Employee) {
  try {
    await updateEmployee(data)
    await fetchEmployees()
  } catch (error) {
    console.error('Erro ao atualizar funcionário:', error)
  }
}

async function handleDeleteEmployee(employee: Employee) {
  const confirmDelete = confirm(`Deseja realmente excluir ${employee.nome}?`)
  if (!confirmDelete) return

  try {
    await deleteEmployee(employee.codigo)
    await fetchEmployees()
  } catch (error) {
    console.error('Erro ao excluir funcionário:', error)
  }
}
</script>

<template>
  <AddEmployeeDialog
    v-model:open="isAddDialogOpen"
    @added="handleAddEmployee"
  />

  <UpdateEmployeeDialog
    v-model:open="isUpdateDialogOpen"
    :employee="selectedEmployee"
    @updated="handleUpdateEmployee"
  />

  <div class="min-h-screen flex flex-col items-end gap-6 px-4 py-6">
    <Button class="w-52 h-9" @click="goToCreate" :disabled="isLoading">
      <Plus class="mr-2 h-4 w-4" />
      Novo Funcionário
    </Button>

    <Table v-if="!isLoading">
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
        <TableRow v-for="employee in employees" :key="employee.codigo">
          <TableCell>{{ employee.nome }}</TableCell>
          <TableCell>{{ employee.cpf }}</TableCell>
          <TableCell>{{ employee.email }}</TableCell>
          <TableCell>{{ employee.telefone }}</TableCell>
          <TableCell class="flex justify-center gap-2">
            <Button
              size="icon"
              variant="secondary"
              @click="editEmployee(employee)"
              :aria-label="`Editar ${employee.nome}`"
            >
              <Pencil class="h-4 w-4" />
            </Button>
            <Button
              v-if="employee.codigo !== authStore.user?.usuario.codigo"
              size="icon"
              variant="destructive"
              @click="handleDeleteEmployee(employee)"
              :aria-label="`Excluir ${employee.nome}`"
            >
              <Trash2 class="h-4 w-4" />
            </Button>
          </TableCell>
        </TableRow>
      </TableBody>
    </Table>

    <div v-else class="text-sm text-muted-foreground self-center">Carregando funcionários...</div>
  </div>
</template>
