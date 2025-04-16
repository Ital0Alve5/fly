import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateReservationStateSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Alterar o estado da reserva',
  description: 'Altera o estado de uma reserva para EMBARCADA',
  params: z.object({
    codigoReserva: z.string().describe('Código da reserva a ser atualizada'),
  }),
  body: z.object({
    estado: z.enum(['EMBARCADA']).describe('Novo estado da reserva'),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.string(),
      data: z.string(),
      valor: z.number(),
      milhas_utilizadas: z.number(),
      quantidade_poltronas: z.number(),
      codigo_cliente: z.number(),
      estado: z.enum(['CONFIRMADA', 'CANCELADA', 'PENDENTE', 'EMBARCADA']),
      codigo_voo: z.string(),
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