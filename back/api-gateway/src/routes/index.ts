import { FastifyTypedInstance } from 'src/shared/types'
import { employeeRoutes } from './employee'
import { clientRoutes } from './client'
import { flyRoutes } from './fly'
import { reservationRoutes } from './reservation'

export async function routes(app: FastifyTypedInstance) {
  employeeRoutes(app)
  clientRoutes(app)
  flyRoutes(app)
  reservationRoutes(app)
}
