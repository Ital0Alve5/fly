import { FastifyTypedInstance } from 'src/shared/types'
import { loginRoute } from './login'
import { logoutRoute } from './logout'
import { registerRoute } from './register'

export async function authRoutes(app: FastifyTypedInstance) {
  loginRoute(app)
  logoutRoute(app)
  registerRoute(app)
}
