import api from '@/lib/axios'
import type { History } from '@/types/Reserve'
import type { AxiosResponse } from 'axios'

export default async function getReservationHistory(
  codigo: string,
): Promise<AxiosResponse<History>> {
  return await api.get(`/reservas/${codigo}/historico`)
}
