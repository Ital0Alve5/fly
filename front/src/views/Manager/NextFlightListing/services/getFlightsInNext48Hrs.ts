import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'
import type { AxiosResponse } from 'axios'

export default async function getFlightsInNext48Hrs(): Promise<AxiosResponse<Flight[]>> {
  const now = new Date().toISOString().slice(0, 10)
  const in48h = new Date(new Date().getTime() + 48 * 60 * 60 * 1000).toISOString().slice(0, 10)

  return await api.get(`/voos?data=${now}&data-fim=${in48h}`)
}
