import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loadAllFlightsSchema } from './schema'
import { userAuthMiddleware } from 'src/middlewares/user-auth'

const isoWithOffset03 = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}-03:00$/;

function normalizeDateParam(dateStr?: string): string | undefined {
  if (!dateStr) return undefined;
  if (isoWithOffset03.test(dateStr)) {
    return dateStr.slice(0, 10);
  }
  return dateStr;
}

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
        const raw = request.query as { 
          data?: string
          'data-fim'?: string
          origem?: string
          destino?: string
        }

        const query = {
          ...raw,
          data: normalizeDateParam(raw.data),
          'data-fim': normalizeDateParam(raw['data-fim']),
        }
        
        const response = await axios.get(`${Env.FLY_SERVICE_URL}/voos`, {
          params: query,
          headers: {
            Authorization: `Bearer ${request.user?.token}`,
          },
        })
        
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