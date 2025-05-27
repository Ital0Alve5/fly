import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { isValidCPF } from '@/lib/utils'

const registerSchema = toTypedSchema(
  z.object({
    name: z.string({ required_error: 'Nome é obrigatório' }).min(1, 'Campo obrigatório'),
    registerEmail: z
      .string({ required_error: 'E-mail é obrigatório' })
      .email('E-mail inválido')
      .min(1, 'Campo obrigatório'),
    cpf: z.string().refine((cpf) => isValidCPF(cpf), 'CPF inválido'),
    cep: z.string({ required_error: 'CEP é obrigatório' }).min(1, 'Campo obrigatório'),
    number: z.string({ required_error: 'Número é obrigatório' }).min(1, 'Campo obrigatório'),
  }),
)

export function useRegisterForm() {
  const { handleSubmit } = useForm({
    validationSchema: registerSchema,
  })

  const name = useField<string>('name')
  const registerEmail = useField<string>('registerEmail')
  const cpf = useField<string>('cpf')
  const cep = useField<string>('cep')
  const number = useField<string>('number')

  return {
    handleSubmit,
    name,
    registerEmail,
    cpf,
    cep,
    number,
  }
}