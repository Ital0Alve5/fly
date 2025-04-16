import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadReservationByCodeSchema } from './schema'

export async function loadReservationByCodeRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigoReserva'

  app.get(
    path,
    {
      schema: loadReservationByCodeSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoReserva } = request.params as { codigoReserva: string }

        const reservationResponse = await axios.get(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigoReserva}`,
        )

        try {
          const { data: flightResponse } = await axios.get(
            `${Env.FLY_SERVICE_URL}/voos/${reservationResponse.data.codigo_voo}`,
          )

          const combinedResponse = {
            ...reservationResponse.data,
            voo: flightResponse,
          }

          return reply.send(combinedResponse)
        } catch (flightError) {
          console.error(`Erro ao buscar dados do voo ${reservationResponse.data.codigo_voo}:`, flightError)
          return reply.send(reservationResponse.data)
        }
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