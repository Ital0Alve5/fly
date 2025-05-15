import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateReservationStateSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Alterar o estado da reserva',
  description: 'Altera o estado de uma reserva para EMBARCADA',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigo_reserva: z.string().describe('Código da reserva a ser atualizada'),
  }),
  body: z.object({
    estado: z.enum(['CHECK-IN', 'EMBARCADA', 'CANCELADA']).describe('Novo estado da reserva'),
  }),
} 
