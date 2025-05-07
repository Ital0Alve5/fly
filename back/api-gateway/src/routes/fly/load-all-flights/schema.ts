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
} 
