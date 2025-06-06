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

export const loadAllAirpoirts = async (): Promise<Airport[]> => {
  try {
    const response = await api.get(`/aeroportos`)
    return response.data || []
  } catch (error) {
    console.error('Erro ao buscar os aeroportos:', error)
    return []
  }
}

function formatToOffsetIso(dateInput: string): string {
  const date = new Date(dateInput)
  const pad = (n: number) => String(n).padStart(2, '0')

  const year = date.getFullYear()
  const month = pad(date.getMonth() + 1)
  const day = pad(date.getDate())
  const hour = pad(date.getHours())
  const minute = pad(date.getMinutes())
  const second = pad(date.getSeconds())

  return `${year}-${month}-${day}T${hour}:${minute}:${second}-03:00`
}

export const registerFlight = async (data: {
  data: string
  valor_passagem: number
  quantidade_poltronas_total: number
  quantidade_poltronas_ocupadas: number
  codigo_aeroporto_origem: string
  codigo_aeroporto_destino: string
}): Promise<AxiosResponse<Flight>> => {
  const dataComFuso = formatToOffsetIso(data.data)

  console.log({ ...data, data: dataComFuso })

  return await api.post(`/voos`, {
    ...data,
    data: dataComFuso,
  })
}
