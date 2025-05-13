import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const registerEmployeeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Criar um novo funcionário',
  description: 'Criar um novo funcionário',
  security: [
    {
      bearerAuth: [],
    },
  ],
  body: z.object({
    cpf: z.string().refine(
      (value) => {
        const digitsRegex = /^\d{11}$/
        const maskedRegex = /^\d{3}\.\d{3}\.\d{3}\-\d{2}$/
        return digitsRegex.test(value) || maskedRegex.test(value)
      },
      { message: 'CPF inválido' },
    ),
    email: z.string().email('E-mail inválido'),
    nome: z.string().nonempty('Nome é obrigatório'),
    telefone: z.string().regex(/^\d{11}$/, 'Telefone deve conter 11 dígitos'),
    senha: z.string().min(4, 'A senha deve ter no mínimo 4 caracteres'),
  }),
}
