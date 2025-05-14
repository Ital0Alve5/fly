import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const hasAuth = localStorage.getItem('auth/user') ?? '{}'

  if (hasAuth === '{}') return config

  const userAuth: AuthenticatedUserData = JSON.parse(hasAuth)

  if (userAuth.access_token) {
    config.headers.Authorization = `${userAuth.token_type} ${userAuth.access_token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    if (status === 401 || status === 403) {
      localStorage.removeItem('auth/user')
      localStorage.removeItem("isAuthenticated")
      
      window.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

export default api
