import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'

export const fetchAllFlights = async () => {
  try {
    const response = await api.get('/voos')
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar voos:', error)
    throw error
  }
}

export const fetchFilteredFlights = async (origin: string, destination: string) => {
  try {
    const params = new URLSearchParams();
    
    if (origin) params.append('origem', origin.toLowerCase());
    if (destination) params.append('destino', destination.toLowerCase());
    
    console.log(`Fetching: /voos?${params.toString()}`);
    
    const response = await api.get(`/voos?${params.toString()}`);
    
    console.log('Response:', response.data);
    
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar voos filtrados:', error);
    throw error;
  }
};



export const fetchFlightByCode = async (code: string) => {
    try {
        const response = await api.get(`/voo/${code}`)
        const flights = response.data
      
        return flights;  
    } catch (error) {
        console.error('Erro ao buscar voo:', error)
        throw error
    }
}