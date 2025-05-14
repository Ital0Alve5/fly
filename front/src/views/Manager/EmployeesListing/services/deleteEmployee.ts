import api from '@/lib/axios'
import type { Employee } from '@/types/Auth/Employee'
import type { AxiosResponse } from 'axios'

export default async function addNewEmployee(employeeId: number): Promise<AxiosResponse<Employee>> {
  return await api.delete(`/funcionarios/${employeeId}`)
}
