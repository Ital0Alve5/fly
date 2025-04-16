import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const registerFlySchema: FastifySchema = {
  tags: ['Voos'],
  summary: 'Inserir um voo novo',
  description: 'Cria um novo voo com os dados fornecidos',
  body: z.object({
    data: z.string(),
    valor_passagem: z.number().positive('O valor da passagem deve ser positivo'),
    quantidade_poltronas_total: z.number().positive('A quantidade de poltronas deve ser positiva'),
    quantidade_poltronas_ocupadas: z.number().min(0, 'A quantidade de poltronas ocupadas não pode ser negativa'),
    codigo_aeroporto_origem: z.string().min(3, 'O código do aeroporto de origem deve ter pelo menos 3 caracteres'),
    codigo_aeroporto_destino: z.string().min(3, 'O código do aeroporto de destino deve ter pelo menos 3 caracteres'),
  }),
  response: {
    [HttpStatusCode.Created]: z.object({
      codigo: z.string(),
      data: z.string(),
      valor_passagem: z.number(),
      quantidade_poltronas_total: z.number(),
      quantidade_poltronas_ocupadas: z.number(),
      estado: z.enum(['CONFIRMADO', 'CANCELADO', 'ADIADO']),
      codigo_aeroporto_origem: z.string(),
      codigo_aeroporto_destino: z.string(),
    }),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.Forbidden]: z.null(),
    [HttpStatusCode.Conflict]: z.null(),
  },
} 