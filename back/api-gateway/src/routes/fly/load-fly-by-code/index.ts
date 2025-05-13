import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadFlyByCodeSchema } from './schema'

export async function loadFlyByCodeRoute(app: FastifyTypedInstance) {
  const path = '/voo/:codigoVoo'

  app.get(
    path,
    {
      schema: loadFlyByCodeSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoVoo } = request.params as { codigoVoo: string }
        const response = await axios.get(
          `${Env.FLY_SERVICE_URL}/voo/${codigoVoo}`,
        )
        return reply.send(response.data.data)
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