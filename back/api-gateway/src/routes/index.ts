import { FastifyTypedInstance } from 'src/shared/types'
import { employeeRoutes } from './employee'
import { clientRoutes } from './client'
import { flyRoutes } from './fly'
import { reservationRoutes } from './reservation'
import { authRoutes } from './auth'

export async function routes(app: FastifyTypedInstance) {
  authRoutes(app)
  clientRoutes(app)
  flyRoutes(app)
  reservationRoutes(app)
  employeeRoutes(app)
}
