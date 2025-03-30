import { useForm, useField } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'

const registerSchema = toTypedSchema(
  z.object({
    originAirpoirt: z.string().optional(),
    destinationAirpoirt: z.string().optional(),
  }),
)

export function useSearchFlightForm() {
  const { handleSubmit } = useForm({
    validationSchema: registerSchema,
  })

  const originAirport = useField<string>('originAirpoirt')
  const destinationAirpoirt = useField<string>('destinationAirpoirt')

  return {
    handleSubmit,
    originAirport,
    destinationAirpoirt,
  }
}
