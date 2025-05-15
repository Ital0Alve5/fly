export type Client = {
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
    numero: string
    complemento: string
  }
}

export type Employee = {
  codigo: number
  cpf: string
  email: string
  nome: string
  telefone: string
}

export type AuthenticatedUserData = {
  access_token: string
  token_type: string
  tipo: 'CLIENTE' | 'FUNCIONARIO'
  senha: string
  usuario: Client | Employee | null
}
