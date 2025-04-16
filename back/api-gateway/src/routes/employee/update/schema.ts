import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const updateEmployeeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Alterar um funcionário',
  description: 'Alterar os dados de um funcionário existente',
  params: z.object({
    codigoFuncionario: z.number(),
  }),
  body: z.object({
    codigo: z.number(),
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
    telefone: z.string().regex(/^\d{10,11}$/, 'Telefone deve conter 10 ou 11 dígitos'),
    senha: z.string().min(4, 'A senha deve ter no mínimo 4 caracteres'),
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