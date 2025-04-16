import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const registerSchema: FastifySchema = {
  tags: ['Autenticação'],
  summary: 'Inserir um cliente não cadastrado',
  description: 'Rota para cadastrar um cliente',
  body: z.object({
    login: z.object({
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
      saldo_milhas: z.number().min(0, 'Saldo de milhas não pode ser negativo'),
      endereco: z.object({
        cep: z.string().length(8, 'CEP deve ter 8 dígitos'),
        uf: z.string().nonempty('UF é obrigatória'),
        cidade: z.string().nonempty('Cidade é obrigatória'),
        bairro: z.string().nonempty('Bairro é obrigatório'),
        rua: z.string().nonempty('Rua é obrigatória'),
        numero: z.string().nonempty('Número é obrigatório'),
        complemento: z.string().nonempty('Complemento é obrigatório'),
      }),
    }),
  }),
  response: {
    [HttpStatusCode.Created]: z.null().describe('Criado'),
    [HttpStatusCode.Conflict]: z.null().describe('Usuário já cadastrado'),
    [HttpStatusCode.BadRequest]: z.null().describe('Campo inválido'),
  },
}
