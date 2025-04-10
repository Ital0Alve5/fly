import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'
import type { Flight } from './flight'
import { getTodayDate } from '@/utils/date/getTodayDate'
import { generateUniqueCode } from '@/utils/generateRandomCode'

export const booking: Ref<Reserve[]> = ref([
  {
    id: 1,
    status: 'CRIADA',
    dateTimeR: '03/04/25 14:00',
    dateTimeF: '08/04/25 14:00',
    origin: 'GRU',
    destination: 'JFK',
    reservationCode: 'HGP123',
    flightCode: 'HGP123',
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
    reservationCode: 'STU901',
    flightCode: 'STU901',
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
    reservationCode: 'MNO345',
    flightCode: 'MNO345',
    price: 50.0,
    miles: 16,
  },
  {
    id: 4,
    status: 'REALIZADA',
    dateTimeR: '01/03/25 07:00',
    dateTimeF: '01/03/25 09:15',
    origin: 'GRU',
    destination: 'SDU',
    reservationCode: 'FL-316',
    flightCode: 'FLW316',
    price: 245.5,
    miles: 4200,
  },
  {
    id: 5,
    status: 'REALIZADA',
    dateTimeR: '20/03/25 08:00',
    dateTimeF: '20/03/25 08:45',
    origin: 'GRU',
    destination: 'EZE',
    reservationCode: 'FL-822',
    flightCode: 'FLK822',
    price: 320,
    miles: 6400,
  },
  {
    id: 6,
    status: 'REALIZADA',
    dateTimeR: '30/03/25 06:00',
    dateTimeF: '30/03/25 07:30',
    origin: 'BSB',
    destination: 'CGH',
    reservationCode: 'FL-156',
    flightCode: 'FLY156',
    price: 175.2,
    miles: 3504,
  },
  {
    id: 7,
    status: 'REALIZADA',
    dateTimeR: '01/04/25 10:00',
    dateTimeF: '01/04/25 12:00',
    origin: 'CWB',
    destination: 'MAO',
    reservationCode: 'PG-001',
    flightCode: 'PGP001',
    price: 2000,
    miles: 40000,
  },
  {
    id: 8,
    status: 'REALIZADA',
    dateTimeR: '23/03/25 16:00',
    dateTimeF: '23/03/25 18:20',
    origin: 'REC',
    destination: 'FOR',
    reservationCode: 'FL-413',
    flightCode: 'FLT413',
    price: 1.9,
    miles: 8000,
  },
  {
    id: 9,
    status: 'CRIADA',
    dateTimeR: '10/04/25 12:00',
    dateTimeF: '10/04/25 15:00',
    origin: 'GRU',
    destination: 'MEX',
    reservationCode: 'NOW001',
    flightCode: 'NOA001',
    price: 1800,
    miles: 36000,
  },
  {
    id: 10,
    status: 'CRIADA',
    dateTimeR: '11/04/25 06:00',
    dateTimeF: '11/04/25 08:45',
    origin: 'FRA',
    destination: 'LHR',
    reservationCode: 'NOW002',
    flightCode: 'NOB002',
    price: 900,
    miles: 18000,
  },
  {
    id: 11,
    status: 'CRIADA',
    dateTimeR: '11/04/25 20:30',
    dateTimeF: '11/04/25 23:00',
    origin: 'MIA',
    destination: 'GRU',
    reservationCode: 'NOW003',
    flightCode: 'NOC003',
    price: 1200,
    miles: 24000,
  },
  {
    id: 12,
    status: 'CRIADA',
    dateTimeR: '12/04/25 04:00',
    dateTimeF: '12/04/25 06:30',
    origin: 'CDG',
    destination: 'FRA',
    reservationCode: 'NOW004',
    flightCode: 'NOD004',
    price: 700,
    miles: 14000,
  },
])

export async function searchReserves(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()

  return booking.value.filter((reserve) => reserve.reservationCode.toUpperCase() === searchCode)
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
  return booking.value.find((reserve) => reserve.reservationCode === code)
}

export async function cancelReservationByFlightCode(flightCode: string) {
  booking.value.forEach((reservation) => {
    if (reservation.flightCode === flightCode) {
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

export async function createNewReservation(data: {
  flight: Flight
  price: number
  miles: number
}): Promise<string> {
  return new Promise((res) => {
    const code = generateUniqueCode()

    booking.value.push({
      id: booking.value[booking.value.length - 1].id + 1,
      status: 'CRIADA',
      dateTimeR: getTodayDate(),
      dateTimeF: data.flight.dateTime ?? '',
      origin: data.flight.originAirport ?? '',
      destination: data.flight.destinationAirport ?? '',
      flightCode: data.flight.code ?? '',
      reservationCode: code,
      price: data.price,
      miles: data.miles,
    })

    res(code)
  })
}
