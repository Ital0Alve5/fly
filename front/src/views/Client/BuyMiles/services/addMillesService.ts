import api from '@/lib/axios'
import type { AxiosResponse } from 'axios'
import type { MilesSummaryResponse, ApiResponse } from '@/types/Api'

export default async function addMiles( clientId: number, miles: number): Promise<AxiosResponse<ApiResponse<MilesSummaryResponse>>> {
    return await api.put(`/clientes/${clientId}/milhas`, { quantidade: miles })
  }