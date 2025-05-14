import api from '@/lib/axios'
import type { Employee } from '@/types/Auth/Employee'
import type { AxiosResponse } from 'axios'

export default async function getAllEmployees(): Promise<AxiosResponse<Employee[]>> {
  return await api.get('/funcionarios')
}
