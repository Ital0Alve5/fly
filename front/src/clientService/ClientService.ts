import type { Client } from '@/types/Auth/AuthenticatedUserData'
import type { MilesExtractResponse } from '@/types/Api'
import type { Reserve } from '@/types/Reserve'
import api from '@/lib/axios'
import { formatarUsuario } from '@/utils/clientFormat'
import { useAuthStore } from '@/stores/auth'
import type { AxiosResponse } from 'axios'

const BASE_PATH = '/clientes'

function getClientCode(): number {
  const code = useAuthStore().user?.usuario?.codigo
  if (!code) {
    throw new Error('Cliente não autenticado: código inválido')
  }
  return code
}

export async function registerClient(client: Client): Promise<AxiosResponse<Client>> {
  const payload = formatarUsuario(client)
  return api.post<Client>(`${BASE_PATH}`, {
    cpf: payload.cpf,
    email: payload.email,
    nome: payload.nome,
    saldo_milhas: payload.saldo_milhas,
    endereco: {
      cep: payload.endereco.cep,
      uf: payload.endereco.uf,
      cidade: payload.endereco.cidade,
      bairro: payload.endereco.bairro,
      rua: payload.endereco.rua,
      numero: payload.endereco.numero,
      complemento: payload.endereco.complemento ?? '',
    },
  })
}

export async function getClientReservationList(): Promise<Reserve[]> {
  try {
    const code = getClientCode()
    const response = await api.get<Reserve[]>(`${BASE_PATH}/${code}/reservas`)
    return response.data
  } catch (error) {
    console.error('[Service] Erro ao obter reservas do cliente:', error)
    return []
  }
}

function isFlightInNext48hrs(reserve: Reserve): boolean {
  const now = Date.now()
  const flightTime = new Date(reserve.voo.data).getTime()
  const diff = flightTime - now
  const ms48h = 48 * 60 * 60 * 1000
  return diff >= 0 && diff <= ms48h && reserve.estado === 'CRIADA'
}

export async function getClientFlightListInNext48hrs(): Promise<Reserve[]> {
  try {
    const reservas = await getClientReservationList()
    return reservas.filter(isFlightInNext48hrs)
  } catch (error) {
    console.error('[Service] Erro ao filtrar voos 48h:', error)
    return []
  }
}

export async function performCheckin(reserve: Reserve): Promise<AxiosResponse<Reserve>> {
  return api.patch<Reserve>(`/reservas/${reserve.codigo}/estado`, { estado: 'CHECK-IN' })
}

export async function performBoarding(reserve: Reserve): Promise<AxiosResponse<Reserve>> {
  return api.patch<Reserve>(`/reservas/${reserve.codigo}/estado`, { estado: 'EMBARCADA' })
}

export async function addMilesToClient(
  milesQuantity: number,
): Promise<{ codigo: number; saldo_milhas: number }> {
  try {
    const code = getClientCode()
    const response = await api.put<{ codigo: number; saldo_milhas: number }>(
      `${BASE_PATH}/${code}/milhas`,
      { quantidade: milesQuantity },
    )
    return response.data
  } catch (error) {
    console.error('[Service] Erro ao adicionar milhas:', error)
    throw error
  }
}

export async function getClientTransactionHistory(): Promise<MilesExtractResponse> {
  try {
    const code = getClientCode()
    const response = await api.get<MilesExtractResponse>(`${BASE_PATH}/${code}/milhas`)
    return response.data
  } catch (error) {
    console.error('[Service] Erro ao obter histórico de milhas:', error)
    throw error
  }
}

export interface ClientData {
  codigo: number
  cpf: string
  email: string
  nome: string
  saldo_milhas: number
  endereco: {
    cep: string
    uf: string
    cidade: string
    bairro: string
    rua: string
    numero: number
    complemento: string
  }
}

export async function getClientData(): Promise<ClientData> {
  try {
    const code = getClientCode()
    const response = await api.get<ClientData>(`${BASE_PATH}/${code}`)
    return response.data
  } catch (error) {
    console.error('[Service] Erro ao obter dados do cliente:', error)
    throw error
  }
}
