import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'

const booking: Ref<Reserve[]> = ref([
  {
    id: 1,
    status: 'CRIADA',
    dateTimeR: '03/04/25 14:00',
    dateTimeF: '06/04/25 14:00',
    origin: 'GRU',
    destination: 'JFK',
    code: 'HGP123',
    price: 250.0,
    miles: 12,
  },
  {
    id: 2,
    status: 'REALIZADA',
    dateTimeR: '20/03/25 08:30',
    dateTimeF: '05/04/25 14:00',
    origin: 'GIG',
    destination: 'MIA',
    code: 'HGP456',
    price: 200.0,
    miles: 2,
  },
  {
    id: 3,
    status: 'CANCELADA',
    dateTimeR: '15/03/25 16:45',
    dateTimeF: '05/04/25 14:00',
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