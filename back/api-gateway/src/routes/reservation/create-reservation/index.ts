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
        const reservationData = request.body as {
          codigo_cliente: number
          valor: number
          milhas_utilizadas: number
          quantidade_poltronas: number
          codigo_voo: string
          codigo_aeroporto_origem: string
          codigo_aeroporto_destino: string
        }

        const reservationResponse = await axios.post(
          `${Env.RESERVATION_SERVICE_URL}/reservas`,
          reservationData,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
 
        try {
          const { data: flightResponse } = await axios.get(
            `${Env.FLY_SERVICE_URL}/voos/${reservationData.codigo_voo}`,
          )

          const combinedResponse = {
            ...reservationResponse.data.data,
            codigo_aeroporto_origem: flightResponse.data.codigo_aeroporto_origem,
            codigo_aeroporto_destino: flightResponse.data.codigo_aeroporto_destino,
          }

          return reply.status(HttpStatusCode.Created).send(combinedResponse)
        } catch (flightError) {
          console.error('Erro ao buscar dados do voo:', flightError)
          return reply.status(HttpStatusCode.Created).send(reservationResponse.data.data)
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