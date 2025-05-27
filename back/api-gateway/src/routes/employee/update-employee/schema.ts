import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateEmployeeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Alterar um funcionário',
  description: 'Alterar os dados de um funcionário existente',
  security: [
    {
      bearerAuth: [],
    },
  ],
  params: z.object({
    codigoFuncionario: z.string(),
  }),
  body: z.object({
    codigo: z.number().optional(),
    cpf: z.string().refine(
      (value) => {
        const digitsRegex = /^\d{11}$/
        return digitsRegex.test(value)
      },
      { message: 'CPF inválido' },
    ),
    email: z.string().email('E-mail inválido'),
    nome: z.string().nonempty('Nome é obrigatório'),
    telefone: z.string().regex(/^\d{11}$/, 'Telefone deve conter 11 dígitos'),
    senha: z.string().min(4, 'A senha deve ter no mínimo 4 caracteres'),
  }),
} 
