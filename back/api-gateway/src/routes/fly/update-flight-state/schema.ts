import { HttpStatusCode } from 'axios'
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
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.string(),
      data: z.string(),
      valor_passagem: z.union([z.number(), z.string()]),
      quantidade_poltronas_total: z.number(),
      quantidade_poltronas_ocupadas: z.number(),
      estado: z.enum(['CONFIRMADO', 'CANCELADO', 'ADIADO', 'REALIZADO']),
      codigo_aeroporto_origem: z.string(),
      codigo_aeroporto_destino: z.string(),
    }),
    [HttpStatusCode.Unauthorized]: z.object({
      error: z.string(),
    }),
    [HttpStatusCode.Forbidden]: z.object({
      error: z.string(),
    }),
    [HttpStatusCode.NotFound]: z.object({
      error: z.string(),
    }),
  },
} 