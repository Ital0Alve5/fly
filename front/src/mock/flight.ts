import { formatFromDate } from '@/utils/date/formatFromDate'
import { loadCityByAirpoirt } from './airpoirts'

export type Flight = {
  originAirport: string
  destinationAirport: string
  dateTime: string
  code: string
  origin: string
  destination: string
  price: number
  status: 'CONFIRMADO' | 'CANCELADO' | 'REALIZADO'
}

export const flights: Flight[] = [
  {
    originAirport: 'GRU',
    destinationAirport: 'JFK',
    dateTime: '08/04/25 14:00',
    code: 'HGP123',
    origin: 'São Paulo',
    destination: 'New York',
    price: 250.0,
    status: 'CONFIRMADO',
  },
  {
    originAirport: 'GIG',
    destinationAirport: 'MIA',
    dateTime: '20/04/25 14:00',
    code: 'STU901',
    origin: 'Rio de Janeiro',
    destination: 'Miami',
    price: 200.0,
    status: 'REALIZADO',
  },
  {
    originAirport: 'BSB',
    destinationAirport: 'LIS',
    dateTime: '22/04/25 14:00',
    code: 'MNO345',
    origin: 'Brasília',
    destination: 'Lisboa',
    price: 50.0,
    status: 'CANCELADO',
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'SDU',
    dateTime: '30/04/25 09:15',
    code: 'FLW316',
    origin: 'São Paulo',
    destination: 'Rio de Janeiro',
    price: 245.5,
    status: 'REALIZADO',
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'EZE',
    dateTime: '02/05/25 08:45',
    code: 'FLK822',
    origin: 'São Paulo',
    destination: 'Buenos Aires',
    price: 320,
    status: 'REALIZADO',
  },
  {
    originAirport: 'BSB',
    destinationAirport: 'CGH',
    dateTime: '04/05/25 07:30',
    code: 'FLY156',
    origin: 'Brasília',
    destination: 'São Paulo',
    price: 175.2,
    status: 'REALIZADO',
  },
  {
    originAirport: 'CWB',
    destinationAirport: 'MAO',
    dateTime: '06/05/25 12:00',
    code: 'PGP001',
    origin: 'Curitiba',
    destination: 'Manaus',
    price: 2000,
    status: 'REALIZADO',
  },
  {
    originAirport: 'REC',
    destinationAirport: 'FOR',
    dateTime: '08/05/25 18:20',
    code: 'FLT413',
    origin: 'Recife',
    destination: 'Fortaleza',
    price: 1.9,
    status: 'REALIZADO',
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'MEX',
    dateTime: '12/04/25 15:00',
    code: 'NOA001',
    origin: 'São Paulo',
    destination: 'Mexico City',
    price: 1800,
    status: 'CONFIRMADO',
  },
  {
    originAirport: 'FRA',
    destinationAirport: 'LHR',
    dateTime: '11/04/25 08:45',
    code: 'NOB002',
    origin: 'Frankfurt',
    destination: 'London',
    price: 900,
    status: 'CONFIRMADO',
  },
  {
    originAirport: 'MIA',
    destinationAirport: 'GRU',
    dateTime: '11/04/25 23:00',
    code: 'NOC003',
    origin: 'Miami',
    destination: 'São Paulo',
    price: 1200,
    status: 'CONFIRMADO',
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'FRA',
    dateTime: '12/04/25 06:30',
    code: 'NOD004',
    origin: 'Paris',
    destination: 'Frankfurt',
    price: 700,
    status: 'CONFIRMADO',
  },
]

export function getFlightByCode(code: string): Flight | undefined {
  return flights.find((f) => f.code === code)
}

export async function searchFlights(origin: string, destination: string): Promise<Flight[]> {
  const searchOrigin = origin.toLowerCase().trim()
  const searchDestination = destination.toLowerCase().trim()

  // Simula chamada à API com um delay de 2 segundos
  await new Promise((resolve) => setTimeout(() => resolve(undefined), 2000))

  const now = new Date()

  return flights.filter((flight) => {
    const flightDate = new Date(flight.dateTime)

    if (flightDate <= now && flight.status !== 'REALIZADO') return false

    const flightOriginCity = flight.origin.toLowerCase()
    const flightOriginAirport = flight.originAirport.toLowerCase()
    const flightDestinationCity = flight.destination.toLowerCase()
    const flightDestinationAirport = flight.destinationAirport.toLowerCase()

    const matchesOrigin =
      !searchOrigin ||
      flightOriginCity.includes(searchOrigin) ||
      flightOriginAirport.includes(searchOrigin)

    const matchesDestination =
      !searchDestination ||
      flightDestinationCity.includes(searchDestination) ||
      flightDestinationAirport.includes(searchDestination)

    return matchesOrigin && matchesDestination
  })
}

function parseDateTime(dateTime: string): Date {
  const [datePart, timePart] = dateTime.split(' ')
  const [day, month, year] = datePart.split('/').map(Number)
  const [hours, minutes] = timePart.split(':').map(Number)
  return new Date(2000 + year, month - 1, day, hours, minutes)
}

function sortFlightsByDateTime(flights: Flight[]): Flight[] {
  return [...flights].sort((a, b) => {
    const dateA = parseDateTime(a.dateTime).getTime()
    const dateB = parseDateTime(b.dateTime).getTime()
    return dateA - dateB
  })
}

export function getFlightsInNext48Hours(): Flight[] {
  const now = new Date()
  const in48h = new Date(now.getTime() + 48 * 60 * 60 * 1000)

  const filtered = flights.filter((flight) => {
    const flightDate = parseDateTime(flight.dateTime)
    return flightDate > now && flightDate <= in48h && flight.status !== 'CANCELADO'
  })

  return sortFlightsByDateTime(filtered)
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
  const codeExists = flights.find((flight) => flight.code === data.code)

  if (codeExists) {
    throw new Error('Código já cadastrado.')
  }

  // Simula chamada à API com um delay de 2 segundos
  await new Promise((resolve) => setTimeout(() => resolve(undefined), 2000))

  const origin = loadCityByAirpoirt(data.originAirport)
  const destination = loadCityByAirpoirt(data.destinationAirport)

  flights.push({
    originAirport: data.originAirport,
    destinationAirport: data.destinationAirport,
    price: data.price,
    code: data.code,
    dateTime: formatFromDate(data.date),
    origin,
    destination,
    status: 'CONFIRMADO',
  })
}

export function cancelFlight(code: string): boolean {
  const flight = flights.find((flight) => flight.code === code)
  if (flight) {
    flight.status = 'CANCELADO'
    return true
  }
  return false
}
export function performFlight(flightCode: string) {
  const flight = flights.find((f) => f.code === flightCode)
  if (flight) {
    flight.status = 'REALIZADO'
  }
}

export default {
  flights,
  searchFlights,
  getFlightsInNext48Hours,
  cancelFlight,
  performFlight,
  registerFlight,
}
