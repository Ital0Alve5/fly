import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { registerEmployeeSchema } from './schema'
import { employeeAuthMiddleware } from 'src/middlewares/employee-auth'

export async function registerEmployeeRoute(app: FastifyTypedInstance) {
  const path = '/funcionarios'

  app.post(
    path,
    {
      schema: registerEmployeeSchema,
      preHandler: employeeAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const response = await axios.post(Env.SAGA_URL + path, request.body, {
          headers: {
            Authorization: `Bearer ${request.user?.token}`,
          },
        })
        return reply.status(response.status).send(response.data.data)
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
