import { FastifyInstance } from 'fastify'

// Estende o tipo FastifyRequest para incluir a propriedade user
declare module 'fastify' {
  interface FastifyRequest {
    user?: {
      token: string
      type: 'client' | 'employee' | 'user'
      data: any
    }
  }
}

export type FastifyTypedInstance = FastifyInstance 