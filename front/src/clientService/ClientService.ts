import type { Client } from '@/types/Auth/AuthenticatedUserData'
import type { ReservationListResponse } from '@/types/Api'
import api from '@/lib/axios'
import { formatarUsuario } from '@/utils/clientFormat'
import { useAuthStore } from '@/stores/auth'

export const registerClient = async (client: Client): Promise<Client | null> => {
    try {
        client = formatarUsuario(client);

        const response = await api.post(`/clientes`,
            {
                cpf: client.cpf,
                email: client.email,
                nome: client.nome,
                saldo_milhas: client.saldo_milhas,
                endereco: {
                  cep: client.endereco.cep,
                  uf: client.endereco.uf,
                  cidade: client.endereco.cidade,
                  bairro: client.endereco.bairro,
                  rua: client.endereco.rua,
                  numero: client.endereco.numero,
                  complemento: client.endereco.complemento,
                }
            }
        )


        return response.data
    } catch (error) {
        console.error('Erro ao cadastrar cliente:', error)
        return null
    }
}

export const getClientReservationList = async (): Promise<ReservationListResponse | null> => {
    const authStore = useAuthStore()
    const code = authStore.user?.usuario?.codigo
    try {
        const response = await api.get(`/clientes/${code}/reservas`)
        return response.data
    } catch (error) {
        console.error('Erro ao pegar lista de reserva do cliente:', error)
        return null
    }
}