import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'
import type { Airport } from '@/types/Airpoirt'
import type { AxiosResponse } from 'axios'

export const cancelFlight = async (code: string): Promise<boolean> => {
  try {
    const response = await api.patch(`/voos/${code}/estado`, {
      estado: 'CANCELADO',
    })

    return response.status == 200
  } catch (error) {
    console.error('Erro ao cancelar o voo:', error)
    return false
  }
}

export const performFlight = async (code: string): Promise<boolean> => {
  try {
    const response = await api.patch(`/voos/${code}/estado`, {
      estado: 'REALIZADO',
    })

    return response.status == 200
  } catch (error) {
    console.error('Erro ao realizar o voo:', error)
    return false
  }
}

export const fetchFlightsNext48Hours = async (): Promise<Flight[]> => {
  try {
    const today = new Date()
    const endDate = new Date(today)
    endDate.setDate(today.getDate() + 2)

    const formatDate = (date: Date) => date.toISOString().split('T')[0]
    const startDate = formatDate(today)
    const formattedEndDate = formatDate(endDate)

    const response = await api.get(
      `/voos?data=${encodeURIComponent(startDate)}&data-fim=${encodeURIComponent(formattedEndDate)}`,
    )

    return response.data || []
  } catch (error) {
    console.error('Erro ao buscar voos das próximas 48 horas:', error)
    return []
  }
}

export const loadAllAirpoirts = async (): Promise<Airport[]> => {
  try {
    const response = await api.get(`/aeroportos`)
    return response.data || []
  } catch (error) {
    console.error('Erro ao buscar os aeroportos:', error)
    return []
  }
}

export const registerFlight = async (data: {
  data: string
  valor_passagem: number
  quantidade_poltronas_total: number
  quantidade_poltronas_ocupadas: number
  codigo_aeroporto_origem: string
  codigo_aeroporto_destino: string
}): Promise<AxiosResponse<Flight>> => {
  return await api.post(`/voos`, data)
}
