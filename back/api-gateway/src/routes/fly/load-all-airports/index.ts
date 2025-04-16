import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadAllAirportsSchema } from './schema'

export async function loadAllAirportsRoute(app: FastifyTypedInstance) {
  const path = '/aeroportos'

  app.get(
    path,
    {
      schema: loadAllAirportsSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const response = await axios.get(
          `${Env.FLY_SERVICE_URL}/aeroportos`,
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