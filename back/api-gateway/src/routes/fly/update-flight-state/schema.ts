import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateFlightStateSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Alterar o estado do voo',
  description: 'Altera o estado de um voo para CANCELADO ou REALIZADO',
  security: [
    {
      bearerAuth: [],
    },
  ],  
  params: z.object({
    codigoVoo: z.string().describe('Código do voo a ser atualizado'),
  }),
  body: z.object({
    estado: z.enum(['CANCELADO', 'REALIZADO']).describe('Novo estado do voo'),
  }),
} 
