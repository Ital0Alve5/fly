import api from '@/lib/axios'
import type { Employee } from '@/types/Auth/Employee'
import type { AxiosResponse } from 'axios'

export default async function addNewEmployee(data: {
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}): Promise<AxiosResponse<Employee>> {
  data.cpf = data.cpf.replace(/\D+/g, '')
  data.telefone = data.telefone.replace(/\D+/g, '')

  return await api.post('/funcionarios', data)
}
