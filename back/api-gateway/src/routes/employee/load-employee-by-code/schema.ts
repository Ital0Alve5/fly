import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadEmployeeByCodeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Buscar dados de um funcionário',
  description: 'Buscar os dados de um funcionário pelo código fornecido na URL',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoFuncionario: z.string(),
  }),
} 
