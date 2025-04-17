import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { cancelReservationSchema } from './schema'

export async function cancelReservationRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigoReserva'

  app.delete(
    path,
    {
      schema: cancelReservationSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoReserva } = request.params as { codigoReserva: string }

        const reservationResponse = await axios.delete(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigoReserva}`,
        )

        try {
          const { data: flightResponse } = await axios.get(
            `${Env.FLY_SERVICE_URL}/voos/${reservationResponse.data.codigo_voo}`,
          )

          const combinedResponse = {
            ...reservationResponse.data,
            codigo_aeroporto_origem: flightResponse.codigo_aeroporto_origem,
            codigo_aeroporto_destino: flightResponse.codigo_aeroporto_destino,
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