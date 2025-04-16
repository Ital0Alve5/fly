import { FastifyTypedInstance } from 'src/shared/types'
import { registerEmployeeRoute } from './register'
import { loadAllEmployeesRoute } from './load-all'
import { deleteEmployeeRoute } from './delete'
import { updateEmployeeRoute } from './update'

export async function employeeRoutes(app: FastifyTypedInstance) {
  registerEmployeeRoute(app)
  loadAllEmployeesRoute(app)
  deleteEmployeeRoute(app)
  updateEmployeeRoute(app)
}
