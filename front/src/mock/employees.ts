import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'
import { ref } from 'vue'

const registeredEmployees = ref<AuthenticatedUserData[]>([
  {
    name: 'Ítalo Zedelinski',
    email: 'italo.zedelinski@empresa.com',
    isManager: true,
  },
  {
    name: 'Jully Juju',
    email: 'jully.juju@empresa.com',
    isManager: false,
  },
])

export function getEmployeeByEmail(email: string): AuthenticatedUserData | null {
  return registeredEmployees.value.find(employee => employee.email === email) || null
}

export function registerEmployee(newEmployee: AuthenticatedUserData): void {
  if (getEmployeeByEmail(newEmployee.email)) {
    throw new Error('E-mail já cadastrado como funcionário.')
  }
  registeredEmployees.value.push(newEmployee)
}

export default { registeredEmployees, registerEmployee, getEmployeeByEmail }
