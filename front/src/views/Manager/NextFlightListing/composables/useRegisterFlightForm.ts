import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'

const flightSchema = toTypedSchema(
  z
    .object({
      code: z.string({ required_error: 'Código do voo é obrigatório' }).min(1, 'Campo obrigatório'),
      originAirport: z
        .string({ required_error: 'O aeroporto de origem é obrigatório' })
        .min(1, 'Campo obrigatório'),
      destinationAirport: z
        .string({ required_error: 'O aeroporto de destino é obrigatório' })
        .min(1, 'Campo obrigatório'),
      price: z
        .string({ required_error: 'O preço é obrigatório' })
        .regex(/^R\$\s*\d+(?:\.\d{3})*,\d{2}$/, 'Insira no formato válido: R$1000,00'),
      seatsNumber: z.coerce
        .number({ invalid_type_error: 'A quantidade de poltronas é obrigatória' })
        .min(1, 'Deve ser maior que zero'),
      date: z.preprocess(
        (arg) => {
          if (typeof arg === 'string' || arg instanceof Date) {
            return new Date(arg)
          }
          return arg
        },
        z
          .date({ required_error: 'A data e hora do voo é obrigatória' })
          .refine((date) => date > new Date(), {
            message: 'A data e hora do voo não pode ser uma data passada',
          }),
      ),
    })
    .refine((val) => val.destinationAirport !== val.originAirport, {
      message: 'O aeroporto de origem não pode ser o mesmo que o aeroporto de destino',
      path: ['destinationAirport'],
    }),
)

export function useRegisterFlightForm() {
  const { handleSubmit } = useForm({
    validationSchema: flightSchema,
  })

  const code = useField<string>('code')
  const originAirport = useField<string>('originAirport')
  const destinationAirport = useField<string>('destinationAirport')
  const priceField = useField<string>('price')
  const seatsNumber = useField<number>('seatsNumber')
  const date = useField<string>('date')

  return {
    handleSubmit,
    code,
    originAirport,
    destinationAirport,
    priceField,
    seatsNumber,
    date,
  }
}
