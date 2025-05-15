import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { AuthenticatedUserData, Client } from '@/types/Auth/AuthenticatedUserData'
import { registerClient } from '@/clientService/ClientService'
import type { AxiosResponse } from 'axios'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = useLocalStorage('auth/isAuthenticated', false)
  const user = useLocalStorage<AuthenticatedUserData | null>('auth/user', null, {
    serializer: {
      read: (v) => (v ? JSON.parse(v) : null),
      write: (v) => JSON.stringify(v),
    },
  })

  async function login(userData: AuthenticatedUserData) {
    isAuthenticated.value = true

    user.value = {
      access_token: userData.access_token,
      token_type: userData.token_type,
      tipo: userData.tipo,
      senha: '',
      usuario: userData.usuario,
    }
  }

  async function register(newUser: Client): Promise<AxiosResponse<Client>> {
    if (!newUser.nome || !newUser.email || !newUser.cpf || !newUser.endereco.cep) {
      throw new Error('Todos os campos são obrigatórios.')
    }
    return await registerClient(newUser)
  }

  function logout() {
    isAuthenticated.value = false
    user.value = null
  }

  return {
    isAuthenticated,
    user,
    login,
    register,
    logout,
  }
})
