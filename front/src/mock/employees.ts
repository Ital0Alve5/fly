import type { Employee } from '@/types/Auth/Employee'
import { ref } from 'vue'

const registeredEmployees = ref<Employee[]>([
  {
    codigo: 11,
    nome: 'Ítalo Zedelinski',
    email: 'italo@empresa.com',
    cpf: '123.456.789-10',
    telefone: '(81) 99999-9999',
  },
  {
    codigo: 12,
    nome: 'Jully Juju',
    email: 'jully@empresa.com',
    cpf: '111.222.333-44',
    telefone: '(41) 99999-9999',
  },
])

export function getEmployeeByEmail(email: string): Employee | null {
  return registeredEmployees.value.find((employee) => employee.email === email) || null
}

export function registerEmployee(newEmployee: Employee): void {
  if (getEmployeeByEmail(newEmployee.email)) {
    throw new Error('E-mail já cadastrado como funcionário.')
  }
  registeredEmployees.value.push(newEmployee)
}

export default { registeredEmployees, registerEmployee, getEmployeeByEmail }
