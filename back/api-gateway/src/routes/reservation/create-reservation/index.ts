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
        const { codigo_cliente } = request.body as { codigo_cliente: string }

        if (request.user?.data.codigoExterno !== codigo_cliente) {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão para acessar este cliente' })
        }
 
        const reservationResponse = await axios.post(
          `${Env.SAGA_URL}/reservations`,
          request.body,
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

          return reply.status(HttpStatusCode.Created).send(combinedResponse)
        } catch (flightError) {
          console.error(`Erro ao buscar dados do voo ${reservationResponse.data.data.codigo_voo}:`, flightError)
          return reply.status(HttpStatusCode.BadGateway).send(reservationResponse.data.data)
        }
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply
            .status(err.response.status)
            .send({ message: err.response.data.message, erro: "Saldo de milhas insuficiente" })
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
} 
