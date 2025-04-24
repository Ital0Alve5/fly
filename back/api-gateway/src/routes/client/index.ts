import { FastifyTypedInstance } from 'src/shared/types'
import { loadClientByCodeRoute } from './load-by-code'
import { updateMilesSumRoute } from './update-miles-sum'
import { loadMilesTransactionsRoute } from './load-miles-transactions'
import { registerClientRoute } from './register-client'

export async function clientRoutes(app: FastifyTypedInstance) {
  loadClientByCodeRoute(app)
  updateMilesSumRoute(app)
  loadMilesTransactionsRoute(app)
  registerClientRoute(app)
}
