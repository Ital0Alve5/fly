import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { AuthenticatedUserData, Client } from '@/types/Auth/AuthenticatedUserData'
import { registerClient } from '@/clientService/ClientService'
import type { AxiosResponse } from 'axios'
import { AxiosError } from 'axios'

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
    if (!newUser.nome) {
      throw new Error('O campo "Nome" é obrigatório.')
    }
    if (!newUser.email) {
      throw new Error('O campo "E-mail" é obrigatório.')
    }
    if (!newUser.cpf) {
      throw new Error('O campo "CPF" é obrigatório.')
    }
    if (!newUser.endereco.cep) {
      throw new Error('O campo "CEP" é obrigatório.')
    }
    if (!newUser.endereco.numero) {
      throw new Error('O campo "Número" é obrigatório.')
    }
  
    try {
      const response = await registerClient(newUser)
      return response
    } catch (error) {
      if (error instanceof AxiosError) {
        if (error.response?.status === 409) {
          throw new Error('CPF ou E-mail já cadastrado.')
        } else if (error.response) {
          throw new Error(`Erro ${error.response.status}: ${error.response.statusText}`)
        }
      }
      throw new Error('Erro ao registrar cliente.')
    }
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
