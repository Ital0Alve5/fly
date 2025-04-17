import { formatFromDate } from '@/utils/date/formatFromDate'
import { loadCityByAirpoirt } from './airpoirts'
import type { Flight } from '@/types/Flight'

export const flights: Flight[] = [
  {
    codigo: 'HGP123',
    data: '14/04/25 14:00',
    valor_passagem: 250.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 150,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'GRU',
      nome: 'Aeroporto de Guarulhos',
      cidade: 'São Paulo',
      uf: 'SP',
    },
    aeroporto_destino: {
      codigo: 'JFK',
      nome: 'John F. Kennedy International Airport',
      cidade: 'New York',
      uf: 'NY',
    },
  },
  {
    codigo: 'STU901',
    data: '15/04/25 14:00',
    valor_passagem: 200.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 170,
    estado: 'CONFIRMADO',
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
  {
    codigo: 'MNO345',
    data: '22/04/25 14:00',
    valor_passagem: 50.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 40,
    estado: 'CONFIRMADO',
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
  {
    codigo: 'FLW316',
    data: '30/04/25 09:15',
    valor_passagem: 245.5,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 160,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'GRU',
      nome: 'Aeroporto de Guarulhos',
      cidade: 'São Paulo',
      uf: 'SP',
    },
    aeroporto_destino: {
      codigo: 'SDU',
      nome: 'Santos Dumont',
      cidade: 'Rio de Janeiro',
      uf: 'RJ',
    },
  },
  {
    codigo: 'FLK822',
    data: '02/05/25 08:45',
    valor_passagem: 320.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 170,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'GRU',
      nome: 'Aeroporto de Guarulhos',
      cidade: 'São Paulo',
      uf: 'SP',
    },
    aeroporto_destino: {
      codigo: 'EZE',
      nome: 'Ministro Pistarini',
      cidade: 'Buenos Aires',
      uf: 'AR',
    },
  },
  {
    codigo: 'FLY156',
    data: '04/05/25 07:30',
    valor_passagem: 175.2,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 165,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'BSB',
      nome: 'Aeroporto de Brasília',
      cidade: 'Brasília',
      uf: 'DF',
    },
    aeroporto_destino: {
      codigo: 'CGH',
      nome: 'Congonhas',
      cidade: 'São Paulo',
      uf: 'SP',
    },
  },
  {
    codigo: 'PGP001',
    data: '06/05/25 12:00',
    valor_passagem: 2000.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 180,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'CWB',
      nome: 'Aeroporto Afonso Pena',
      cidade: 'Curitiba',
      uf: 'PR',
    },
    aeroporto_destino: {
      codigo: 'MAO',
      nome: 'Eduardo Gomes',
      cidade: 'Manaus',
      uf: 'AM',
    },
  },
  {
    codigo: 'FLT413',
    data: '08/05/25 18:20',
    valor_passagem: 1.9,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 30,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'REC',
      nome: 'Aeroporto do Recife',
      cidade: 'Recife',
      uf: 'PE',
    },
    aeroporto_destino: {
      codigo: 'FOR',
      nome: 'Pinto Martins',
      cidade: 'Fortaleza',
      uf: 'CE',
    },
  },
  {
    codigo: 'NOA001',
    data: '12/04/25 15:00',
    valor_passagem: 1800.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 120,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'GRU',
      nome: 'Aeroporto de Guarulhos',
      cidade: 'São Paulo',
      uf: 'SP',
    },
    aeroporto_destino: {
      codigo: 'MEX',
      nome: 'Benito Juárez',
      cidade: 'Mexico City',
      uf: 'MX',
    },
  },
  {
    codigo: 'NOB002',
    data: '11/04/25 08:45',
    valor_passagem: 900.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 160,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'FRA',
      nome: 'Frankfurt am Main',
      cidade: 'Frankfurt',
      uf: 'DE',
    },
    aeroporto_destino: {
      codigo: 'LHR',
      nome: 'Heathrow',
      cidade: 'London',
      uf: 'UK',
    },
  },
  {
    codigo: 'NOC003',
    data: '11/04/25 23:00',
    valor_passagem: 1200.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 150,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'MIA',
      nome: 'Miami International Airport',
      cidade: 'Miami',
      uf: 'FL',
    },
    aeroporto_destino: {
      codigo: 'GRU',
      nome: 'Aeroporto de Guarulhos',
      cidade: 'São Paulo',
      uf: 'SP',
    },
  },
  {
    codigo: 'NOD004',
    data: '12/04/25 06:30',
    valor_passagem: 700.0,
    quantidade_poltronas_total: 180,
    quantidade_poltronas_ocupadas: 145,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: 'CDG',
      nome: 'Charles de Gaulle',
      cidade: 'Paris',
      uf: 'FR',
    },
    aeroporto_destino: {
      codigo: 'FRA',
      nome: 'Frankfurt am Main',
      cidade: 'Frankfurt',
      uf: 'DE',
    },
  },
]

