import type { Client } from '@/types/Auth/AuthenticatedUserData'
import type { MilesExtractResponse } from '@/types/Api'
import api from '@/lib/axios'
import { formatarUsuario } from '@/utils/clientFormat'
import { useAuthStore } from '@/stores/auth'
import type { AxiosResponse } from 'axios'
import type { Reserve } from '@/types/Reserve'

export const registerClient = async (client: Client): Promise<AxiosResponse<Client>> => {
  client = formatarUsuario(client)

  return await api.post(`/clientes`, {
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
    },
  })
}

export const getClientReservationList = async (): Promise<Reserve[] | null> => {
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

export const getClientFlightListInNext48hrs = async (): Promise<Reserve[]> => {
  try {
    const response = await getClientReservationList()

    if (!response) return []

    return response.filter((res) => {
      console.log(res)
      const inputDate = new Date(res.voo.data)
      const now = new Date()
      const timeDifference = inputDate.getTime() - now.getTime()
      const hours48 = 48 * 60 * 60 * 1000

      return timeDifference >= 0 && timeDifference <= hours48 && res.estado === "CRIADA"
    })
  } catch (error) {
    console.error('Erro ao pegar lista de reserva do cliente:', error)
    return []
  }
}

export const performCheckin = async (reserve: Reserve): Promise<AxiosResponse<Reserve>> => {
  return await api.patch(`/reservas/${reserve.codigo}/estado`, { estado: 'CHECK-IN' })
}

export const performBoarding = async (reserve: Reserve): Promise<AxiosResponse<Reserve>> => {
  return await api.patch(`/reservas/${reserve.codigo}/estado`, { estado: 'EMBARCADA' })
}

export const addMilesToClient = async (
  milesQuantity: number,
): Promise<{ codigo: number; saldo_milhas: number } | null> => {
  const authStore = useAuthStore()
  const code = authStore.user?.usuario?.codigo
  try {
    const response = await api.put(`/clientes/${code}/milhas`, {
      quantidade: milesQuantity,
    })
    return response.data
  } catch (error) {
    console.error('Erro ao adicionar milhas:', error)
    return null
  }
}

export const getClientTransactionHistory = async (): Promise<MilesExtractResponse | null> => {
  const authStore = useAuthStore()
  const code = authStore.user?.usuario?.codigo
  try {
    const response = await api.get(`/clientes/${code}/milhas`)

    return response.data
  } catch (error) {
    console.error('Erro ao adicionar milhas:', error)
    return null
  }
}

export const getClientData = async (): Promise<{
  codigo: 1010
  cpf: string
  email: string
  nome: string
  saldo_milhas: 0
  endereco: {
    cep: string
    uf: string
    cidade: string
    bairro: string
    rua: string
    numero: number
    complemento: string
  }
} | null> => {
  const authStore = useAuthStore()
  const code = authStore.user?.usuario?.codigo
  try {
    const response = await api.get(`/clientes/${code}`)
    return response.data
  } catch (error) {
    console.error('Erro pegar dados do cliente:', error)
    return null
  }
}
