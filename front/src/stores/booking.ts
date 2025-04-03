import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import type { Reserve } from '@/types/Reserve'
import bookingData from '@/mock/booking'

export const useReserveStore = defineStore('booking', () => {
  // 1. Persiste TODAS as reservas (mock + adicionadas)
  const allReservations = useLocalStorage<Reserve[]>(
    'all-reservations',
    // Inicializa com os dados do mock na primeira carga
    [...bookingData],
    {
      serializer: {
        read: (v) => v ? JSON.parse(v) : [...bookingData],
        write: (v) => JSON.stringify(v),
      }
    }
  )

  // 2. Método para adicionar novas reservas
  const addReservation = (reservation: Omit<Reserve, 'id'>) => {
    const newId = allReservations.value.length > 0 
      ? Math.max(...allReservations.value.map(r => r.id)) + 1 
      : 1
    
    allReservations.value.push({
      id: newId,
      ...reservation
    })
  }

  // 3. Método para cancelar reservas
  const cancelReservation = (id: number) => {
    const index = allReservations.value.findIndex(r => r.id === id)
    if (index !== -1) {
      allReservations.value[index] = { 
        ...allReservations.value[index], 
        status: 'CANCELADA' 
      }
    }
  }

  // 4. Método para sincronizar com o mock (opcional)
  const syncWithMock = () => {
    const mockIds = bookingData.map(r => r.id)
    
    // Mantém apenas reservas que não existem no mock
    const userAdded = allReservations.value.filter(r => !mockIds.includes(r.id))
    
    // Combina mock atualizado + reservas adicionadas pelo usuário
    allReservations.value = [...bookingData, ...userAdded]
  }

  return {
    reserves: allReservations,
    addReservation,
    cancelReservation,
    syncWithMock
  }
})