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
    date: '05/03/2025',
    reservationCode: 'FL-316',
    value: 'R$245,50',
    miles: 4200,
    description: 'GRU->SDU',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '12/03/2025',
    reservationCode: '',
    value: 'R$189,90',
    miles: 0,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '15/03/2025',
    reservationCode: '',
    value: 'R$1.250,00',
    miles: 25000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '18/03/2025',
    reservationCode: '',
    value: 'R$75,30',
    miles: 1506,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '20/03/2025',
    reservationCode: 'FL-822',
    value: 'R$320,00',
    miles: 6400,
    description: 'GRU->EZE',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '22/03/2025',
    reservationCode: '',
    value: 'R$0,00',
    miles: 1000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '25/03/2025',
    reservationCode: '',
    value: 'R$412,75',
    miles: 0,
    description: 'COMPRA DE MILHAS',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '28/03/2025',
    reservationCode: '',
    value: 'R$600,00',
    miles: 12000,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  },
  {
    userId: 1,
    date: '30/03/2025',
    reservationCode: 'FL-156',
    value: 'R$175,20',
    miles: 3504,
    description: 'BSB->CGH',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '02/04/2025',
    reservationCode: 'PG-001',
    value: 'R$2.000,00',
    miles: 40000,
    description: 'CWB->MAO',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '05/04/2025',
    reservationCode: 'FL-413',
    value: 'R$0,00',
    miles: 8000,
    description: 'REC->FOR',
    type: 'SAÍDA',
  },
  {
    userId: 1,
    date: '08/04/2025',
    reservationCode: '',
    value: 'R$750,00',
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