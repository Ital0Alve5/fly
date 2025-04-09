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
    dateTime: '01/04/25 10:30',
    code: 'ABC123',
    origin: 'São Paulo',
    destination: 'Nova York',
    price: 2000,
    status: 'REALIZADO',
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'CDG',
    dateTime: '02/04/25 09:00',
    code: 'DEF456',
    origin: 'São Paulo',
    destination: 'Paris',
    price: 3450,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'LAX',
    destinationAirport: 'JFK',
    dateTime: '03/04/25 12:00',
    code: 'GHI789',
    origin: 'Los Angeles',
    destination: 'Nova York',
    price: 500,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'LAX',
    destinationAirport: 'LHR',
    dateTime: '04/04/25 11:00',
    code: 'JKL012',
    origin: 'Los Angeles',
    destination: 'Londres',
    price: 1400,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'GRU',
    dateTime: '05/04/25 18:00',
    code: 'MNO345',
    origin: 'Paris',
    destination: 'São Paulo',
    price: 2300,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'JFK',
    destinationAirport: 'LAX',
    dateTime: '06/04/25 15:00',
    code: 'PQR678',
    origin: 'Nova York',
    destination: 'Los Angeles',
    price: 380,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'JFK',
    dateTime: '07/04/25 07:30',
    code: 'STU901',
    origin: 'São Paulo',
    destination: 'Nova York',
    price: 1980,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'LAX',
    dateTime: '08/04/25 14:00',
    code: 'VWX111',
    origin: 'Paris',
    destination: 'Los Angeles',
    price: 2600,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'LHR',
    destinationAirport: 'CDG',
    dateTime: '09/04/25 10:00',
    code: 'YZA222',
    origin: 'Londres',
    destination: 'Paris',
    price: 1300,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'JFK',
    destinationAirport: 'MEX',
    dateTime: '10/04/25 09:30',
    code: 'BCD333',
    origin: 'Nova York',
    destination: 'Cidade do México',
    price: 500,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'MAD',
    destinationAirport: 'JFK',
    dateTime: '11/04/25 16:00',
    code: 'EFG444',
    origin: 'Madrid',
    destination: 'Nova York',
    price: 1900,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'NRT',
    destinationAirport: 'CDG',
    dateTime: '12/04/25 20:00',
    code: 'HIJ555',
    origin: 'Tóquio',
    destination: 'Paris',
    price: 3300,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'FRA',
    destinationAirport: 'GRU',
    dateTime: '13/04/25 11:00',
    code: 'KLM666',
    origin: 'Frankfurt',
    destination: 'São Paulo',
    price: 2100,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'MIA',
    dateTime: '04/04/25 20:30',
    code: 'NEW101',
    origin: 'São Paulo',
    destination: 'Miami',
    price: 1900,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'LAX',
    destinationAirport: 'GRU',
    dateTime: '03/04/25 23:00',
    code: 'NEW102',
    origin: 'Los Angeles',
    destination: 'São Paulo',
    price: 1700,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'JFK',
    destinationAirport: 'GRU',
    dateTime: '02/04/25 15:00',
    code: 'NEW103',
    origin: 'Nova York',
    destination: 'São Paulo',
    price: 2200,
    status: 'CONFIRMADO'
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'MAD',
    dateTime: '04/04/25 06:15',
    code: 'NEW104',
    origin: 'Paris',
    destination: 'Madrid',
    price: 880,
    status: 'CONFIRMADO'
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

export default { flights, searchFlights, getFlightsInNext48Hours, cancelFlight, performFlight }
