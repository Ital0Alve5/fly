import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadFlyByCodeSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Buscar Voo por Código',
  description: 'Retorna os dados de um voo específico pelo código fornecido',
  params: z.object({
    codigoVoo: z.string(),
  }),
} 
