import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import clientsMock from '@/mock/clients'
import employeesMock from '@/mock/employees'

function gerarSenhaAleatoria(): string {
  return Math.floor(1000 + Math.random() * 9000).toString()
}

function enviarSenhaPorEmail(email: string, senha: string): void {
  console.log(`Senha enviada para ${email}: ${senha}`)
}

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const user = ref<AuthenticatedUserData | null>(null)

  async function login(email: string, password: string): Promise<AuthenticatedUserData | null> {
    const client = clientsMock.getClientByEmail(email)
    const employee = employeesMock.getEmployeeByEmail(email)

    if (
      (client || employee) &&
      (client?.password === password || employee?.password === password)
    ) {
      isAuthenticated.value = true
      user.value = client || employee
      return user.value
    }

    return null
  }

  async function register(newUser: {
    name: string
    email: string
    cpf: string
    cep: string
    isManager?: boolean
  }): Promise<AuthenticatedUserData | null> {
    if (!newUser.name || !newUser.email || !newUser.cpf || !newUser.cep) {
      throw new Error('Todos os campos são obrigatórios.')
    }

    const senha = gerarSenhaAleatoria()
    const newClient = {
      ...newUser,
      isManager: false,
      password: senha,
      miles: 0,
    }

    clientsMock.registerClient(newClient)
    enviarSenhaPorEmail(newClient.email, senha)
    isAuthenticated.value = true
    user.value = newClient

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
