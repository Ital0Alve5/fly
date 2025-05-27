import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const logoutSchema: FastifySchema = {
  tags: ['Autenticação'],
  summary: 'Efetuar o logout',
  description: 'Fazer logout do usuário logado',
  security: [
    {
      bearerAuth: [],
    },
  ],
  body: z.object({
    login: z.string().email('E-mail inválido'),
  }),
}
