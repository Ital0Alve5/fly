import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'

const registerSchema = toTypedSchema(
  z.object({
    name: z.string({ required_error: 'Nome é obrigatório' }).min(1, 'Campo obrigatório'),
    registerEmail: z
      .string({ required_error: 'E-mail é obrigatório' })
      .email('E-mail inválido')
      .min(1, 'Campo obrigatório'),
    cpf: z.string({ required_error: 'CPF é obrigatório' }).min(1, 'Campo obrigatório'),
    cep: z.string({ required_error: 'CEP é obrigatório' }).min(2, 'Campo obrigatório'),
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

  return {
    handleSubmit,
    name,
    registerEmail,
    cpf,
    cep,
  }
}
