import api from '@/lib/axios'
import type { Reserve } from '@/types/Reserve'
import type { AxiosResponse } from 'axios'

export default async function createReservatio(data: {
  codigo_cliente: number
  valor: number
  milhas_utilizadas: number
  quantidade_poltronas: number
  codigo_voo: string
  codigo_aeroporto_origem: string
  codigo_aeroporto_destino: string
}): Promise<AxiosResponse<Reserve>> {
  return await api.post('/reservas', data)
}
