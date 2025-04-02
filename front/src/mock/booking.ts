import type { Reserve } from '@/types/Reserve'

const booking: Reserve[] = [
  { id: 1, status: 'CRIADA', dataHora: '2025-04-3 14:00', origem: 'GRU', destino: 'JFK', codigo: 'HGP123', valor: 'R$250,00', milhas: 12 },
  { id: 2, status: 'REALIZADA', dataHora: '2025-03-20 08:30', origem: 'GIG', destino: 'MIA', codigo: 'HGP456', valor: 'R$200,00', milhas: 2  },
  { id: 3, status: 'CANCELADA', dataHora: '2025-03-15 16:45', origem: 'BSB', destino: 'LIS', codigo: 'MNB123', valor: 'R$50,00', milhas: 16  },
]

export default booking

export async function searchReserves(codigo: string): Promise<Reserve[]> {
  const searchCode = codigo.trim().toUpperCase()

  return booking.filter(reserve => reserve.codigo.toUpperCase() === searchCode)
}