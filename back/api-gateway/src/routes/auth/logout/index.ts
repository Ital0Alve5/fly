import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { logoutSchema } from './schema'

export async function logoutRoute(app: FastifyTypedInstance) {
  const path = '/logout'

  app.post(
    path,
    {
      schema: logoutSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const response = await axios.post(Env.AUTH_SERVICE_URL + path, request.body)
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
