import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { updateFlightStateSchema } from './schema'
import { employeeAuthMiddleware } from 'src/middlewares/employee-auth'  

export async function updateFlightStateRoute(app: FastifyTypedInstance) {
  const path = '/voos/:codigoVoo/estado'

  app.patch(
    path,
    {
      schema: updateFlightStateSchema,
      preHandler: employeeAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoVoo } = request.params as { codigoVoo: string }
        const { estado } = request.body as { estado: 'CANCELADO' | 'REALIZADO' }

        const response = await axios.patch(
          `${Env.FLY_SERVICE_URL}/voos/${codigoVoo}/estado`,
            { estado },
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        
        return reply.send(response.data)
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