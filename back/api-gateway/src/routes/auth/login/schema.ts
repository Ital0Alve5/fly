import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loginSchema: FastifySchema = {
  tags: ['Autenticação'],
  summary: 'Efetuar o login',
  description: 'Fazer login do usuário',
  body: z.object({
    login: z.string().email('E-mail inválido'),
    senha: z.string(),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({ 
      token: z.string(),
    }),
    [HttpStatusCode.Unauthorized]: z.object({
    }),
  },
}
