import { ref } from 'vue'

export type ExtractItem = {
  userId: number
  date: string
  reservationCode: string | null
  value: string
  miles: number
  description: string
  type: string
}

const ExtractHistory = ref<ExtractItem[]>([
  {
    userId: 1,
    date: '01/03/2025 09:15:30',
    reservationCode: 'FL-316',
    value: (245.50).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 4200,
    description: 'GRU->SDU',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '12/03/2025 14:20:45',
    reservationCode: null,
    value: (189.90).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 0,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '15/03/2025 11:05:22',
    reservationCode: null,
    value: (1250).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 25000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '18/03/2025 16:30:18',
    reservationCode: null,
    value: (75.3).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 1506,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '20/03/2025 08:45:00',
    reservationCode: 'FL-822',
    value: (320).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 6400,
    description: 'GRU->EZE',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '22/03/2025 13:10:05',
    reservationCode: null,
    value: (5000).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 1000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '25/03/2025 10:25:40',
    reservationCode: null,
    value: (412.75).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 0,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '28/03/2025 15:55:12',
    reservationCode: null,
    value: (600).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 12000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '30/03/2025 07:30:00',
    reservationCode: 'FL-156',
    value: (175.20).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 3504,
    description: 'BSB->CGH',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '01/04/2025 12:00:00',
    reservationCode: 'PG-001',
    value: (2000).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 40000,
    description: 'CWB->MAO',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '23/03/2025 18:20:30',
    reservationCode: 'FL-413',
    value: (1.9).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 8000,
    description: 'REC->FOR',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '29/03/2025 09:45:15',
    reservationCode: null,
    value: (750.6).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: 15000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
])

export function getExtractByUserId(userId: number): ExtractItem[] {
  return ExtractHistory.value.filter((extract) => extract.userId === userId)
}

export function registerExtract(extract: ExtractItem): void {
  ExtractHistory.value.push(extract)
}

export default { ExtractHistory, registerExtract, getExtractByUserId }
