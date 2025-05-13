import axios, { HttpStatusCode } from 'axios'
import { FastifyTypedInstance } from 'src/shared/types'
import { FastifyReply, FastifyRequest } from 'fastify'
import { Env } from 'src/shared/env'
import { loginSchema } from './schema'

export async function loginRoute(app: FastifyTypedInstance) {
  const path = '/login'

  app.post(
    path,
    {
      schema: loginSchema,
    },
    async (request: FastifyRequest, reply: FastifyReply) => {
      try {
        const authResponse= await axios.post(Env.AUTH_SERVICE_URL + path, request.body)
	const authResponseData = authResponse.data.data
	
	let usuario
	if(authResponseData.tipo === "CLIENTE"){
	  const clientData = await axios.get(`${Env.CLIENT_SERVICE_URL}/clientes/${authResponseData.codigoExterno}`)

	  usuario = clientData.data.data
	} else {
	  const employeeData = await axios.get(`${Env.EMPLOYEE_SERVICE_URL}/funcionarios/${authResponseData.codigoExterno}`)

	  usuario = employeeData.data.data
	}

        return reply.send({
	    access_token: authResponseData.access_token,
	    token_type: authResponseData.token_type,
	    tipo: authResponseData.tipo,
	    usuario: {...usuario, senha: ""}
	  })
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          return reply.status(err.response.status).send(err.response.data.data)
        }

        return reply
          .status(HttpStatusCode.BadGateway)
          .send({ error: 'Erro ao encaminhar a requisição para o upstream' })
      }
    },
  )
}
