import { FastifySchema } from 'fastify'

export const loadAllEmployeesSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Buscar todos os funcionários',
  description: 'Buscar todos os funcionários',
  security: [
    {
      bearerAuth: [],
    },
  ],
}
