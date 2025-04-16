import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadMilesTransactionsSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Buscar o extrato de todas as transações com milhas',
  description: 'Retorna o saldo atual e o histórico de transações com milhas do cliente',
  params: z.object({
    codigoCliente: z.number(),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.number(),
      saldo_milhas: z.number(),
      transacoes: z.array(
        z.object({
          data: z.string(),
          valor_reais: z.number(),
          quantidade_milhas: z.number(),
          descricao: z.string(),
          codigo_reserva: z.string(),
          tipo: z.enum(['ENTRADA', 'SAIDA']),
        }),
      ),
    }),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.Forbidden]: z.null(),
    [HttpStatusCode.NotFound]: z.null(),
  },
} 