import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { createReservationSchema } from './schema'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'

export async function createReservationRoute(app: FastifyTypedInstance) {
  const path = '/reservas'

  app.post(
    path,
    {
      schema: createReservationSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoCliente } = request.body as { codigoCliente: string }

        if (request.user?.data.codigo !== codigoCliente) {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão para acessar este cliente' })
        }

        const reservationResponse = await axios.post(
          `${Env.RESERVATION_SERVICE_URL}/reservas`,
          request.body,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )

        return reply.status(HttpStatusCode.Created).send(reservationResponse.data.data)
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
