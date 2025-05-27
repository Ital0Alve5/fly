import api from '@/lib/axios'
import type { Reserve } from '@/types/Reserve'
import type { AxiosResponse } from 'axios'

export default async function getReservationDetails(codigo: string): Promise<AxiosResponse<Reserve>> {
  return await api.get(`/reservas/${codigo}`)
}
