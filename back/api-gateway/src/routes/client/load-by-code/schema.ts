import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadClientByCodeSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Buscar dados de um cliente',
  description: 'Buscar os dados de um cliente pelo código fornecido na URL',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoCliente: z.string(),
  }),
} 
