import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { updateReservationStateSchema } from './schema'
import { userAuthMiddleware } from 'src/middlewares/user-auth'

export async function updateReservationStateRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigo_reserva/estado'

  app.patch(
    path,
    {
      schema: updateReservationStateSchema,
      preHandler: userAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigo_reserva } = request.params as { codigo_reserva: string }
        const { estado } = request.body as { estado: string }

        if (estado !== 'CHECK-IN' && request.user?.data.role !== 'FUNCIONARIO') {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão realizar essa ação' })
        }

        if (estado === 'CHECK-IN' && request.user?.data.role !== 'CLIENTE') {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão realizar essa ação' })
        }

        const reservationResponse = await axios.patch(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigo_reserva}/estado`,
          { estado },
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )

        return reply.status(reservationResponse.status).send(reservationResponse.data.data)
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
