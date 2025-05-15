import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadReservationByCodeSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Buscar uma reserva específica',
  description: 'Retorna os detalhes de uma reserva específica',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigo_reserva: z.string().describe('Código da reserva'),
  }),
} 
