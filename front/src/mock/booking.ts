import type { Reserve } from '@/types/Reserve'

const booking: Reserve[] = [
  { id: 1, status: 'CRIADA', dataHora: '2025-03-25 14:00', origem: 'GRU', destino: 'JFK' },
  { id: 2, status: 'REALIZADA', dataHora: '2025-03-20 08:30', origem: 'GIG', destino: 'MIA' },
  { id: 3, status: 'CANCELADA', dataHora: '2025-03-15 16:45', origem: 'BSB', destino: 'LIS' },
  { id: 4, status: 'CHECK-IN', dataHora: '2025-03-15 16:45', origem: 'BSB', destino: 'LIS' },
  { id: 5, status: 'CRIADA', dataHora: '2025-03-15 16:45', origem: 'BSB', destino: 'LIS' },
]

export default booking
