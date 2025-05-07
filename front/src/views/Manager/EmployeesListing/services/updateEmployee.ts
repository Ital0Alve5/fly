import api from '@/lib/axios'
import { Employee } from '@/types/Auth/Employee'
import { AxiosResponse } from 'axios'

export default async function updateEmployee(data: {
  codigo: number,
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}): Promise<AxiosResponse<Employee>> {
  return await api.put(`/funcionarios/${data.codigo}`, data)
}