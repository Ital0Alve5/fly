import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'

export const booking: Ref<Reserve[]> = ref([
  {
    id: 1,
    status: 'CRIADA',
    dateTimeR: '03/04/25 14:00',
    dateTimeF: '08/04/25 14:00',
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
    code: 'STU901',
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
    code: 'MNO345',
    price: 50.0,
    miles: 16,
  },
])

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

export function getReservationById(id: number): Reserve | undefined {
  return booking.value.find((reserve) => reserve.id === id)
}

export async function cancelReservationByFlightCode(flightCode: string) {
  booking.value.forEach((reservation) => {
    if (reservation.code === flightCode) {
      reservation.status = 'CANCELADO VOO'
    }
  })
}

function getFlightsInNext48hrs(): Reserve[] {
  const now = new Date()
  const nowTime = now.getTime()
  const limitTime = nowTime + 48 * 60 * 60 * 1000

  return booking.value.filter((reserve) => {
    const [datePart, timePart] = reserve.dateTimeF.split(' ')
    const [day, month, year] = datePart.split('/').map(Number)
    const [hour, minute] = timePart.split(':').map(Number)

    const flightDate = new Date(2000 + year, month - 1, day, hour, minute)
    const flightTime = flightDate.getTime()

    return flightTime > nowTime && flightTime <= limitTime
  })
}

export function getCheckInFlightsInNext48Hrs(): Reserve[] {
  return getFlightsInNext48hrs().filter((reserve) =>
    ['CRIADA', 'CHECK-IN'].includes(reserve.status.toUpperCase()),
  )
}

export async function updateReservationStatus(
  reservationId: number,
  newStatus: Reserve['status'],
): Promise<void> {
  const reservation = booking.value.find((r) => r.id === reservationId)
  if (reservation) {
    reservation.status = newStatus
  } else {
    console.error(`Reserva com ID ${reservationId} não encontrada`)
    throw new Error(`Reserva com ID ${reservationId} não encontrada`)
  }
}
