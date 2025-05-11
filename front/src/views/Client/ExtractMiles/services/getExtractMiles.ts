import api from '@/lib/axios'
import type { AxiosResponse } from 'axios'
import type { MilesExtractResponse, ApiResponse } from '@/types/Api'

export default async function getExtract( clientId: number ): Promise<AxiosResponse<ApiResponse<MilesExtractResponse>>> {
    return await api.get(`/clientes/${clientId}/milhas/extrato`)
  }