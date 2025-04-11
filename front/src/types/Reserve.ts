export type Reserve = {
  id: number
  userId: number
  status:
    | 'CRIADA'
    | 'CHECK-IN'
    | 'CANCELADA'
    | 'VOO'
    | 'EMBARCADA'
    | 'REALIZADA'
    | 'NÃO REALIZADA'
    | 'CANCELADO VOO'
  dateTimeR: string // yyyy-mm-dd hh:mm
  dateTimeF: string // yyyy-mm-dd hh:mm
  origin: string
  destination: string
  flightCode: string
  reservationCode: string
  price: number
  miles: number
}
