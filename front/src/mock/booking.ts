import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'

const booking: Ref<Reserve[]> = ref([
  {
    id: 1,
    status: 'CRIADA',
    dateTimeR: '2025-04-3 14:00',
    dateTimeF: '2025-04-5 14:00',
    origin: 'GRU',
    destination: 'JFK',
    code: 'HGP123',
    price: 250.0,
    miles: 12,
  },
  {
    id: 2,
    status: 'REALIZADA',
    dateTimeR: '2025-03-20 08:30',
    dateTimeF: '2025-04-5 14:00',
    origin: 'GIG',
    destination: 'MIA',
    code: 'HGP456',
    price: 200.0,
    miles: 2,
  },
  {
    id: 3,
    status: 'CANCELADA',
    dateTimeR: '2025-03-15 16:45',
    dateTimeF: '2025-04-5 14:00',
    origin: 'BSB',
    destination: 'LIS',
    code: 'MNB123',
    price: 50.0,
    miles: 16,
  },
])

export default booking

export async function searchReserves(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()

  return booking.value.filter((reserve) => reserve.code.toUpperCase() === searchCode)
}

export async function cancelReservation(reservationid: number) {
  if (reservationid) {
    const reservation = booking.value.find((r) => r.id === reservationid)
    if (reservation) {
      reservation.status = 'CANCELADA'
    } else {
      console.log(`Reserva com ID ${reservationid} não encontrada.`)
    }
  }
}

export function getReservationByCode(code: string): Reserve | undefined {
  const searchCode = code.trim().toUpperCase()
  return booking.value.find((reserve) => reserve.code.toUpperCase() === searchCode)
}

export function updateReservationStatus(code: string, newStatus: Reserve['status']): void {
  const reservation = getReservationByCode(code)
  if (reservation) {
    reservation.status = newStatus
  } else {
    console.warn(`Reserva com código ${code} não encontrada para atualizar status.`)
  }
}
