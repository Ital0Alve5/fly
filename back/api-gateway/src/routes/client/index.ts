import { FastifyTypedInstance } from 'src/shared/types'
import { loadClientByCodeRoute } from './load-by-code'
import { updateMilesSumRoute } from './update-miles-sum'
import { loadMilesTransactionsRoute } from './load-miles-transactions'

export async function clientRoutes(app: FastifyTypedInstance) {
  loadClientByCodeRoute(app)
  updateMilesSumRoute(app)
  loadMilesTransactionsRoute(app)
}
