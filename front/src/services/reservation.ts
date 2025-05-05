import api from '@/lib/axios'
import axios from 'axios'
import type { ApiResponse } from '@/types/Api'
import type { Reserve } from '@/types/Reserve'

export async function getReservationByCode(codigo: string): Promise<ApiResponse<Reserve>> {
  try {
    const response = await api.get(`/reservas/${codigo}`)

    return {
      error: false,
      data: response.data,
    }
  } catch (err) {
    if (axios.isAxiosError(err) && err.response) {
      return {
        error: true,
        statusCode: err.response.status,
        message: err.response.data?.message || 'Unexpected error',
      }
    }

    return {
      error: true,
      statusCode: 500,
      message: 'Unexpected error',
    }
  }
}
