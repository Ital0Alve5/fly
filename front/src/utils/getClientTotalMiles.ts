import { getClientData } from '@/clientService/ClientService'

export const getClientMiles = async (): Promise<number | undefined> => {
    try {
        const response = await getClientData()

        return response?.saldoMilhas;
    } catch (error) {
        console.error('Erro pegar milhas do cliente:', error)
        return -1
    }
}