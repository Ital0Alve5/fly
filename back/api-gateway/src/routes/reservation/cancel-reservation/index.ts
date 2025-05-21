import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { cancelReservationSchema } from './schema'

export async function cancelReservationRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigo_reserva'

  app.delete(
    path,
    {
      schema: cancelReservationSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigo_reserva } = request.params as { codigo_reserva: string }

        const reservationResponse = await axios.delete(
          `${Env.SAGA_URL}/reservations/cancela/${codigo_reserva}`,
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