import type { Client } from '@/types/Auth/AuthenticatedUserData'
import { ref } from 'vue'
import { getEmployeeByCPF, getEmployeeByEmail } from '@/mock/employees'


const registeredClients = ref<Client[]>([
  {
    codigo: 1,
    nome: 'Heitor Plinta Pagodeiro',
    email: 'heitor@gmail.com',
    cpf: '123.456.789-00',
    saldo_milhas: 0,
    endereco: {
      cep: 'teste',
      uf: 'teste',
      cidade: 'teste',
      bairro: 'teste',
      rua: 'teste',
      numero: 'teste',
      complemento: 'teste',
    },
  },
])

export function getClientByEmail(email: string): Client | null {
  return registeredClients.value.find((client) => client.email === email) || null
}

export function getClientByCPF(cpf: string): Client | null {
  return registeredClients.value.find((client) => client.cpf === cpf) || null
}

export function registerClient(newClient: Client): void {
  if (getEmployeeByEmail(newClient.email) || getClientByEmail(newClient.email)) {
    throw new Error('E-mail já cadastrado.')
  }
  if (getEmployeeByCPF(newClient.cpf) || getClientByCPF(newClient.cpf)) {
    throw new Error('CPF já cadastrado.')
  }
  registeredClients.value.push(newClient)
}

export default { registeredClients, registerClient, getClientByEmail, getClientByCPF }
