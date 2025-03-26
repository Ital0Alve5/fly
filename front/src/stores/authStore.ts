import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import clientsMock from '@/mock/clients'
import employeesMock from '@/mock/employees'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const user = ref<AuthenticatedUserData | null>(null)

  async function login(email: string, password: string): Promise<AuthenticatedUserData | null> {
    const validPassword = '1234'

    const client = clientsMock.getClientByEmail(email)
    const employee = employeesMock.getEmployeeByEmail(email)

    if ((client || employee) && password === validPassword) {
      isAuthenticated.value = true
      user.value = client || employee
      return user.value
    }

    return null
  }

  async function register(
    newUser: {
      name: string
      email: string
      cpf: string
      cep: string
      isManager?: boolean
    },
    password: string,
  ): Promise<AuthenticatedUserData | null> {
    if (!newUser.name || !newUser.email || !newUser.cpf || !newUser.cep) {
      throw new Error('Todos os campos são obrigatórios.')
    }

    const newClient = {
      name: newUser.name,
      email: newUser.email,
      cpf: newUser.cpf,
      cep: newUser.cep,
      isManager: false,
    }

    clientsMock.registerClient(newClient)
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
