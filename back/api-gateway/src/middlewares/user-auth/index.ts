import axios, { HttpStatusCode } from 'axios'
import { FastifyRequest, FastifyReply } from 'fastify'
import { Env } from 'src/shared/env'

export async function userAuthMiddleware(
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

    const response = await axios.get(`${Env.AUTH_SERVICE_URL}/usuario`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    request.user = {
      token,
      type: 'user',
      data: response.data.data,
    }

    console.log(request.user?.data)
    return
  } catch (err) {
    if (axios.isAxiosError(err) && err.response) {
      return reply.status(err.response.status).send(err.response.data.data)
    }

    return reply
      .status(HttpStatusCode.Unauthorized)
      .send({ error: 'Token de autenticação inválido' })
  }
}