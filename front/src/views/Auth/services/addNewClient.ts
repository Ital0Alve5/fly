import api from '@/lib/axios'
import type { Client } from '@/types/Auth/AuthenticatedUserData'
import type { AxiosResponse } from 'axios'

export default async function addNewClient(clientData: {
    nome: string
    email: string
    cpf: string
    endereco: {
      cep: string
      uf: string
      cidade: string
      bairro: string
      rua: string
      numero: string
      complemento: string
    }
  }): Promise<AxiosResponse<Client>> {
    return await api.post('/clientes', clientData)
  }