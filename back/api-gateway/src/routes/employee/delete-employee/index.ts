import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { deleteEmployeeSchema } from './schema'
import { employeeAuthMiddleware } from 'src/middlewares/employee-auth'  

export async function deleteEmployeeRoute(app: FastifyTypedInstance) {
  const path = '/funcionarios/:codigoFuncionario'
  app.delete(
    path,
    {
      schema: deleteEmployeeSchema,
      preHandler: employeeAuthMiddleware,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const { codigoFuncionario } = request.params as { codigoFuncionario: string }
        const response = await axios.delete(
          `${Env.SAGA_URL}/funcionarios/${codigoFuncionario}`,
          {
            headers: {
              Authorization: `Bearer ${request.user?.token}`,
            },
          },
        )
        return reply.send(response.data.data)
      } catch (err: any) {
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
