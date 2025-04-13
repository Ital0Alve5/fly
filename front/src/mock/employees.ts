import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import { ref } from 'vue'

type EmployeeData = AuthenticatedUserData & {
  password: string
  cpf: string
  phone: string
}

const registeredEmployees = ref<EmployeeData[]>([
  {
    userId: 11,
    miles: 0,
    name: 'Ítalo Zedelinski',
    email: 'italo@empresa.com',
    isManager: true,
    password: '1234',
    cpf: '123.456.789-10',
    phone: '(81) 99999-9999',
  },
  {
    userId: 12,
    miles: 0,
    name: 'Jully Juju',
    email: 'jully@empresa.com',
    isManager: false,
    password: '1234',
    cpf: '111.222.333-44',
    phone: '(41) 99999-9999',
  },
])

export function getEmployeeByEmail(email: string): EmployeeData | null {
  return registeredEmployees.value.find((employee) => employee.email === email) || null
}

export function registerEmployee(newEmployee: EmployeeData): void {
  if (getEmployeeByEmail(newEmployee.email)) {
    throw new Error('E-mail já cadastrado como funcionário.')
  }
  registeredEmployees.value.push(newEmployee)
}

export default { registeredEmployees, registerEmployee, getEmployeeByEmail }
