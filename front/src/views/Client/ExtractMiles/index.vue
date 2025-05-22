<script setup lang="ts">
import { ref, computed, watch, onMounted, h } from 'vue'
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  VisibilityState,
} from '@tanstack/vue-table'
import {
  useVueTable,
  getCoreRowModel,
  getSortedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getExpandedRowModel,
  flexRender as FlexRender,
} from '@tanstack/vue-table'
import { Button } from '@/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { ArrowUpDown } from 'lucide-vue-next'
import { useAuthStore } from '@/stores/auth'
import { getClientTransactionHistory } from '@/clientService/ClientService'
import type { MilesExtractItem } from '@/types/Api'

// extende para incluir Date nativo
interface MilesItem extends MilesExtractItem {
  dateObj: Date
}

const authStore = useAuthStore()
const userCode = computed(() => authStore.user?.usuario?.codigo)

const data = ref<MilesItem[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

async function loadData() {
  if (!userCode.value) {
    error.value = 'Usuário não autenticado'
    return
  }
  loading.value = true
  error.value = null
  try {
    const response = await getClientTransactionHistory()
    data.value = response.transacoes.map((item) => ({
      ...item,
      dateObj: new Date(item.data),
    }))
  } catch (e) {
    console.error('Erro ao buscar transações:', e)
    error.value = 'Falha ao carregar histórico'
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
watch(userCode, (newVal) => {
  if (newVal) loadData()
})

const columns = computed<ColumnDef<MilesItem>[]>(() => [
  {
    accessorKey: 'dateObj',
    header: ({ column }) =>
      h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(null),
        },
        () => ['Data', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      ),
    cell: (info) =>
      (info.getValue() as Date).toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      }),
  },
  {
    accessorKey: 'codigo_reserva',
    header: 'Código de reserva',
  },
  {
    accessorKey: 'valor_reais',
    header: ({ column }) =>
      h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(null),
        },
        () => ['Valor', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      ),
    cell: (info) =>
      (info.getValue() as number).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      }),
  },
  {
    accessorKey: 'quantidade_milhas',
    header: ({ column }) =>
      h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(null),
        },
        () => ['Milhas', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      ),
  },
  {
    accessorKey: 'descricao',
    header: 'Descrição',
    cell: (info) => (info.getValue() as string).toUpperCase(),
  },
  {
    accessorKey: 'tipo',
    header: 'Tipo',
  },
])

const columnFilters = ref<ColumnFiltersState>([])
const columnVisibility = ref<VisibilityState>({})
const rowSelection = ref({})
const expanded = ref<ExpandedState>({})

const table = useVueTable({
  data: data.value,
  columns: columns.value,
  state: {
    columnFilters: columnFilters.value,
    columnVisibility: columnVisibility.value,
    rowSelection: rowSelection.value,
    expanded: expanded.value,
  },
  onColumnFiltersChange: (val) => (columnFilters.value = val),
  onColumnVisibilityChange: (val) => (columnVisibility.value = val),
  onRowSelectionChange: (val) => (rowSelection.value = val),
  onExpandedChange: (val) => (expanded.value = val),
  getCoreRowModel: getCoreRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
  getExpandedRowModel: getExpandedRowModel(),
  initialState: {
    sorting: [{ id: 'dateObj', desc: true }],
  },
})
</script>

<template>
  <div class="flex h-screen w-full items-center justify-center p-4 bg-gray-50">
    <div class="w-full max-w-screen-lg rounded-lg border shadow-sm bg-white overflow-hidden">
      <div class="p-6">
        <div v-if="loading" class="text-center py-12">Carregando histórico...</div>
        <div v-else-if="error" class="text-center text-red-600 py-12">{{ error }}</div>
        <div v-else>
          <Table>
            <TableHeader>
              <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
                <TableHead v-for="header in headerGroup.headers" :key="header.id">
                  <FlexRender
                    v-if="!header.isPlaceholder"
                    :render="header.column.columnDef.header"
                    :props="header.getContext()"
                  />
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <template v-if="table.getRowModel().rows.length">
                <TableRow v-for="row in table.getRowModel().rows" :key="row.id">
                  <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
                    <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                  </TableCell>
                </TableRow>
              </template>
              <TableRow v-else>
                <TableCell :colspan="columns.length" class="h-24 text-center">
                  Sem resultados.
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>

          <div class="flex items-center justify-between py-4">
            <div class="space-x-2">
              <Button
                variant="outline"
                size="sm"
                @click="() => table.setPageIndex(0)"
                :disabled="!table.getCanPreviousPage()"
              >
                Primeira
              </Button>
              <Button
                variant="outline"
                size="sm"
                @click="table.previousPage"
                :disabled="!table.getCanPreviousPage()"
              >
                Anterior
              </Button>
            </div>

            <div>
              Página
              <strong>{{ table.getState().pagination.pageIndex + 1 }}</strong>
              de <strong>{{ table.getPageCount() }}</strong>
            </div>

            <div class="space-x-2">
              <Button
                variant="outline"
                size="sm"
                @click="table.nextPage"
                :disabled="!table.getCanNextPage()"
              >
                Próximo
              </Button>
              <Button
                variant="outline"
                size="sm"
                @click="() => table.setPageIndex(table.getPageCount() - 1)"
                :disabled="!table.getCanNextPage()"
              >
                Última
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
