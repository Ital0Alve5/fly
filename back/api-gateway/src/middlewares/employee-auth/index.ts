import axios, { HttpStatusCode } from 'axios'
import { FastifyRequest, FastifyReply } from 'fastify'
import { Env } from 'src/shared/env'

export async function employeeAuthMiddleware(
  request: FastifyRequest,
  reply: FastifyReply,
) {
  try {
    const authHeader = request.headers.authorization

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return reply
        .status(HttpStatusCode.Unauthorized)
        .send({ error: 'Token de autenticação não fornecido' })
    }

    const token = authHeader.split(' ')[1]

    const response = await axios.get(`${Env.AUTH_SERVICE_URL}/funcionario`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    request.user = {
      token,
      type: 'employee',
      data: response.data,
    }

    return
  } catch (err) {
    if (axios.isAxiosError(err) && err.response) {
      return reply.status(err.response.status).send(err.response.data)
    }

    return reply
      .status(HttpStatusCode.Unauthorized)
      .send({ error: 'Token de autenticação inválido' })
  }
}