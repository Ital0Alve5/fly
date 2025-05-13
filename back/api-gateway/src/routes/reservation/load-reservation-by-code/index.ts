import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadReservationByCodeSchema } from './schema'
import { userAuthMiddleware } from 'src/middlewares/user-auth'

export async function loadReservationByCodeRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigoReserva'

  app.get(
    path,
    {
      schema: loadReservationByCodeSchema,
      preHandler: userAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoReserva } = request.params as { codigoReserva: string }

        const reservationResponse = await axios.get(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigoReserva}`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )

        try {
          const { data: flightResponse } = await axios.get(
            `${Env.FLY_SERVICE_URL}/voos/${reservationResponse.data.data.codigo_voo}`,
          )

          const combinedResponse = {
            ...reservationResponse.data.data,
            voo: flightResponse.data,
          }

          return reply.send(combinedResponse)
        } catch (flightError) {
          console.error(`Erro ao buscar dados do voo ${reservationResponse.data.data.codigo_voo}:`, flightError)
          return reply.send(reservationResponse.data.data)
        }
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