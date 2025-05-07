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
}
