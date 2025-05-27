import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateMilesSumSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Somar a quantidade de milhas',
  description: 'Adiciona milhas ao saldo do cliente',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoCliente: z.string(),
  }),
  body: z.object({
    quantidade: z.number().positive('A quantidade deve ser positiva'),
  }),
} 
