export type Reserve = {
  id: number
  status: 'CRIADA' | 'CHECK-IN' | 'CANCELADA' | 'VOO' | 'EMBARCADA' | 'REALIZADA' | 'NÃO REALIZADA'
  dataHora: string // yyyy-mm-dd hh:mm
  origem: string
  destino: string
  codigo: string
  valor: string
  milhas: number
}
