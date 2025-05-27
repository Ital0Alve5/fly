import api from '@/lib/axios'
import type { Employee } from '@/types/Auth/Employee'
import type { AxiosResponse } from 'axios'

export default async function updateEmployee(data: {
  codigo: number
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}): Promise<AxiosResponse<Employee>> {
  data.cpf = data.cpf.replace(/\D+/g, '')
  data.telefone = data.telefone.replace(/\D+/g, '')

  return await api.put(`/funcionarios/${data.codigo}`, data)
}
