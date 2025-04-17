import { FastifyTypedInstance } from 'src/shared/types'
import { loadFlyByCodeRoute } from './load-fly-by-code'
import { loadAllAirportsRoute } from './load-all-airports'
import { registerFlyRoute } from './register-fly'
import { loadAllFlightsRoute } from './load-all-flights'
import { updateFlightStateRoute } from './update-flight-state'

export async function flyRoutes(app: FastifyTypedInstance) {
  loadFlyByCodeRoute(app)
  loadAllAirportsRoute(app)
  registerFlyRoute(app)
  loadAllFlightsRoute(app)
  updateFlightStateRoute(app)
} 