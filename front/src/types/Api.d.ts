// Enums
export enum UserType {
  CLIENTE = 'CLIENTE',
  FUNCIONARIO = 'FUNCIONARIO',
}

export enum ReservationStatus {
  CRIADA = 'CRIADA',
  CHECK_IN = 'CHECK-IN',
  CANCELADA = 'CANCELADA',
  VOO = 'VOO',
  EMBARCADA = 'EMBARCADA',
  REALIZADA = 'REALIZADA',
  NAO_REALIZADA = 'NÃO REALIZADA',
  CANCELADO_VOO = 'CANCELADO VOO',
}

export enum TransactionType {
  ENTRADA = 'ENTRADA',
  SAIDA = 'SAIDA',
}

// Tipos base
export type Address = {
  cep: string
  uf: string
  cidade: string
  bairro: string
  rua: string
  numero: string
  complemento: string
}

export type Client = {
  codigo: number
  cpf: string
  email: string
  nome: string
  telefone: string
}

export type Airport = {
  codigo: string
  nome: string
  cidade: string
  uf: string
}

export type Flight = {
  codigo: string
  data: string
  valor_passagem: number
  quantidade_poltronas_total: number
  quantidade_poltronas_ocupadas: number
  estado: string
  aeroporto_origem: Airport
  aeroporto_destino: Airport
}

// Tipos de resposta por endpoint
export type CadastroResponse = Client

export type LoginResponse = {
  access_token: string
  token_type: 'bearer'
  tipo: UserType
  usuario: Client & {
    senha: string
    saldo_milhas: number
    endereco: Address
  }
}

export type CreateReservationResponse = {
  codigo_cliente: number
  valor: number
  milhas_utilizadas: number
  quantidade_poltronas: number
  codigo_voo: string
  codigo_aeroporto_origem: string
  codigo_aeroporto_destino: string
}

export type Reservation = {
  codigo: string
  data: string
  valor: number
  milhas_utilizadas: number
  quantidade_poltronas: number
  codigo_cliente: number
  estado: ReservationStatus
  voo: Flight
}

export type ReservationListResponse = Reservation[]

export type MilesExtractItem = {
  data: string
  valor_reais: number
  quantidade_milhas: number
  descricao: string
  codigo_reserva: string
  tipo: TransactionType
}

export type MilesExtractResponse = {
  codigo: number
  saldo_milhas: number
  transacoes: MilesExtractItem[]
}

export type MilesSummaryResponse = {
  codigo: number
  saldo_milhas: number
}

export type AirportListResponse = Airport[]

// Tipos genéricos
export type ApiSuccess<T> = {
  error: false
  data: T
}

export type ApiError = {
  error: true
  statusCode: number
  message: string
}

export type ApiResponse<T> = ApiSuccess<T> | ApiError