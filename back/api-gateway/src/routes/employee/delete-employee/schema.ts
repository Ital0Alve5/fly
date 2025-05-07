import { HttpStatusCode } from 'axios'
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
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.number(),
      cpf: z.string(),
      email: z.string().email(),
      nome: z.string(),
      telefone: z.string(),
    }),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.Forbidden]: z.null(),
    [HttpStatusCode.NotFound]: z.null(),
  },
}
