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
    codigoReserva: z.string().describe('Código da reserva a ser atualizada'),
  }),
  body: z.object({
    estado: z.enum(['EMBARCADA']).describe('Novo estado da reserva'),
  }),
} 
