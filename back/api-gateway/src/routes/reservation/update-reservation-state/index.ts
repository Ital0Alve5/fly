import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { updateReservationStateSchema } from './schema'
import { employeeAuthMiddleware } from 'src/middlewares/employee-auth'

export async function updateReservationStateRoute(app: FastifyTypedInstance) {
  const path = '/reservas/:codigoReserva/estado'

  app.patch(
    path,
    {
      schema: updateReservationStateSchema,
      preHandler: employeeAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoReserva } = request.params as { codigoReserva: string }
        const { estado } = request.body as { estado: 'EMBARCADA' }

        const reservationResponse = await axios.patch(
          `${Env.RESERVATION_SERVICE_URL}/reservas/${codigoReserva}/estado`,
          { estado },
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
            codigo_aeroporto_origem: flightResponse.data.codigo_aeroporto_origem,
            codigo_aeroporto_destino: flightResponse.data.codigo_aeroporto_destino,
          }

          return reply.send(combinedResponse)
        } catch (flightError) {
          console.error(`Erro ao buscar dados do voo ${reservationResponse.data.data.codigo_voo}:`, flightError)
          return reply.send(reservationResponse.data.data)
        }
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