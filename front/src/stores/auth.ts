import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { AuthenticatedUserData, Client, Employee } from '@/types/Auth/AuthenticatedUserData'
import clientsMock from '@/mock/clients'
import employeesMock from '@/mock/employees'
import { addEmployeePassword, addClientPassword } from '@/mock/auth'

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

    addClientPassword(newUser.email, senha)

    return user.value
  }

  async function registerEmployee(newEmployee: Employee) {
    const senha = generatedRandomPassword()

    employeesMock.registerEmployee(newEmployee)

    sendPasswordOnEmail(newEmployee.email, senha)

    addEmployeePassword(newEmployee.email, senha)
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
    registerEmployee,
  }
})
