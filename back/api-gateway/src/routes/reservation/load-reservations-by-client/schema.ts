import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadReservationsByClientSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Lista de Reservas',
  description: 'Retorna a lista de reservas de um cliente',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoCliente: z.string().describe('Código do cliente'),
  }),
} 
