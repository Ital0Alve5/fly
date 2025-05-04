import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { registerSchema } from './schema'

export async function registerClientRoute(app: FastifyTypedInstance) {
  const path = '/clientes'

  app.post(
    path,
    {
      schema: registerSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { saldo_milhas, ...rest } = request.body as {
          saldo_milhas: number
          [key: string]: any
        }

        const payload = {
          ...rest,
          saldoMilhas: saldo_milhas,
        }

        const response = await axios.post(
          `${Env.SAGA_URL}${path}`,
          payload,
        )

        return reply.send(response.data.data)
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply
            .status(err.response.status)
            .send(err.response.data.data)
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
}
