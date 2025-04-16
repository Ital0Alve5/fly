import { HttpStatusCode } from 'axios'
import { FastifySchema } from 'fastify'
import { z } from 'zod'

export const loadClientByCodeSchema: FastifySchema = {
  tags: ['Clientes'],
  summary: 'Buscar dados de um cliente',
  description: 'Buscar os dados de um cliente pelo código fornecido na URL',
  params: z.object({
    codigoCliente: z.number(),
  }),
  response: {
    [HttpStatusCode.Ok]: z.object({
      codigo: z.number(),
      cpf: z.string(),
      email: z.string().email(),
      nome: z.string(),
      saldoMilhas: z.number(),
      endereco: z.object({
        cep: z.string(),
        uf: z.string(),
        cidade: z.string(),
        bairro: z.string(),
        rua: z.string(),
        numero: z.string(),
        complemento: z.string(),
      }),
    }),
    [HttpStatusCode.Unauthorized]: z.null(),
    [HttpStatusCode.NotFound]: z.null(),
  },
} 