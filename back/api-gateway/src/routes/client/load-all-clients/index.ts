import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'
import { loadAllClientsSchema } from './schema'

export async function loadAllClientsRoute(app: FastifyTypedInstance) {
  const path = '/clientes'

  app.get(
    path,
    {
      schema: loadAllClientsSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const response = await axios.get(
          `${Env.CLIENT_SERVICE_URL}/clientes`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        return reply.send(response.data.data)
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply
            .status(err.response.status)
            .send({ message: err.response.data.message })
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
} 