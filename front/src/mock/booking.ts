import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'
import type { Flight } from './flight'
import { getTodayDate } from '@/utils/date/getTodayDate'
import { generateUniqueCode } from '@/utils/generateRandomCode'
import { useAuthStore } from '@/stores/auth'
import { useMilesStore } from '@/stores/miles'

export const booking: Ref<Reserve[]> = ref([
  {
    id: 2,
    userId: 1,
    status: 'REALIZADA',
    dateTimeR: '20/03/25 08:30',
    dateTimeF: '21/03/25 14:00',
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
    id: 9,
    userId: 1,
    status: 'CHECK-IN',
    dateTimeR: '10/04/25 12:00',
    dateTimeF: '13/04/25 15:00',
    origin: 'São Paulo',
    destination: 'Mexico City',
    reservationCode: 'NOW001',
    flightCode: 'NOA001',
    price: 1800,
    miles: 36000,
  },
])

export async function getBooking(): Promise<Reserve[]> {
  return new Promise((res) => {
    res(booking.value)
  })
}

export async function getBookingByUserId(): Promise<Reserve[]> {
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

export async function searchReservesByFlightCode(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const booking = await getBooking()

  return booking.filter((reserve) => reserve.flightCode.toUpperCase() === searchCode)
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
  const booking = await getBookingByUserId()

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
  const booking = await getBookingByUserId()

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
  console.log(
    flights.filter((reserve) => ['CRIADA', 'CHECK-IN'].includes(reserve.status.toUpperCase())),
  )
  return flights.filter((reserve) => ['CRIADA', 'CHECK-IN'].includes(reserve.status.toUpperCase()))
}

export async function updateReservationStatus(
  reservationCode: string,
  newStatus: Reserve['status'],
): Promise<void> {
  const booking = await getBooking()
  const reservation = booking.find((r) => r.reservationCode === reservationCode)

  if (reservation) {
    reservation.status = newStatus
  } else {
    console.error(`Reserva com código ${reservationCode} não encontrada`)
    throw new Error(`Reserva com código ${reservationCode} não encontrada`)
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
