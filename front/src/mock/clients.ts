import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import { ref } from 'vue'

type ClientData = AuthenticatedUserData & { cpf: string; cep: string }

const registeredClients = ref<ClientData[]>([
  {
    name: 'Heitor Plinta Pagodeiro',
    email: 'plinta.pagode@example.com',
    cpf: '123.456.789-00',
    cep: '01001-000',
    isManager: false,
  },
  {
    name: 'João Pedro',
    email: 'joao.pedro@example.com',
    cpf: '987.654.321-00',
    cep: '22041-001',
    isManager: false,
  },
])

export function getClientByEmail(email: string): ClientData | null {
  return registeredClients.value.find((client) => client.email === email) || null
}

export function registerClient(newClient: ClientData): void {
  if (getClientByEmail(newClient.email)) {
    throw new Error('E-mail já cadastrado como cliente.')
  }
  registeredClients.value.push(newClient)
}

export default { registeredClients, registerClient, getClientByEmail }
