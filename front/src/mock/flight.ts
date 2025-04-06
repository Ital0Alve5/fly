export type Flight = {
  originAirport: string
  destinationAirport: string
  dateTime: string
  code: string
  origin: string
  destination: string
  price: number
}

export const flights = [
  {
    originAirport: 'GRU',
    destinationAirport: 'JFK',
    dateTime: '2025-04-07T10:30:00',
    code: 'ABC123',
    origin: 'São Paulo',
    destination: 'Nova York',
    price: 2000,
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'CDG',
    dateTime: '2025-04-02T09:00:00',
    code: 'DEF456',
    origin: 'São Paulo',
    destination: 'Paris',
    price: 3450,
  },
  {
    originAirport: 'LAX',
    destinationAirport: 'JFK',
    dateTime: '2025-04-03T12:00:00',
    code: 'GHI789',
    origin: 'Los Angeles',
    destination: 'Nova York',
    price: 500,
  },
  {
    originAirport: 'LAX',
    destinationAirport: 'LHR',
    dateTime: '2025-04-04T11:00:00',
    code: 'JKL012',
    origin: 'Los Angeles',
    destination: 'Londres',
    price: 1400,
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'GRU',
    dateTime: '2025-04-05T18:00:00',
    code: 'MNO345',
    origin: 'Paris',
    destination: 'São Paulo',
    price: 2300,
  },
  {
    originAirport: 'JFK',
    destinationAirport: 'LAX',
    dateTime: '2025-04-06T15:00:00',
    code: 'PQR678',
    origin: 'Nova York',
    destination: 'Los Angeles',
    price: 380,
  },
  {
    originAirport: 'GRU',
    destinationAirport: 'JFK',
    dateTime: '2025-04-07T07:30:00',
    code: 'STU901',
    origin: 'São Paulo',
    destination: 'Nova York',
    price: 1980,
  },
  {
    originAirport: 'CDG',
    destinationAirport: 'LAX',
    dateTime: '2025-04-08T14:00:00',
    code: 'VWX111',
    origin: 'Paris',
    destination: 'Los Angeles',
    price: 2600,
  },
  {
    originAirport: 'LHR',
    destinationAirport: 'CDG',
    dateTime: '2025-04-09T10:00:00',
    code: 'YZA222',
    origin: 'Londres',
    destination: 'Paris',
    price: 1300,
  },
  {
    originAirport: 'JFK',
    destinationAirport: 'MEX',
    dateTime: '2025-04-10T09:30:00',
    code: 'BCD333',
    origin: 'Nova York',
    destination: 'Cidade do México',
    price: 500,
  },
  {
    originAirport: 'MAD',
    destinationAirport: 'JFK',
    dateTime: '2025-04-11T16:00:00',
    code: 'EFG444',
    origin: 'Madrid',
    destination: 'Nova York',
    price: 1900,
  },
  {
    originAirport: 'NRT',
    destinationAirport: 'CDG',
    dateTime: '2025-04-12T20:00:00',
    code: 'HIJ555',
    origin: 'Tóquio',
    destination: 'Paris',
    price: 3300,
  },
  {
    originAirport: 'FRA',
    destinationAirport: 'GRU',
    dateTime: '2025-04-13T11:00:00',
    code: 'KLM666',
    origin: 'Frankfurt',
    destination: 'São Paulo',
    price: 2100,
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

    if (flightDate <= now) return false

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

export default { flights, searchFlights }
