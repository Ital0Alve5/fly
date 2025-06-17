import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'

const loginSchema = toTypedSchema(
  z.object({
    email: z
      .string({ required_error: 'E-mail é obrigatório' })
      .min(1, 'Campo obrigatório')
      .email('E-mail inválido'),
    password: z
      .string({ required_error: 'Senha é obrigatória' })
      .min(1, 'Campo obrigatório')
      .length(4, 'Senha deve ter 4 dígitos')
  }),
)

export function useLoginForm() {
  const { handleSubmit } = useForm({
    validationSchema: loginSchema,
  })

  const email = useField<string>('email')
  const password = useField<string>('password')

  return {
    handleSubmit,
    email,
    password,
  }
}
