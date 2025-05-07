import api from '@/lib/axios'
import { Employee } from '@/types/Auth/Employee'
import { AxiosResponse } from 'axios'

export default async function getAllEmployees(): Promise<AxiosResponse<Employee[]>> {
  return await api.get('/funcionarios')
}