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

        console.log('Valida cliente:', request.user?.data.codigoExterno, codigo_cliente)
        console.log('Tipo: ', typeof request.user?.data.codigoExterno, typeof codigo_cliente)
        if (request.user?.data.codigoExterno !== codigo_cliente) {
          return reply
            .status(HttpStatusCode.Forbidden)
            .send({ message: 'Você não tem permissão para acessar este cliente' })
        }

        console.log('Validou cliente:', request.user?.data.codigoExterno, codigo_cliente)
        const reservationResponse = await axios.post(
          `${Env.SAGA_URL}/reservas`,
          request.body,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )

        return reply.status(HttpStatusCode.Created).send(reservationResponse.data.data)
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
