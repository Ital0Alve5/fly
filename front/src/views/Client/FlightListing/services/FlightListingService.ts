import api from '@/lib/axios'

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

    const today = new Date().toISOString().split('T')[0];

    params.append('data', today);

    if (origin) params.append('origem', origin.toUpperCase());
    if (destination) params.append('destino', destination.toUpperCase());

    const response = await api.get(`/voos?${params.toString()}`);

    return response.data.voos;
  } catch (error) {
    console.error('Erro ao buscar voos filtrados:', error);
    throw error;
  }
};



export const fetchFlightByCode = async (code: string) => {
    try {
        const response = await api.get(`/voos/${code}`)
        const flights = response.data

        return flights;
    } catch (error) {
        console.error('Erro ao buscar voo:', error)
        throw error
    }
}
