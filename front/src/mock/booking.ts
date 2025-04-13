import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'
import { getTodayDate } from '@/utils/date/getTodayDate'
import { generateUniqueCode } from '@/utils/generateRandomCode'
import { useAuthStore } from '@/stores/auth'
import { useMilesStore } from '@/stores/miles'
import { registerExtract, type ExtractItem } from './extract'
import type { Flight } from '@/types/Flight'

export const booking: Ref<Reserve[]> = ref([
  {
    codigo: 'AWN123',
    codigo_cliente: 1,
    estado: 'CHECK-IN',
    data: '10/04/25 08:30',
    valor: 200,
    milhas_utilizadas: 17,
    quantidade_poltronas: 2,
    voo: {
      codigo: 'STU901',
      data: '15/04/25 14:00',
      valor_passagem: 200,
      quantidade_poltronas_total: 180,
      quantidade_poltronas_ocupadas: 170,
      estado: 'REALIZADO',
      aeroporto_origem: {
        codigo: 'GIG',
        nome: 'Aeroporto do Galeão',
        cidade: 'Rio de Janeiro',
        uf: 'RJ',
      },
      aeroporto_destino: {
        codigo: 'MIA',
        nome: 'Miami International Airport',
        cidade: 'Miami',
        uf: 'FL',
      },
    },
  },
  {
    codigo: 'AWA566',
    codigo_cliente: 1,
    estado: 'EMBARCADA',
    data: '10/04/25 08:30',
    valor: 50,
    milhas_utilizadas: 11,
    quantidade_poltronas: 3,
    voo: {
      codigo: 'MNO345',
      data: '10/04/25 14:00',
      valor_passagem: 50,
      quantidade_poltronas_total: 180,
      quantidade_poltronas_ocupadas: 40,
      estado: 'CANCELADO',
      aeroporto_origem: {
        codigo: 'BSB',
        nome: 'Aeroporto de Brasília',
        cidade: 'Brasília',
        uf: 'DF',
      },
      aeroporto_destino: {
        codigo: 'LIS',
        nome: 'Humberto Delgado Airport',
        cidade: 'Lisboa',
        uf: 'N/A',
      },
    },
  },
])

export async function getBooking(): Promise<Reserve[]> {
  return new Promise((res) => {
    res(booking.value)
  })
}

export async function getBookingByUserCode(): Promise<Reserve[]> {
  const authStore = useAuthStore()

  return new Promise((res) => {
    res(
      booking.value.filter(
        (reservation) => reservation.codigo_cliente === authStore.user?.usuario.codigo,
      ),
    )
  })
}

export async function searchReserves(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const booking = await getBooking()

  return booking.filter((reserve) => reserve.codigo.toUpperCase() === searchCode)
}

export async function searchReservesByFlightCode(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const booking = await getBooking()

  return booking.filter(
    (reserve) => reserve.voo.codigo.toLocaleLowerCase() === searchCode.toLocaleLowerCase(),
  )
}

export async function cancelReservation(reservationCode: string) {
  const milesStore = useMilesStore()
  const authStore = useAuthStore()

  if (!reservationCode) return

  const reservation = booking.value.find((r) => r.codigo === reservationCode)

  if (reservation) {
    reservation.estado = 'CANCELADA'
    milesStore.setTotalMiles(milesStore.totalMiles + reservation.milhas_utilizadas)

    const newExtract: ExtractItem = {
      codigo_cliente: authStore.user?.usuario.codigo || 0,
      data: getTodayDate(),
      codigo_reserva: null,
      valor_reais: (milesStore.pricePerMile * reservation.milhas_utilizadas).toLocaleString(
        'pt-BR',
        {
          style: 'currency',
          currency: 'BRL',
        },
      ),
      quantidade_milhas: reservation.milhas_utilizadas,
      descricao: 'DEVOLUÇÃO DE MILHAS',
      tipo: 'ENTRADA',
    }

    registerExtract(newExtract)
  } else {
    console.log(`Reserva com ID ${reservationCode} não encontrada.`)
  }
}

export async function getReservationByCodeAndFlightCode(
  code: string,
  flightCode: string,
): Promise<Reserve | undefined> {
  const booking = await getBooking()

  return booking.find((reserve) => reserve.codigo === code && reserve.voo.codigo === flightCode)
}

export async function cancelReservationByFlightCode(flightCode: string) {
  const booking = await getBookingByUserCode()

  booking.forEach((reservation) => {
    if (reservation.voo.codigo === flightCode) {
      reservation.estado = 'CANCELADO VOO'
    }
  })
}

async function getFlightsInNext48hrs(): Promise<Reserve[]> {
  const now = new Date()
  const nowTime = now.getTime()
  const limitTime = nowTime + 48 * 60 * 60 * 1000
  const booking = await getBookingByUserCode()

  return booking.filter((reserve) => {
    const [datePart, timePart] = reserve.voo.data.split(' ')
    const [day, month, year] = datePart.split('/').map(Number)
    const [hour, minute] = timePart.split(':').map(Number)

    const flightDate = new Date(2000 + year, month - 1, day, hour, minute)
    const flightTime = flightDate.getTime()

    return flightTime > nowTime && flightTime <= limitTime
  })
}

export async function getCheckInFlightsInNext48Hrs(): Promise<Reserve[]> {
  const flights = await getFlightsInNext48hrs()

  return flights.filter((reserve) => ['CRIADA', 'CHECK-IN'].includes(reserve.estado.toUpperCase()))
}

export async function updateReservationStatus(
  reservationCode: string,
  newStatus: Reserve['estado'],
): Promise<void> {
  const booking = await getBooking()
  const reservation = booking.find((r) => r.codigo === reservationCode)

  if (reservation) {
    reservation.estado = newStatus
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
      codigo: code,
      codigo_cliente: authStore.user!.usuario.codigo,
      estado: 'CRIADA',
      data: getTodayDate(),
      valor: data.price,
      milhas_utilizadas: data.miles,
      quantidade_poltronas: 3,
      voo: {
        codigo: data.flight.codigo ?? '',
        data: data.flight.data ?? '',
        valor_passagem: 213,
        quantidade_poltronas_total: 180,
        quantidade_poltronas_ocupadas: 155,
        estado: 'CONFIRMADO',
        aeroporto_origem: {
          codigo: data.flight.aeroporto_origem.codigo ?? '',
          nome: data.flight.aeroporto_origem.nome ?? '',
          cidade: data.flight.aeroporto_origem.cidade ?? '',
          uf: data.flight.aeroporto_origem.uf ?? '',
        },
        aeroporto_destino: {
          codigo: data.flight.aeroporto_destino.codigo ?? '',
          nome: data.flight.aeroporto_destino.nome ?? '',
          cidade: data.flight.aeroporto_destino.cidade ?? '',
          uf: data.flight.aeroporto_destino.uf ?? '',
        },
      },
    })

    res(code)
  })
}
