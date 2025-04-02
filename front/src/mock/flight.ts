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
    dateTime: '2025-04-01T10:30:00',
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
]

export function getFlightByCode(code: string): Flight | undefined {
  return flights.find((f) => f.code === code)
}

export async function searchFlights(
  originAirport: string,
  destinationAirport: string,
): Promise<Flight[]> {
  const searchOrigin = originAirport.toLowerCase().trim()
  const searchDestination = destinationAirport.toLowerCase().trim()

  // Sleep de 2 segundos para simular chamada a API
  await new Promise((resolve) => setTimeout(() => resolve(undefined), 2000))

  return flights.filter((flight) => {
    const flightOrigin = flight.originAirport.toLowerCase()
    const flightDestination = flight.destinationAirport.toLowerCase()

    const matchesOrigin = !searchOrigin || flightOrigin.includes(searchOrigin)
    const matchesDestination = !searchDestination || flightDestination.includes(searchDestination)

    return matchesOrigin && matchesDestination
  })
}

export default { flights, searchFlights }
