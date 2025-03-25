import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import { useRouter } from 'vue-router'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const user = ref<AuthenticatedUserData | null>(null)

  const router = useRouter()

  const login = async (email: string, password: string): Promise<AuthenticatedUserData | null> => {
    const validEmail = 'usuario@exemplo.com'
    const validPassword = '1234'

    if (email === validEmail && password === validPassword) {
      isAuthenticated.value = true
      user.value = {
        name: 'Usuário Exemplo',
        email: email,
        isManager: false,
      }

      router.push('/home')
      return user.value
    } else {
      isAuthenticated.value = false
      user.value = null
      return null
    }
  }

  const register = async (formData: {
    name: string
    email: string
    cpf: string
    cep: string
    street: string
    city: string
    state: string
    number: string
    complement: string
  }): Promise<AuthenticatedUserData | null> => {
    const validEmail = 'usuario@exemplo.com'

    if (formData.email === validEmail) {
      const userData = {
        name: formData.name,
        email: formData.email,
        isManager: false,
      }

      user.value = userData
      isAuthenticated.value = true

      const generatedPassword = Math.floor(Math.random() * 9000) + 1000

      console.log(`Senha gerada: ${generatedPassword}`)

      router.push('/home')
      return userData
    }

    return null
  }

  return { login, register, isAuthenticated, user }
})
