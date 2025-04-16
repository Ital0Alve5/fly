import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadAllAirportsSchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Buscar Aeroportos',
  description: 'Retorna a lista de todos os aeroportos disponíveis',
  response: {
    [HttpStatusCode.Ok]: z.array(
      z.object({
        codigo: z.string(),
        nome: z.string(),
        cidade: z.string(),
        uf: z.string(),
      }),
    ),
    [HttpStatusCode.NoContent]: z.null(),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.Forbidden]: z.null(),
  },
} 