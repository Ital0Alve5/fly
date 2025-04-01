import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'

const checkoutSchema = toTypedSchema(
  z.object({
    cardNumber: z
      .string({ required_error: 'Número do cartão é obrigatório' })
      .min(1, 'Número do cartão é obrigatório')
      .min(19, 'Número de cartão deve ter 16 dígitos')
      .max(19, 'Número de cartão deve ter 16 dígitos')
      .regex(/^(?:\d{4} ){3}\d{4}$/, 'Número de cartão deve conter apenas números'),
    cardName: z
      .string({ required_error: 'Nome do titular é obrigatório' })
      .min(1, 'Nome do titular é obrigatório')
      .min(2, 'Nome deve ter pelo menos 2 caracteres')
      .regex(/^[a-zA-ZÀ-ÿ\s'-]+$/, 'Caracteres inválidos'),
    expiryDate: z
      .string({ required_error: 'Data de validade é obrigatória' })
      .min(1, 'Data de validade é obrigatória')
      .regex(/^(0[1-9]|1[0-2])\/([0-9]{2})$/, 'Formato inválido (MM/YY)')
      .refine((val) => {
        const [month, year] = val.split('/')
        const currentYear = new Date().getFullYear() % 100
        const currentMonth = new Date().getMonth() + 1
        return +year > currentYear || (+year === currentYear && +month >= currentMonth)
      }, 'Cartão expirado'),
    cvv: z
      .string({ required_error: 'CVV é obrigatório' })
      .min(1, 'CVV é obrigatório')
      .min(3, 'CVV deve ter 3 dígitos')
      .max(4, 'CVV deve ter no máximo 4 dígitos')
      .regex(/^\d+$/, 'Apenas números são permitidos'),
  }),
)

export function useCheckoutForm() {
  const { handleSubmit, resetForm } = useForm({
    validationSchema: checkoutSchema,
  })

  const cardNumber = useField<string>('cardNumber')
  const cardName = useField<string>('cardName')
  const expiryDate = useField<string>('expiryDate')
  const cvv = useField<string>('cvv')

  return {
    handleSubmit,
    resetForm,
    cardNumber,
    cardName,
    expiryDate,
    cvv,
  }
}
