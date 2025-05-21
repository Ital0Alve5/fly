<script setup lang="ts">
import { computed, watchEffect } from 'vue'
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  VisibilityState,
} from '@tanstack/vue-table'
import { Button } from '../../../components/ui/button'
import { valueUpdater } from '../../../lib/utils'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '../../../components/ui/table'
import {
  FlexRender,
  getCoreRowModel,
  getExpandedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useVueTable,
} from '@tanstack/vue-table'
import { ArrowUpDown } from 'lucide-vue-next'
import { h, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getClientTransactionHistory } from '@/clientService/ClientService'
import type { MilesExtractResponse, MilesExtractItem } from '@/types/Api'

const authtore = useAuthStore()

const userCode = computed(() => authtore.user?.usuario?.codigo)
const data = ref<MilesExtractResponse | null>(null)

watchEffect(async () => {
  if (userCode.value) {
    try {
      const response = await getClientTransactionHistory()

      const transformed: MilesExtractResponse = {
        codigo: response?.codigo,
        saldo_milhas: response?.saldo_milhas,
        transacoes: response?.transacoes?.map((item) => ({
          data: new Date(item.data).toLocaleString('pt-BR'),
          codigo_reserva: item.codigo_reserva ?? '-',
          valor_reais: Number(item.valor_reais),
          quantidade_milhas: item.quantidade_milhas,
          descricao: item.descricao,
          tipo: item.tipo,
        })),
      }

      data.value = transformed
    } catch (error) {
      console.error('Erro ao buscar histórico de transações:', error)
      data.value = null
    }
  } else {
    data.value = null
  }
})

const columns: ColumnDef<MilesExtractItem>[] = [
  {
    accessorKey: 'data',
    header: ({ column }) => {
      return h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(column.getIsSorted() === 'asc'),
        },
        () => ['Data', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      )
    },
    cell: ({ row }) => h('div', row.getValue('data')),
    sortingFn: (rowA, rowB, columnId) => {
      const dateTimeA = rowA.getValue(columnId) as string
      const dateTimeB = rowB.getValue(columnId) as string
      const [dateA, timeA] = dateTimeA.split(', ')
      const [dateB, timeB] = dateTimeB.split(', ')

      const [dayA, monthA, yearA] = dateA.split('/').map(Number)
      const [dayB, monthB, yearB] = dateB.split('/').map(Number)

      let hoursA = 0,
        minutesA = 0,
        secondsA = 0
      let hoursB = 0,
        minutesB = 0,
        secondsB = 0

      if (timeA) {
        ;[hoursA, minutesA, secondsA] = timeA.split(':').map(Number)
      }

      if (timeB) {
        ;[hoursB, minutesB, secondsB] = timeB.split(':').map(Number)
      }

      const parsedDateA = new Date(yearA, monthA - 1, dayA, hoursA, minutesA, secondsA)
      const parsedDateB = new Date(yearB, monthB - 1, dayB, hoursB, minutesB, secondsB)

      return parsedDateA.getTime() - parsedDateB.getTime()
    },
  },
  {
    accessorKey: 'codigo_reserva',
    header: 'Código de reserva',
    cell: ({ row }) => h('div', row.getValue('codigo_reserva')),
  },
  {
    accessorKey: 'valor_reais',
    header: ({ column }) => {
      return h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(column.getIsSorted() === 'asc'),
        },
        () => ['Valor', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      )
    },
    cell: ({ row }) => {
      const value = row.getValue('valor_reais') as number
      return h('div', value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }))
    },
  },
  {
    accessorKey: 'quantidade_milhas',
    header: ({ column }) => {
      return h(
        Button,
        {
          variant: 'ghost',
          onClick: () => column.toggleSorting(column.getIsSorted() === 'asc'),
        },
        () => ['Milhas', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })],
      )
    },
    cell: ({ row }) => h('div', row.getValue('quantidade_milhas')),
  },
  {
    accessorKey: 'descricao',
    header: 'Descrição',
    cell: ({ row }) => h('div', (row.getValue('descricao') as string).toUpperCase()),
  },
  {
    accessorKey: 'tipo',
    header: 'Tipo',
    cell: ({ row }) => h('div', row.getValue('tipo')),
  },
]

const columnFilters = ref<ColumnFiltersState>([])
const columnVisibility = ref<VisibilityState>({})
const rowSelection = ref({})
const expanded = ref<ExpandedState>({})

const table = computed(() =>
  useVueTable({
    data: data.value?.transacoes ?? [],
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getExpandedRowModel: getExpandedRowModel(),
    onColumnFiltersChange: (updaterOrValue) => valueUpdater(updaterOrValue, columnFilters),
    onColumnVisibilityChange: (updaterOrValue) => valueUpdater(updaterOrValue, columnVisibility),
    onRowSelectionChange: (updaterOrValue) => valueUpdater(updaterOrValue, rowSelection),
    onExpandedChange: (updaterOrValue) => valueUpdater(updaterOrValue, expanded),
    initialState: {
      sorting: [
        {
          id: 'data',
          desc: true,
        },
      ],
    },
    state: {
      get columnFilters() {
        return columnFilters.value
      },
      get columnVisibility() {
        return columnVisibility.value
      },
      get rowSelection() {
        return rowSelection.value
      },
      get expanded() {
        return expanded.value
      },
    },
  }),
)
</script>

<template>
  <div class="flex h-screen w-full items-center justify-center p-4">
    <div class="w-full max-w-full rounded-lg border shadow-sm overflow-hidden">
      <div class="w-full p-4">
        <div class="rounded-md border">
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
              <template v-if="table.getRowModel().rows?.length">
                <TableRow v-for="row in table.getRowModel().rows" :key="row.id">
                  <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
                    <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                  </TableCell>
                </TableRow>
              </template>

              <TableRow v-else>
                <TableCell :colspan="columns.length" class="h-24 text-center">
                  No results.
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>

        <div class="flex items-center justify-end space-x-2 py-4">
          <div class="space-x-2">
            <Button
              variant="outline"
              size="sm"
              :disabled="!table.getCanPreviousPage()"
              @click="table.previousPage()"
            >
              Anterior
            </Button>
            <Button
              variant="outline"
              size="sm"
              :disabled="!table.getCanNextPage()"
              @click="table.nextPage()"
            >
              Próximo
            </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
