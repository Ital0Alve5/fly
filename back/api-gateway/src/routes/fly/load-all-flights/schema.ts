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
  querystring: z
    .object({
      data: z.string().optional().describe('Data inicial (YYYY-MM-DD)'),
      'data-fim': z.string().optional().describe('Data final (YYYY-MM-DD)'),
      inicio: z.string().optional().describe('Alias para data inicial'),
      fim: z.string().optional().describe('Alias para data final'),
      origem: z.string().optional().describe('Código IATA de origem'),
      destino: z.string().optional().describe('Código IATA de destino'),
    })
} 
