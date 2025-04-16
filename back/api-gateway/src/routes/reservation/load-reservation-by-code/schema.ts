import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadReservationByCodeSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Buscar uma reserva específica',
  description: 'Retorna os detalhes de uma reserva específica',
  params: z.object({
    codigoReserva: z.string().describe('Código da reserva'),
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
      voo: z.object({
        codigo: z.string(),
        data: z.string(),
        valor_passagem: z.union([z.number(), z.string()]),
        quantidade_poltronas_total: z.number(),
        quantidade_poltronas_ocupadas: z.number(),
        estado: z.enum(['CONFIRMADO', 'CANCELADO', 'ADIADO', 'REALIZADO']),
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
    }),
    [HttpStatusCode.NoContent]: z.null(),
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