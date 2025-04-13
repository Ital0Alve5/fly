import type { Flight } from './Flight'

export type Reserve = {
  codigo: string
  codigo_cliente: number
  estado:
    | 'CRIADA'
    | 'CHECK-IN'
    | 'CANCELADA'
    | 'VOO'
    | 'EMBARCADA'
    | 'REALIZADA'
    | 'NÃO REALIZADA'
    | 'CANCELADO VOO'
  data: string
  valor: number
  milhas_utilizadas: number
  quantidade_poltronas: number
  voo: Flight
}
