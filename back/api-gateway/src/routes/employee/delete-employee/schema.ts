import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const deleteEmployeeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Remover um funcionário',
  description: 'Remove um funcionário pelo código fornecido na URL',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoFuncionario: z.string(),
  }),
}
