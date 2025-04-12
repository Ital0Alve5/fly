import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'
import type { Flight } from './flight'
import { getTodayDate } from '@/utils/date/getTodayDate'
import { generateUniqueCode } from '@/utils/generateRandomCode'
import { useAuthStore } from '@/stores/auth'
import { useMilesStore } from '@/stores/miles'

export const booking: Ref<Reserve[]> = ref([
  {
    id: 1,
    userId: 1,
    status: 'CRIADA',
    dateTimeR: '03/04/25 14:00',
    dateTimeF: '08/04/25 14:00',
    origin: 'São Paulo',
    destination: 'New York',
    reservationCode: 'HGP123',
    flightCode: 'HGP123',
    price: 250.0,
    miles: 12,
  },
  {
    id: 2,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '20/03/25 08:30',
    dateTimeF: '20/04/25 14:00',
    origin: 'Rio de Janeiro',
    destination: 'Miami',
    reservationCode: 'STU901',
    flightCode: 'STU901',
    price: 200.0,
    miles: 2,
  },
  {
    id: 3,
    userId: 1,
    status: 'CANCELADA',
    dateTimeR: '15/03/25 16:45',
    dateTimeF: '22/04/25 14:00',
    origin: 'Brasília',
    destination: 'Lisboa',
    reservationCode: 'MNO345',
    flightCode: 'MNO345',
    price: 50.0,
    miles: 16,
  },
  {
    id: 4,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '30/04/25 07:00',
    dateTimeF: '30/04/25 09:15',
    origin: 'São Paulo',
    destination: 'Rio de Janeiro',
    reservationCode: 'FL-316',
    flightCode: 'FLW316',
    price: 245.5,
    miles: 4200,
  },
  {
    id: 5,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '02/05/25 08:00',
    dateTimeF: '02/05/25 08:45',
    origin: 'São Paulo',
    destination: 'Buenos Aires',
    reservationCode: 'FL-822',
    flightCode: 'FLK822',
    price: 320,
    miles: 6400,
  },
  {
    id: 6,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '04/05/25 06:00',
    dateTimeF: '04/05/25 07:30',
    origin: 'Brasília',
    destination: 'São Paulo',
    reservationCode: 'FL-156',
    flightCode: 'FLY156',
    price: 175.2,
    miles: 3504,
  },
  {
    id: 7,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '06/05/25 10:00',
    dateTimeF: '06/05/25 12:00',
    origin: 'Curitiba',
    destination: 'Manaus',
    reservationCode: 'PG-001',
    flightCode: 'PGP001',
    price: 2000,
    miles: 40000,
  },
  {
    id: 8,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '08/05/25 16:00',
    dateTimeF: '08/05/25 18:20',
    origin: 'Recife',
    destination: 'Fortaleza',
    reservationCode: 'FL-413',
    flightCode: 'FLT413',
    price: 1.9,
    miles: 8000,
  },
  {
    id: 9,
    userId: 1,
    status: 'CRIADA',
    dateTimeR: '10/04/25 12:00',
    dateTimeF: '10/04/25 15:00',
    origin: 'São Paulo',
    destination: 'Mexico City',
    reservationCode: 'NOW001',
    flightCode: 'NOA001',
    price: 1800,
    miles: 36000,
  },
  {
    id: 10,
    userId: 1,
    status: 'CRIADA',
    dateTimeR: '11/04/25 06:00',
    dateTimeF: '11/04/25 08:45',
    origin: 'Frankfurt',
    destination: 'London',
    reservationCode: 'NOW002',
    flightCode: 'NOB002',
    price: 900,
    miles: 18000,
  },
  {
    id: 11,
    userId: 1,
    status: 'CRIADA',
    dateTimeR: '11/04/25 20:30',
    dateTimeF: '11/04/25 23:00',
    origin: 'Miami',
    destination: 'São Paulo',
    reservationCode: 'NOW003',
    flightCode: 'NOC003',
    price: 1200,
    miles: 24000,
  },
  {
    id: 12,
    userId: 1,
    status: 'CRIADA',
    dateTimeR: '12/04/25 04:00',
    dateTimeF: '12/04/25 06:30',
    origin: 'Paris',
    destination: 'Frankfurt',
    reservationCode: 'NOW004',
    flightCode: 'NOD004',
    price: 700,
    miles: 14000,
  },
])

export async function getBooking(): Promise<Reserve[]> {
  const authStore = useAuthStore()

  return new Promise((res) => {
    res(booking.value.filter((reservation) => reservation.userId === authStore.user?.userId))
  })
}

export async function searchReserves(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const booking = await getBooking()

  return booking.filter((reserve) => reserve.reservationCode.toUpperCase() === searchCode)
}

export async function cancelReservation(reservationid: number) {
  const milesStore = useMilesStore()

  if (!reservationid) return

  const reservation = booking.value.find((r) => r.id === reservationid)

  if (reservation) {
    reservation.status = 'CANCELADA'
    milesStore.setTotalMiles(milesStore.totalMiles + reservation.miles)
  } else {
    console.log(`Reserva com ID ${reservationid} não encontrada.`)
  }
}

export async function getReservationByCode(code: string): Promise<Reserve | undefined> {
  const booking = await getBooking()

  return booking.find((reserve) => reserve.reservationCode === code)
}

export async function cancelReservationByFlightCode(flightCode: string) {
  const booking = await getBooking()

  booking.forEach((reservation) => {
    if (reservation.flightCode === flightCode) {
      reservation.status = 'CANCELADO VOO'
    }
  })
}

async function getFlightsInNext48hrs(): Promise<Reserve[]> {
  const now = new Date()
  const nowTime = now.getTime()
  const limitTime = nowTime + 48 * 60 * 60 * 1000
  const booking = await getBooking()

  return booking.filter((reserve) => {
    const [datePart, timePart] = reserve.dateTimeF.split(' ')
    const [day, month, year] = datePart.split('/').map(Number)
    const [hour, minute] = timePart.split(':').map(Number)

    const flightDate = new Date(2000 + year, month - 1, day, hour, minute)
    const flightTime = flightDate.getTime()

    return flightTime > nowTime && flightTime <= limitTime
  })
}

export async function getCheckInFlightsInNext48Hrs(): Promise<Reserve[]> {
  const flights = await getFlightsInNext48hrs()

  return flights.filter((reserve) => ['CRIADA', 'CHECK-IN'].includes(reserve.status.toUpperCase()))
}

export async function updateReservationStatus(
  reservationId: number,
  newStatus: Reserve['status'],
): Promise<void> {
  const booking = await getBooking()

  const reservation = booking.find((r) => r.id === reservationId)
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
  const authStore = useAuthStore()

  return new Promise((res) => {
    const code = generateUniqueCode()

    booking.value.push({
      id: booking.value[booking.value.length - 1].id + 1,
      userId: authStore.user!.userId,
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
