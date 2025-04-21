import type { Reserve } from '@/types/Reserve'
import { ref, type Ref } from 'vue'
import { getTodayDate } from '@/utils/date/getTodayDate'
import { generateUniqueCode } from '@/utils/generateRandomCode'
import { useAuthStore } from '@/stores/auth'
import { useMilesStore } from '@/stores/miles'
import { registerExtract, type ExtractItem } from './extract'
import type { Flight } from '@/types/Flight'
import { flights, reserveSeats } from './flight'

export const booking: Ref<Reserve[]> = ref([
  {
    codigo: 'AWN123',
    codigo_cliente: 1,
    estado: 'CHECK-IN',
    data: '22/04/25 08:30',
    valor: 200,
    milhas_utilizadas: 17,
    quantidade_poltronas: 2,
    voo: {
      codigo: 'STU901',
      data: '22/04/25 14:00',
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
    data: '22/04/25 08:30',
    valor: 50,
    milhas_utilizadas: 11,
    quantidade_poltronas: 3,
    voo: {
      codigo: 'MNO345',
      data: '22/04/25 14:00',
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
        nome: 'Aeroporto Humberto Delgado',
        cidade: 'Lisboa',
        uf: 'PT',
      },
    },
  },
])
export async function getBooking(): Promise<Reserve[]> {
  booking.value.forEach((reserva) => {
    const vooAtual = flights.find((f) => f.codigo === reserva.voo.codigo)
    if (vooAtual) reserva.voo = vooAtual
  })

  return booking.value
}

export async function getBookingByUserCode(): Promise<Reserve[]> {
  const authStore = useAuthStore()

  const reservas = booking.value.filter(
    (reservation) => reservation.codigo_cliente === authStore.user?.usuario.codigo,
  )

  reservas.forEach((reservation) => {
    const vooAtual = flights.find((f) => f.codigo === reservation.voo.codigo)
    if (vooAtual) reservation.voo = vooAtual
  })

  return reservas
}

export async function searchReserves(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const reservas = booking.value.filter((reserve) => reserve.codigo.toUpperCase() === searchCode)

  reservas.forEach((reserve) => {
    const vooAtual = flights.find((f) => f.codigo === reserve.voo.codigo)
    if (vooAtual) reserve.voo = vooAtual
  })

  return reservas
}

export async function searchReservesByFlightCode(code: string): Promise<Reserve[]> {
  const searchCode = code.trim().toUpperCase()
  const reservas = booking.value.filter(
    (reserve) => reserve.voo.codigo.toUpperCase() === searchCode,
  )

  reservas.forEach((reserve) => {
    const vooAtual = flights.find((f) => f.codigo === reserve.voo.codigo)
    if (vooAtual) reserve.voo = vooAtual
  })

  return reservas
}

export async function cancelReservation(reservationCode: string) {
  const milesStore = useMilesStore()
  const authStore = useAuthStore()

  if (!reservationCode) return

  const reservation = booking.value.find((r) => r.codigo === reservationCode)

  if (reservation && reservation.estado !== 'CANCELADA') {
    reservation.estado = 'CANCELADA'
    milesStore.setTotalMiles(milesStore.totalMiles + reservation.milhas_utilizadas)

    const vooRegistrado = flights.find((f) => f.codigo === reservation.voo.codigo)
    if (vooRegistrado) {
      vooRegistrado.quantidade_poltronas_ocupadas = Math.max(
        0,
        vooRegistrado.quantidade_poltronas_ocupadas - reservation.quantidade_poltronas,
      )
    }

    const valorDevolvido = Math.max(0, milesStore.pricePerMile * reservation.milhas_utilizadas)

    const newExtract: ExtractItem = {
      codigo_cliente: authStore.user?.usuario.codigo || 0,
      data: getTodayDate(),
      codigo_reserva: null,
      valor_reais: valorDevolvido.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      }),
      quantidade_milhas: reservation.milhas_utilizadas,
      descricao: 'DEVOLUÇÃO DE MILHAS',
      tipo: 'ENTRADA',
    }

    registerExtract(newExtract)

    booking.value = [...booking.value]
  } else if (!reservation) {
    console.log(`Reserva com ID ${reservationCode} não encontrada.`)
  }
}

export async function getReservationByCodeAndFlightCode(
  code: string,
  flightCode: string,
): Promise<Reserve | undefined> {
  const reserva = booking.value.find((r) => r.codigo === code && r.voo.codigo === flightCode)

  if (reserva) {
    const vooAtual = flights.find((f) => f.codigo === reserva.voo.codigo)
    if (vooAtual) reserva.voo = vooAtual
  }

  return reserva
}

export async function cancelReservationByFlightCode(flightCode: string) {
  const reservas = booking.value.filter((reservation) => reservation.voo.codigo === flightCode)

  reservas.forEach((reservation) => {
    if (reservation.estado !== 'CANCELADA') {
      reservation.estado = 'CANCELADO VOO'

      const vooAtual = flights.find((f) => f.codigo === reservation.voo.codigo)
      if (vooAtual) reservation.voo = vooAtual
    }
  })
}

async function getFlightsInNext48hrs(): Promise<Reserve[]> {
  const now = new Date()
  const nowTime = now.getTime()
  const limitTime = nowTime + 48 * 60 * 60 * 1000

  const reservas = await getBookingByUserCode()

  return reservas.filter((reserve) => {
    const [datePart, timePart] = reserve.voo.data.split(' ')
    const [day, month, year] = datePart.split('/').map(Number)
    const [hour, minute] = timePart.split(':').map(Number)
    const flightTime = new Date(2000 + year, month - 1, day, hour, minute).getTime()

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
    if (reservation.estado === 'CANCELADA') return
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
  seats: number
}): Promise<string> {
  const authStore = useAuthStore()

  const code = generateUniqueCode()

  reserveSeats(data.flight.codigo, data.seats)

  booking.value.push({
    codigo: code,
    codigo_cliente: authStore.user!.usuario.codigo,
    estado: 'CRIADA',
    data: getTodayDate(),
    valor: data.price,
    milhas_utilizadas: data.miles,
    quantidade_poltronas: data.seats,
    voo: {
      ...data.flight,
      quantidade_poltronas_ocupadas: data.flight.quantidade_poltronas_ocupadas + data.seats,
    },
  })

  return code
}
