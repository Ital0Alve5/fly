import api from '@/lib/axios'

export const MilesService = {
  async getSummary(clientId: number) {
    const response = await api.get(`/clientes/${clientId}/milhas`)
    return response.data
  },

  async addMiles(clientId: number, miles: number) {
    const response = await api.put(`/clientes/${clientId}/milhas`, { quantidade: miles })
    return response.data
  },

  async getExtract(clientId: number) {
    const response = await api.get(`/clientes/${clientId}/milhas/extrato`)
    return response.data
  }
}