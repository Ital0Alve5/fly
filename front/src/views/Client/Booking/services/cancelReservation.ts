import api from '@/lib/axios'
import type { Reserve } from '@/types/Reserve'
import type { AxiosResponse } from 'axios'

export default async function cancelReservation(codigo: string): Promise<AxiosResponse<Reserve>> {
  return await api.delete(`/reservas/${codigo}`)
}
