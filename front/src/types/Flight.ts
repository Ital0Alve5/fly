import type { Airport } from './Airpoirt'

export type Flight = {
  codigo: string
  data: string
  valor_passagem: number
  quantidade_poltronas_total: number
  quantidade_poltronas_ocupadas: number
  estado: 'CONFIRMADO' | 'CANCELADO' | 'REALIZADO'
  aeroporto_origem: Airport
  aeroporto_destino: Airport
}
