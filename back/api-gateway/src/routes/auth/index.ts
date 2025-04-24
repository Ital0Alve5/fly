import { FastifyTypedInstance } from 'src/shared/types'
import { loginRoute } from './login'
import { logoutRoute } from './logout'


export async function authRoutes(app: FastifyTypedInstance) {
  loginRoute(app)
  logoutRoute(app) 
}
