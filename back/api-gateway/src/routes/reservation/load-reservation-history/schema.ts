import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadReservationByCodeSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Buscar histórico da reserva',
  description: 'Retorna o histórico de mudanças de estado de uma reserva específica',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigo_reserva: z.string().describe('Código da reserva'),
  }),
} 
