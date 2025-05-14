import fastifyCors from '@fastify/cors'
import fastify from 'fastify'
import {
  jsonSchemaTransform,
  serializerCompiler,
  validatorCompiler,
  ZodTypeProvider,
} from 'fastify-type-provider-zod'
import { routes } from './routes'
import fastifySwaggerUi from '@fastify/swagger-ui'
import fastifySwagger from '@fastify/swagger'

const app = fastify().withTypeProvider<ZodTypeProvider>()

app.setValidatorCompiler(validatorCompiler)
app.setSerializerCompiler(serializerCompiler)
app.register(fastifyCors, {
  origin: true,
  methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['*'],
  exposedHeaders: ['*'],
  credentials: true,
})

app.register(fastifySwagger, {
  openapi: {
    info: {
      title: 'API Gateway',
      version: '1.0.0',
    },
    components: {
      securitySchemes: {
        bearerAuth: {
          type: 'http',
          scheme: 'bearer',
          bearerFormat: 'JWT',
        },
      },
    },
  },
  transform: jsonSchemaTransform,
})

app.register(fastifySwaggerUi, {
  routePrefix: '/api-docs',
})

app.register(routes)

app.listen({ port: 8000, host: '0.0.0.0' }, function (err, address) {
  if (err) {
    app.log.error(err)
    process.exit(1)
  }

  console.log(`API Gateway started at ${address}`)
})
