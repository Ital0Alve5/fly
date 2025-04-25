import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadClientByCodeSchema } from './schema'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'

export async function loadClientByCodeRoute(app: FastifyTypedInstance) {
  const path = '/clientes/:codigoCliente'

  app.get(
    path,
    {
      schema: loadClientByCodeSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoCliente } = request.params as { codigoCliente: string }
        const response = await axios.get(
          `${Env.CLIENT_SERVICE_URL}/clientes/${codigoCliente}`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        return reply.send(response.data.data)
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply.status(err.response.status).send(err.response.data.data)
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
} 