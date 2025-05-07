import api from '@/lib/axios'
import { Employee } from '@/types/Auth/Employee'
import { AxiosResponse } from 'axios'

export default async function addNewEmployee(data: {
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}): Promise<AxiosResponse<Employee>> {
  return await api.post('/funcionarios', data)
}