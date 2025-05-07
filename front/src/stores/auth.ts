import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { LoginResponse} from '@/types/Api'
import { AuthService } from '@/services/authService'
import { useToast } from '@/components/ui/toast'
import api from '@/lib/axios'

export const useAuthStore = defineStore('auth', () => {
  const { toast } = useToast()
  const isAuthenticated = useLocalStorage('auth/isAuthenticated', false)
  const user = useLocalStorage<LoginResponse | null>('auth/user', null, {
    serializer: {
      read: (v) => (v ? JSON.parse(v) : null),
      write: (v) => JSON.stringify(v),
    },
  })

  async function login(email: string, password: string): Promise<LoginResponse | null> {
    try {
      const response = await AuthService.login(email, password)
      
      if (!response.error) {
        isAuthenticated.value = true
        user.value = response.data
        return user.value
      } else {
        toast({
          title: 'Erro no login',
          description: response.message,
          variant: 'destructive',
          duration: 2500,
        })
        return null
      }
    } catch (error) {
      const errorMessage = error instanceof Error 
      ? error.message 
      : 'Falha ao tentar fazer login'
      toast({
        title: 'Erro no login',
        description: errorMessage,
        variant: 'destructive',
        duration: 2500,
      })
      return null
    }
  }

  async function register(newUser: {
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
  }): Promise<LoginResponse | null> {
    try {
      const response = await AuthService.register(newUser)
      
      if (!response.error) {
        toast({
          title: 'Cadastro realizado!',
          description: 'Verifique seu e-mail para a senha',
          variant: 'default',
        })
        return response.data
      } else {
        toast({
          title: 'Erro no cadastro',
          description: response.message,
          variant: 'destructive',
          duration: 2500,
        })
        return null
      }
    } catch (error) {
      const errorMessage = error instanceof Error 
      ? error.message 
      : 'Falha ao tentar cadastrar'
      toast({
        title: 'Erro no cadastro',
        description: errorMessage,
        variant: 'destructive',
        duration: 2500,
      })
      return null
    }
  }

  function logout() {
    isAuthenticated.value = false
    user.value = null
    localStorage.removeItem('token')
  }

  async function checkAuth() {
    const token = localStorage.getItem('token')
    if (token) {
      try {
        const response = await api.get('/auth/me')
        if (response.data) {
          isAuthenticated.value = true
          user.value = response.data
        }
      } catch {
        logout()
      }
    }
  }

  return {
    isAuthenticated,
    user,
    login,
    register,
    logout,
    checkAuth,
  }
})