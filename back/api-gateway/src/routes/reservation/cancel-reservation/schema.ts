import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const cancelReservationSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Cancelar a reserva',
  description: 'Cancela uma reserva, alterando seu estado para CANCELADA',
  params: z.object({
    codigoReserva: z.string().describe('Código da reserva a ser cancelada'),
  }),
} 
