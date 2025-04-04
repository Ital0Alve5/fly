export type Reserve = {
  id: number
  status: 'CRIADA' | 'CHECK-IN' | 'CANCELADA' | 'VOO' | 'EMBARCADA' | 'REALIZADA' | 'NÃO REALIZADA'
  dateTimeR: string // yyyy-mm-dd hh:mm
  dateTimeF: string // yyyy-mm-dd hh:mm
  origin: string
  destination: string
  code: string
  price: number
  miles: number
}
