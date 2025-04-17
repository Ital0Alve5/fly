import { FastifyTypedInstance } from 'src/shared/types'
import { createReservationRoute } from './create-reservation'
import { loadReservationsByClientRoute } from './load-reservations-by-client'
import { loadReservationByCodeRoute } from './load-reservation-by-code'
import { cancelReservationRoute } from './cancel-reservation'
import { updateReservationStateRoute } from './update-reservation-state'

export async function reservationRoutes(app: FastifyTypedInstance) {
  createReservationRoute(app)
  loadReservationsByClientRoute(app)
  loadReservationByCodeRoute(app)
  cancelReservationRoute(app)
  updateReservationStateRoute(app)
} 