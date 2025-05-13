import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { updateMilesSumSchema } from './schema'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'

export async function updateMilesSumRoute(app: FastifyTypedInstance) {
  const path = '/clientes/:codigoCliente/milhas'

  app.put(
    path,
    {
      schema: updateMilesSumSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoCliente } = request.params as { codigoCliente: string }

        if (request.user?.data.codigo !== codigoCliente) {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão para acessar este cliente' })
        }

        const response = await axios.put(
          `${Env.CLIENT_SERVICE_URL}/clientes/${codigoCliente}/milhas`,
          request.body,
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