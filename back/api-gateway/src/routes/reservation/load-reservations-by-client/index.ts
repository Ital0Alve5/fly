import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadReservationsByClientSchema } from './schema'
import { clientAuthMiddleware } from 'src/middlewares/client-auth'

export async function loadReservationsByClientRoute(app: FastifyTypedInstance) {
  const path = '/clientes/:codigoCliente/reservas'

  app.get(
    path,
    {
      schema: loadReservationsByClientSchema,
      preHandler: clientAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoCliente } = request.params as { codigoCliente: string }

        const reservationsResponse = await axios.get(
          `${Env.RESERVATION_SERVICE_URL}/clientes/${codigoCliente}/reservas`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )

        const enrichedReservations = await Promise.all(
          reservationsResponse.data.map(async (reservation: any) => {
            try {
              const { data: flightResponse } = await axios.get(
                `${Env.FLY_SERVICE_URL}/voos/${reservation.codigo_voo}`,
              )

              return {
                ...reservation,
                voo: flightResponse,
              }
            } catch (flightError) {
              console.error(`Erro ao buscar dados do voo ${reservation.codigo_voo}:`, flightError)
              return reservation
            }
          }),
        )

        return reply.send(enrichedReservations)
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