export function getFlightByCode(code: string): Flight | undefined {
  return flights.find((f) => f.codigo === code)
}

export async function searchFlights(origin: string, destination: string): Promise<Flight[]> {
  const searchOrigin = origin.toLowerCase().trim()
  const searchDestination = destination.toLowerCase().trim()

  await new Promise((resolve) => setTimeout(resolve, 2000))

  const now = new Date()

  return flights.filter((flight) => {
    const flightDate = parseDateTime(flight.data)
    if (flightDate <= now) return false

    const flightCityOrigin = flight.aeroporto_origem.cidade.toLowerCase()
    const flightCityDestination = flight.aeroporto_destino.cidade.toLowerCase()
    const flightCodeOrigin = flight.aeroporto_origem.codigo.toLowerCase()
    const flightCodeDestination = flight.aeroporto_destino.codigo.toLowerCase()

    return (
      (flightCityOrigin.includes(searchOrigin) || flightCodeOrigin.includes(searchOrigin)) &&
      (flightCityDestination.includes(searchDestination) ||
        flightCodeDestination.includes(searchDestination))
    )
  })
}

function parseDateTime(dateTime: string): Date {
  const [datePart, timePart] = dateTime.split(' ')
  const [day, month, year] = datePart.split('/').map(Number)
  const [hours, minutes] = timePart.split(':').map(Number)
  return new Date(2000 + year, month - 1, day, hours, minutes)
}

function sortFlightsByDateTime(flights: Flight[]): Flight[] {
  return [...flights].sort(
    (a, b) => parseDateTime(a.data).getTime() - parseDateTime(b.data).getTime(),
  )
}

export function getFlightsInNext48Hours(): Flight[] {
  const now = new Date()
  const in48h = new Date(now.getTime() + 48 * 60 * 60 * 1000)
  return sortFlightsByDateTime(
    flights.filter((flight) => {
      const flightDate = parseDateTime(flight.data)
      return flightDate > now && flightDate <= in48h && flight.estado !== 'CANCELADO'
    }),
  )
}

type RegisterFlightFormType = {
  code: string
  originAirport: string
  destinationAirport: string
  date: Date
  price: number
  seatsNumber: number
}

export async function registerFlight(data: RegisterFlightFormType) {
  await new Promise((resolve) => setTimeout(resolve, 2000))

  const originCity = loadCityByAirpoirt(data.originAirport)
  const destinationCity = loadCityByAirpoirt(data.destinationAirport)

  flights.push({
    codigo: data.code,
    data: formatFromDate(data.date),
    valor_passagem: data.price,
    quantidade_poltronas_total: data.seatsNumber,
    quantidade_poltronas_ocupadas: 0,
    estado: 'CONFIRMADO',
    aeroporto_origem: {
      codigo: data.originAirport,
      nome: `Aeroporto de ${originCity}`,
      cidade: originCity,
      uf: '',
    },
    aeroporto_destino: {
      codigo: data.destinationAirport,
      nome: `Aeroporto de ${destinationCity}`,
      cidade: destinationCity,
      uf: '',
    },
  })
}

export function cancelFlight(code: string): boolean {
  const flight = flights.find((f) => f.codigo === code)
  if (flight) {
    flight.estado = 'CANCELADO'
    return true
  }
  return false
}

export function performFlight(code: string): void {
  const flight = flights.find((f) => f.codigo === code)
  if (flight) flight.estado = 'REALIZADO'
}

export function reserveSeats(code: string, numberOfSeats: number): void {
  const flight = flights.find((f) => f.codigo === code)
  if (flight) {
    flight.quantidade_poltronas_ocupadas += numberOfSeats;
  }
}


export default {
  flights,
  searchFlights,
  getFlightsInNext48Hours,
  cancelFlight,
  performFlight,
  registerFlight,
  reserveSeats
}
