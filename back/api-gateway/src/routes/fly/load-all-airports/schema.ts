import { FastifySchema } from 'fastify'

export const loadAllAirportsSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Buscar Aeroportos',
  description: 'Retorna a lista de todos os aeroportos disponíveis',
  security: [
    {
      bearerAuth: [],
    },
  ],
} 
