import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadAllFlightsSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Buscar Voos',
  description: 'Retorna a lista de voos disponíveis com filtros opcionais por data, aeroportos e período.',
  security: [
    {
      bearerAuth: [],
    },
  ],
  querystring: z.object({
    data: z.string().optional().describe('Data inicial para filtrar os voos (formato: YYYY-MM-DD)'),
    'data-fim': z.string().optional().describe('Data final para filtrar os voos (formato: YYYY-MM-DD)'),
    origem: z.string().optional().describe('Código IATA do aeroporto de origem'),
    destino: z.string().optional().describe('Código IATA do aeroporto de destino'),
  }),
  response: {
    [HttpStatusCode.Ok]: z.array(
      z.object({
        codigo: z.string(),
        data: z.string(),
        valor_passagem: z.union([z.number(), z.string()]),
        quantidade_poltronas_total: z.number(),
        quantidade_poltronas_ocupadas: z.number(),
        estado: z.enum(['CONFIRMADO', 'CANCELADO', 'ADIADO']),
        aeroporto_origem: z.object({
          codigo: z.string(),
          nome: z.string(),
          cidade: z.string(),
          uf: z.string(),
        }),
        aeroporto_destino: z.object({
          codigo: z.string(),
          nome: z.string(),
          cidade: z.string(),
          uf: z.string(),
        }),
      }),
    ),
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