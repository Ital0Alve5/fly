import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'

export const cancelFlight = async (code: string): Promise<boolean> => {
    try {
      const response = await api.put(`/voos/${code}/estado`, {
        estado: 'CANCELADO'
      })
  
      return response.status === 200
    } catch (error) {
      console.error('Erro ao cancelar o voo:', error)
      return false
    }
  }
  

export const performFlight = async (code: string): Promise<boolean> => {
    try {
        const response = await api.put(`/voos/${code}/estado`,
            {
            estado: 'REALIZADO'
            }
        )

        return response.status === 200
    } catch (error) {
        console.error('Erro ao realizar o voo:', error)
        return false
    }
}

export const fetchFlightsNext48Hours = async (): Promise<Flight[]> => {
    try {
      const today = new Date()
      const endDate = new Date(today)
      endDate.setDate(today.getDate() + 2)

      // DESATIVE OS COMENTARIOS ABAIXO PARA PEGAR TODOS VOOS PARA TESTES
      // today.setDate(today.getDate() - 1000)
      // endDate.setDate(today.getDate() + 1000)
      
      const formatDate = (date: Date) => date.toISOString().split('T')[0]
      const startDate = formatDate(today)
      const formattedEndDate = formatDate(endDate)
  
      const response = await api.get(
           `/voos?data=${encodeURIComponent(startDate)}&data-fim=${encodeURIComponent(formattedEndDate)}`
         );
      
      return response.data || []
    } catch (error) {
      console.error('Erro ao buscar voos das próximas 48 horas:', error)
      return []
    }
  }