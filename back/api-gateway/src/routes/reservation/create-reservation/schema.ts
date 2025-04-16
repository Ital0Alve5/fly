import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const createReservationSchema: FastifySchema = {
  tags: ['Reservas'],
  summary: 'Criar uma reserva',
  description: 'Cria uma nova reserva para um voo',
  body: z.object({
    codigo_cliente: z.number().describe('Código do cliente'),
    valor: z.number().describe('Valor da reserva'),
    milhas_utilizadas: z.number().describe('Quantidade de milhas utilizadas'),
    quantidade_poltronas: z.number().describe('Quantidade de poltronas reservadas'),
    codigo_voo: z.string().describe('Código do voo'),
    codigo_aeroporto_origem: z.string().describe('Código do aeroporto de origem'),
    codigo_aeroporto_destino: z.string().describe('Código do aeroporto de destino'),
  }),
  response: {
    [HttpStatusCode.Created]: z.object({
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
    [HttpStatusCode.Conflict]: z.object({
      error: z.string(),
    }),
    [HttpStatusCode.BadRequest]: z.object({
      error: z.string(),
    }),
  },
} 