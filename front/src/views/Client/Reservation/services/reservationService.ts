import api from '@/lib/axios'
import type { Reserve } from '@/types/Reserve'
import type { AxiosResponse } from 'axios'

export async function listReservationsByClient(): Promise<AxiosResponse<Reserve[]>> {
  const stored = localStorage.getItem('auth/user')!
  const user = JSON.parse(stored)
  const clientCode = user.usuario.codigo
  return api.get<Reserve[]>(`/clientes/${clientCode}/reservas`)
}

export async function getReservationByCode(code: string): Promise<AxiosResponse<Reserve>> {
  return api.get<Reserve>(`/reservas/${code}`)
}

export async function createReservation(data: {
  codigo_cliente: number
  valor: number
  milhas_utilizadas: number
  quantidade_poltronas: number
  codigo_voo: string
  codigo_aeroporto_origem: string
  codigo_aeroporto_destino: string
}): Promise<AxiosResponse<Reserve>> {
  return api.post<Reserve>('/reservas', data)
}

export async function cancelReservationService(code: string): Promise<AxiosResponse<void>> {
  return api.delete<void>(`/reservas/${code}`)
}

export async function updateReservationStateService(
  code: string,
  estado: 'CRIADA' | 'CHECK-IN' | 'EMBARCADA' | 'CANCELADA',
): Promise<AxiosResponse<void>> {
  return api.patch<void>(`/reservas/${code}/estado`, { estado })
}
