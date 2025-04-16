import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { updateEmployeeSchema } from './schema'

export async function updateEmployeeRoute(app: FastifyTypedInstance) {
  const path = '/funcionarios/:codigoFuncionario'

  app.put(
    path,
    {
      schema: updateEmployeeSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoFuncionario } = request.params as { codigoFuncionario: string }
        const response = await axios.put(
          `${Env.EMPLOYEE_SERVICE_URL}/funcionarios/${codigoFuncionario}`,
          request.body,
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