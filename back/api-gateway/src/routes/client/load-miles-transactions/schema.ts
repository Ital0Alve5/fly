import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadMilesTransactionsSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Buscar o extrato de todas as transações com milhas',
  description: 'Retorna o saldo atual e o histórico de transações com milhas do cliente',
  security: [
    {
      bearerAuth: [],
    },
  ],  
  params: z.object({
    codigoCliente: z.string(),
  }),
} 
