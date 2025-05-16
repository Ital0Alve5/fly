import { FastifyTypedInstance } from 'src/shared/types'
import { registerEmployeeRoute } from './register-employee'
import { loadAllEmployeesRoute } from './load-all-employees'
import { deleteEmployeeRoute } from './delete-employee'
import { updateEmployeeRoute } from './update-employee'
import { loadEmployeeByCodeRoute } from './load-employee-by-code'

export async function employeeRoutes(app: FastifyTypedInstance) {
  registerEmployeeRoute(app)
  loadAllEmployeesRoute(app)
  deleteEmployeeRoute(app)
  updateEmployeeRoute(app)
  loadEmployeeByCodeRoute(app)
}
