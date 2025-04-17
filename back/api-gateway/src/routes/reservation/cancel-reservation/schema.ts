import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const cancelReservationSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Cancelar a reserva',
  description: 'Cancela uma reserva, alterando seu estado para CANCELADA',
  params: z.object({
    codigoReserva: z.string().describe('Código da reserva a ser cancelada'),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.string(),
      data: z.string(),
      valor: z.number(),
      milhas_utilizadas: z.number(),
      quantidade_poltronas: z.number(),
      codigo_cliente: z.number(),
      estado: z.enum(['CONFIRMADA', 'CANCELADA', 'PENDENTE']),
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