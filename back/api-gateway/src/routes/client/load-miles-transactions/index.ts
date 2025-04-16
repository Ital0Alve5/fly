import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadMilesTransactionsSchema } from './schema'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'

export async function loadMilesTransactionsRoute(app: FastifyTypedInstance) {
  const path = '/clientes/:codigoCliente/milhas'

  app.get(
    path,
    {
      schema: loadMilesTransactionsSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoCliente } = request.params as { codigoCliente: string }
        const response = await axios.get(
          `${Env.CLIENT_SERVICE_URL}/clientes/${codigoCliente}/milhas`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        return reply.send(response.data)
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply.status(err.response.status).send(err.response.data)
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
} 