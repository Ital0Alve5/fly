import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateMilesSumSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Somar a quantidade de milhas',
  description: 'Adiciona milhas ao saldo do cliente',
  params: z.object({
    codigoCliente: z.number(),
  }),
  body: z.object({
    quantidade: z.number().positive('A quantidade deve ser positiva'),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.number(),
      saldo_milhas: z.number(),
    }),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.Forbidden]: z.null(),
    [HttpStatusCode.NotFound]: z.null(),
  },
} 