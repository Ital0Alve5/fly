import api from '@/lib/axios'
import { Employee } from '@/types/Auth/Employee'
import { AxiosResponse } from 'axios'

export default async function addNewEmployee(employeeId: number): Promise<AxiosResponse<Employee>> {
  return await api.delete(`/funcionarios/${employeeId}`)
}