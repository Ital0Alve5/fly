import api from '@/lib/axios'
import type { AxiosResponse } from 'axios'
import type { MilesSummaryResponse, ApiResponse } from '@/types/Api'

export default async function getSummary(clientId: number): Promise<AxiosResponse<ApiResponse<MilesSummaryResponse>>> {
    return await api.get(`/clientes/${clientId}/milhas`)
  }