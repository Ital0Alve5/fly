import api from '@/lib/axios'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import type { AxiosResponse } from 'axios'

export default async function login(data: {
  login: string
  senha: string
}): Promise<AxiosResponse<AuthenticatedUserData>> {
  return await api.post('/login', data)
}
