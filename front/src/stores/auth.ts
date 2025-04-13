import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { AuthenticatedUserData, Client, Employee } from '@/types/Auth/AuthenticatedUserData'
import clientsMock from '@/mock/clients'
import employeesMock from '@/mock/employees'
import { passwords } from '@/mock/auth'

function generatedRandomPassword(): string {
  return Math.floor(1000 + Math.random() * 9000).toString()
}

function sendPasswordOnEmail(email: string, senha: string): void {
  console.log(`Senha enviada para ${email}: ${senha}`)
}

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = useLocalStorage('auth/isAuthenticated', false)
  const user = useLocalStorage<AuthenticatedUserData | null>('auth/user', null, {
    serializer: {
      read: (v) => (v ? JSON.parse(v) : null),
      write: (v) => JSON.stringify(v),
    },
  })

  async function login(email: string, password: string): Promise<AuthenticatedUserData | null> {
    let authenticatedUser: Client | Employee | null = null

    if (email.includes('@empresa.com')) {
      authenticatedUser = employeesMock.getEmployeeByEmail(email)

      if (passwords.employee[email] === password) {
        isAuthenticated.value = true

        user.value = {
          access_token: '',
          token_type: '',
          tipo: 'FUNCIONARIO',
          senha: password,
          usuario: authenticatedUser!,
        }

        return user.value
      }
    } else {
      authenticatedUser = clientsMock.getClientByEmail(email)

      if (passwords.client[email] === password) {
        isAuthenticated.value = true

        user.value = {
          access_token: '',
          token_type: '',
          tipo: 'CLIENTE',
          senha: password,
          usuario: authenticatedUser!,
        }

        return user.value
      }
    }

    return null
  }

  async function register(newUser: Client): Promise<AuthenticatedUserData | null> {
    if (!newUser.nome || !newUser.email || !newUser.cpf || !newUser.endereco.cep) {
      throw new Error('Todos os campos são obrigatórios.')
    }

    const senha = generatedRandomPassword()

    clientsMock.registerClient(newUser)

    sendPasswordOnEmail(newUser.email, senha)

    isAuthenticated.value = true

    user.value = {
      access_token: '',
      token_type: '',
      tipo: 'CLIENTE',
      senha: senha,
      usuario: newUser,
    }

    return user.value
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
