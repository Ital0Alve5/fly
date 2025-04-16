import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const registerEmployeeSchema: FastifySchema = {
  tags: ['Funcionários'],
  summary: 'Criar um novo funcionário',
  description: 'Criar um novo funcionário',
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
    telefone: z.string().regex(/^\d{10,11}$/, 'Telefone deve conter 10 ou 11 dígitos'),
    senha: z.string().min(4, 'A senha deve ter no mínimo 4 caracteres'),
  }),
  response: {
    [HttpStatusCode.Created]: z
      .object({
        codigo: z.number().describe('1'),
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
