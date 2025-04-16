import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { registerFlySchema } from './schema'
import { employeeAuthMiddleware } from 'src/middlewares/employee-auth'

export async function registerFlyRoute(app: FastifyTypedInstance) {
  const path = '/voos'

  app.post(
    path,
    {
      schema: registerFlySchema,
      preHandler: employeeAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const response = await axios.post(
          `${Env.FLY_SERVICE_URL}/voos`,
          request.body,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        return reply.status(HttpStatusCode.Created).send(response.data)
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