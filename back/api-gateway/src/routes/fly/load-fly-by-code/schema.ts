import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadFlyByCodeSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Buscar Voo por Código',
  description: 'Retorna os dados de um voo específico pelo código fornecido',
  params: z.object({
    codigoVoo: z.string(),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.string(),
      data: z.string(),
      valor_passagem: z.number(),
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
    [HttpStatusCode.NotFound]: z.null(),
  },
} 