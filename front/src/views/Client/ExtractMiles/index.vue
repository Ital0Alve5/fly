<script setup lang="ts">
import { h, computed, onMounted, ref } from 'vue'
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  VisibilityState,
} from '@tanstack/vue-table'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
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
import { Button } from '@/components/ui/button'
import { useAuthStore } from '@/stores/auth'
import { MilesService } from '@/services/milesService'
import { useToast } from '@/components/ui/toast'
import type { MilesExtractItem } from '@/types/Api'
import { valueUpdater } from '@/lib/utils'

const { toast } = useToast()
const authStore = useAuthStore()
const data = ref<MilesExtractItem[]>([])
const isLoading = ref(false)

const fetchExtractData = async () => {
  if (!authStore.user?.usuario.codigo) return
  
  isLoading.value = true
  try {
    const response = await MilesService.getExtract(authStore.user.usuario.codigo)
    if (!response.error) {
      data.value = response.data.transacoes.map((item: MilesExtractItem) => ({
        ...item,
        valor_reais: new Intl.NumberFormat('pt-BR', {
          style: 'currency',
          currency: 'BRL'
        }).format(item.valor_reais),
        data: new Date(item.data).toLocaleString('pt-BR')
      }))
    } else {
      toast({
        title: 'Erro',
        description: response.message,
        variant: 'destructive',
        duration: 2000,
      })
    }
  } catch (error: unknown) {
    const errorMessage = error instanceof Error 
      ? error.message 
      : 'Falha ao carregar extrato de milhas'
    toast({
      title: 'Erro',
      description: errorMessage,
      variant: 'destructive',
      duration: 2000,
    })
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchExtractData)

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
        () => ['Data', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
      )
    },
    cell: ({ row }) => h('div', row.getValue('data')),
    sortingFn: (rowA, rowB) => {
      const dateA = new Date(rowA.original.data)
      const dateB = new Date(rowB.original.data)
      return dateA.getTime() - dateB.getTime()
    },
  },
  {
    accessorKey: 'codigo_reserva',
    header: 'Código Reserva',
    cell: ({ row }) => h('div', row.getValue('codigo_reserva') || 'N/A'),
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
        () => ['Valor (R$)', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
      )
    },
    cell: ({ row }) => h('div', row.getValue('valor_reais')),
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
        () => ['Milhas', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
      )
    },
    cell: ({ row }) => h('div', row.getValue('quantidade_milhas')),
  },
  {
    accessorKey: 'descricao',
    header: 'Descrição',
    cell: ({ row }) => h('div', row.getValue('descricao')),
  },
  {
    accessorKey: 'tipo',
    header: 'Tipo',
    cell: ({ row }) => {
      const tipo = row.getValue('tipo') as string
      return h('div', {
        class: tipo === 'ENTRADA' ? 'text-green-500' : 'text-red-500'
      }, tipo)
    },
  },
]

const columnFilters = ref<ColumnFiltersState>([])
const columnVisibility = ref<VisibilityState>({})
const rowSelection = ref({})
const expanded = ref<ExpandedState>({})

const table = computed(() =>
  useVueTable({
    data: data.value,
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
    state: {
      get columnFilters() { return columnFilters.value },
      get columnVisibility() { return columnVisibility.value },
      get rowSelection() { return rowSelection.value },
      get expanded() { return expanded.value },
    },
    initialState: {
      pagination: {
        pageSize: 8,
      },
      sorting: [
        {
          id: 'data',
          desc: true,
        },
      ],
    },
  })
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