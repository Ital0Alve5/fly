import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { historyReservationStateSchema } from './schema'
import { userAuthMiddleware } from 'src/middlewares/user-auth'

export async function loadReservationHistory(app: FastifyTypedInstance) {
  const path = '/reservas/:codigo_reserva/historico'

  app.get(
    path,
    {
      schema: historyReservationStateSchema,
      preHandler: userAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigo_reserva } = request.params as { codigo_reserva: string }


        const reservationResponse = await axios.get(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigo_reserva}/historico`,
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
