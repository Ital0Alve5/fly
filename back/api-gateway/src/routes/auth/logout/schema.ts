import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const logoutSchema: FastifySchema = {
  tags: ['Autenticação'],
  summary: 'Efetuar o logout',
  description: 'Fazer logout do usuário logado',
  body: z.object({
    login: z.string().email('E-mail inválido'),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      login: z.string().email(),
    }),
    [HttpStatusCode.Unauthorized]: z.null().describe('Não autorizado'),
  },
}
