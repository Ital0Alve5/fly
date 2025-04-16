import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadAllFlightsSchema } from './schema'
import { userAuthMiddleware } from 'src/middlewares/user-auth'

export async function loadAllFlightsRoute(app: FastifyTypedInstance) {
  const path = '/voos'

  app.get(
    path,
    {
      schema: loadAllFlightsSchema,
      preHandler: userAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const query = request.query as { 
          data?: string
          'data-fim'?: string
          origem?: string
          destino?: string
        }
        
        const response = await axios.get(`${Env.FLY_SERVICE_URL}/voos`, {
          params: query,
          headers: {
            Authorization: `Bearer ${request.user?.token}`,
          },
        })
        
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