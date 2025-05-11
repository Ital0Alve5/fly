import api from '@/lib/axios'
import { useAuthStore } from '@/stores/auth'

export const getBookingByUserCode = async () => {
    try {
        const authStore = useAuthStore();
        const code = authStore.user?.usuario.codigo;

        const response = await api.get(`/clientes/${code}/reservas`)
        const bookings = response.data

        console.log('endpoint:', response.data)
        console.log(`/clientes/${code}/reservas`);

        return bookings;  
    } catch (error) {
        console.error('Erro ao buscar reservas:', error)
        throw error
    }
}

export const createNewReservation = async (data: {
    codigo_cliente: number,
    valor: number,
    milhas_utilizadas: number,
    quantidade_poltronas: number,
    codigo_voo: string,
    codigo_aeroporto_origem: string,
    codigo_aeroporto_destino: string
  }) => {
    console.log(data);
    try {
      const response = await api.post('/reservas', data)
      return response.data.codigo
    } catch (error) {
      console.error('Erro ao criar reserva:', error)
      throw error
    }
  }
  