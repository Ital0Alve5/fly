import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'

export const fetchAllFlights = async (origin: string, destination: string) => {
  try {
    const response = await api.get('/voos')
    const flights = response.data

    const searchOrigin = origin.toLowerCase().trim()
    const searchDestination = destination.toLowerCase().trim()

    return flights.filter((flight: Flight) => {
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
  } catch (error) {
    console.error('Erro ao buscar voos:', error)
    throw error
  }
}

export const fetchFlightByCode = async (code: string) => {
    try {
        const response = await api.get(`/voo/${code}`)
        const flights = response.data
      
        return flights;  
    } catch (error) {
        console.error('Erro ao buscar voo:', error)
        throw error
    }
}