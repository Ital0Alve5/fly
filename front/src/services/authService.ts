import api from '@/lib/axios'

export const AuthService = {
  async login(email: string, password: string) {
    const response = await api.post('/auth/login', { email, password })
    return response.data
  },

  async register(clientData: {
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
  }) {
    const response = await api.post('/clientes', clientData)
    return response.data
  }
}