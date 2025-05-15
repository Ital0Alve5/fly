import api from '@/lib/axios'
import type { Employee } from '@/types/Auth/Employee'
import type { AxiosResponse } from 'axios'

export type NewEmployeeData = {
  nome: string
  email: string
  telefone: string
  cpf: string
  senha: string
}

function sanitizeEmployeeData(data: NewEmployeeData): NewEmployeeData {
  return {
    ...data,
    cpf: data.cpf.replace(/\D+/g, ''),
    telefone: data.telefone.replace(/\D+/g, ''),
  }
}

export default async function addNewEmployee(data: NewEmployeeData): Promise<AxiosResponse<Employee>> {
  try {
    const sanitized = sanitizeEmployeeData(data)
    return await api.post('/funcionarios', sanitized)
  } catch (error) {
    console.error('Erro ao adicionar funcionário:', error)
    throw error
  }
}
