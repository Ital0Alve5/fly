import { ref } from 'vue'

export type ExtractItem = {
  codigo_cliente: number
  data: string
  codigo_reserva: string | null
  valor_reais: string
  quantidade_milhas: number
  descricao: string
  tipo: 'SAÍDA' | 'ENTRADA'
}

const ExtractHistory = ref<ExtractItem[]>([
  {
    codigo_cliente: 1,
    data: '01/03/2025 09:15:30',
    codigo_reserva: 'FL-316',
    valor_reais: (245.5).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 4200,
    descricao: 'GRU->SDU',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '12/03/2025 14:20:45',
    codigo_reserva: null,
    valor_reais: (189.9).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 0,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '15/03/2025 11:05:22',
    codigo_reserva: null,
    valor_reais: (1250).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 25000,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'ENTRADA',
  },
  {
    codigo_cliente: 1,
    data: '18/03/2025 16:30:18',
    codigo_reserva: null,
    valor_reais: (75.3).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 1506,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '20/03/2025 08:45:00',
    codigo_reserva: 'FL-822',
    valor_reais: (320).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 6400,
    descricao: 'GRU->EZE',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '22/03/2025 13:10:05',
    codigo_reserva: null,
    valor_reais: (5000).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 1000,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'ENTRADA',
  },
  {
    codigo_cliente: 1,
    data: '25/03/2025 10:25:40',
    codigo_reserva: null,
    valor_reais: (412.75).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 0,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '28/03/2025 15:55:12',
    codigo_reserva: null,
    valor_reais: (600).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 12000,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'ENTRADA',
  },
  {
    codigo_cliente: 1,
    data: '30/03/2025 07:30:00',
    codigo_reserva: 'FL-156',
    valor_reais: (175.2).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 3504,
    descricao: 'BSB->CGH',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '01/04/2025 12:00:00',
    codigo_reserva: 'PG-001',
    valor_reais: (2000).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 40000,
    descricao: 'CWB->MAO',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '23/03/2025 18:20:30',
    codigo_reserva: 'FL-413',
    valor_reais: (1.9).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 8000,
    descricao: 'REC->FOR',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '29/03/2025 09:45:15',
    codigo_reserva: null,
    valor_reais: (750.6).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    quantidade_milhas: 15000,
    descricao: 'COMPRA DE MILHAS',
    tipo: 'ENTRADA',
  },
  {
    codigo_cliente: 1,
    data: '10/04/2025 15:00:00',
    codigo_reserva: 'NOW001',
    valor_reais: 'R$ 1.800,00',
    quantidade_milhas: 36000,
    descricao: 'GRU->MEX',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '11/04/2025 08:45:00',
    codigo_reserva: 'NOW002',
    valor_reais: 'R$ 900,00',
    quantidade_milhas: 18000,
    descricao: 'FRA->LHR',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '11/04/2025 23:00:00',
    codigo_reserva: 'NOW003',
    valor_reais: 'R$ 1.200,00',
    quantidade_milhas: 24000,
    descricao: 'MIA->GRU',
    tipo: 'SAÍDA',
  },
  {
    codigo_cliente: 1,
    data: '12/04/2025 06:30:00',
    codigo_reserva: 'NOW004',
    valor_reais: 'R$ 700,00',
    quantidade_milhas: 14000,
    descricao: 'CDG->FRA',
    tipo: 'SAÍDA',
  },
])

export function getExtractByUserCode(codigo_cliente: number): ExtractItem[] {
  return ExtractHistory.value.filter((extract) => extract.codigo_cliente === codigo_cliente)
}

export function registerExtract(extract: ExtractItem): void {
  ExtractHistory.value.push(extract)
}

export default { ExtractHistory, registerExtract, getExtractByUserCode }
