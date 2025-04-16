import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadAllEmployeesSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Buscar todos os funcionários',
  description: 'Buscar todos os funcionários',
  response: {
    [HttpStatusCode.Ok]: z
      .object({
        codigo: z.number(),
        cpf: z.string(),
        email: z.string().email(),
        nome: z.string(),
        telefone: z.string(),
      })
      .describe('Criado'),
    [HttpStatusCode.Conflict]: z.null().describe('Usuário já cadastrado'),
    [HttpStatusCode.Forbidden]: z
      .null()
      .describe('Você não possui permissão para realizar essa ação'),
    [HttpStatusCode.Unauthorized]: z.null().describe('Não autorizado'),
    [HttpStatusCode.BadRequest]: z.null().describe('Campo inválido'),
  },
}
